"""
minecraft_instance.py
Generates a sync schema file from a Minecraft instance directory
by resolving installed files against the Modrinth API.
"""

import sys
import os
import hashlib
import json
import subprocess

# ── Dependency check ────────────────────────────────────────────────────────


def ensure_requests():
    try:
        import requests
    except ImportError:
        print("[!] 'requests' is not installed.")
        answer = input("    Install it now via pip? (y/n): ").strip().lower()
        if answer == "y":
            subprocess.check_call([sys.executable, "-m", "pip", "install", "requests"])
            import requests
        else:
            print("[!] Cannot continue without 'requests'. Exiting.")
            sys.exit(1)


ensure_requests()
import requests  # noqa: E402  (imported after install check)

# ── Constants ────────────────────────────────────────────────────────────────

SCHEMA_VERSION = 3
API_BASE = "https://api.modrinth.com/v2"
USER_AGENT = "modrinth_export_script/1.0"
CHUNK_SIZE = 64  # files per API batch (API limit is ~500 hashes)

FOLDER_TYPE_MAP = {
    "mods": "mod",
    "resourcepacks": "resourcepack",
    "shaderpacks": "shaderpack",
}

TYPE_FOLDER_MAP = {v: k for k, v in FOLDER_TYPE_MAP.items()}  # reverse lookup

# ── Helpers ──────────────────────────────────────────────────────────────────


def sha512(path: str) -> str:
    h = hashlib.sha512()
    with open(path, "rb") as f:
        for chunk in iter(lambda: f.read(65536), b""):
            h.update(chunk)
    return h.hexdigest()


def collect_files(folder: str) -> dict[str, str]:
    """Return {sha512_hash: absolute_path} for all files in *folder*."""
    result = {}
    if not os.path.isdir(folder):
        return result
    for fname in os.listdir(folder):
        fpath = os.path.join(folder, fname)
        if os.path.isfile(fpath):
            result[sha512(fpath)] = fpath
    return result


def batch_lookup(hashes: list[str]) -> dict[str, dict]:
    """
    POST /v2/version_files for up to CHUNK_SIZE hashes at a time.
    Returns {hash: version_object}.
    """
    found = {}
    for i in range(0, len(hashes), CHUNK_SIZE):
        chunk = hashes[i : i + CHUNK_SIZE]
        resp = requests.post(
            f"{API_BASE}/version_files",
            json={"hashes": chunk, "algorithm": "sha512"},
            headers={"User-Agent": USER_AGENT},
            timeout=15,
        )
        resp.raise_for_status()
        found.update(resp.json())
    return found


def batch_projects(project_ids: list[str]) -> dict[str, dict]:
    """
    GET /v2/projects for up to 500 IDs at a time.
    Returns {project_id: project_object}.
    """
    result = {}
    for i in range(0, len(project_ids), 500):
        chunk = project_ids[i : i + 500]
        resp = requests.get(
            f"{API_BASE}/projects",
            params={"ids": json.dumps(chunk)},
            headers={"User-Agent": USER_AGENT},
            timeout=15,
        )
        resp.raise_for_status()
        for proj in resp.json():
            result[proj["id"]] = proj
    return result


def pick_primary_file(version: dict) -> dict | None:
    """Return the primary file entry from a version object, or first available."""
    files = version.get("files", [])
    for f in files:
        if f.get("primary"):
            return f
    return files[0] if files else None


# ── Main ─────────────────────────────────────────────────────────────────────


def main():
    print("=" * 52)
    print("   S-M-S Modrinth fetcher")
    print("=" * 52)

    # 1. Get instance directory
    while True:
        instance_dir = (
            input("\nEnter your Minecraft instance directory path:\n> ")
            .strip()
            .strip('"')
        )
        if os.path.isdir(instance_dir):
            break
        print(f"[!] Directory not found: {instance_dir!r}. Please try again.")

    # 2. Ask which folders to include
    selected_folders: dict[str, str] = {}  # {folder_name: full_path}
    print()
    for folder_name in FOLDER_TYPE_MAP:
        folder_path = os.path.join(instance_dir, folder_name)
        if not os.path.isdir(folder_path):
            print(f"[~] '{folder_name}' folder not found, skipping.")
            continue
        ans = input(f"Include '{folder_name}'? (Y/n): ").strip().lower()
        if ans == "y" or len(ans) == 0:
            selected_folders[folder_name] = folder_path

    if not selected_folders:
        print("\n[!] No folders selected. Nothing to export.")
        sys.exit(0)

    # 3. Hash all files
    print("\n[*] Hashing files...")
    all_hashes: dict[str, tuple[str, str]] = {}  # {hash: (folder_name, path)}
    for folder_name, folder_path in selected_folders.items():
        files = collect_files(folder_path)
        for h, path in files.items():
            all_hashes[h] = (folder_name, path)
        print(f"    {folder_name}: {len(files)} file(s)")

    if not all_hashes:
        print("[!] No files found in selected folders.")
        sys.exit(0)

    # 4. Resolve hashes against Modrinth API
    print(f"\n[*] Looking up {len(all_hashes)} file(s) on Modrinth...")
    try:
        found = batch_lookup(list(all_hashes.keys()))
    except requests.RequestException as e:
        print(f"[!] API error during hash lookup: {e}")
        sys.exit(1)

    # Warn about unrecognised files
    not_found = [
        os.path.basename(path) for h, (_, path) in all_hashes.items() if h not in found
    ]
    if not_found:
        print(f"\n[~] {len(not_found)} file(s) not found on Modrinth (skipped):")
        for fname in not_found:
            print(f"    - {fname}")

    if not found:
        print("\n[!] No files could be resolved. Schema will be empty.")

    # 5. Fetch project names in bulk
    project_ids = list({v["project_id"] for v in found.values()})
    print(f"\n[*] Fetching project names for {len(project_ids)} project(s)...")
    try:
        projects = batch_projects(project_ids)
    except requests.RequestException as e:
        print(f"[!] API error during project lookup: {e}")
        sys.exit(1)

    # 6. Build schema entries
    sync_entries = []
    for file_hash, version in found.items():
        folder_name, _ = all_hashes[file_hash]
        file_entry = pick_primary_file(version)
        if not file_entry:
            continue

        project = projects.get(version["project_id"], {})
        proj_name = project.get("slug", version["project_id"])  # fallback to ID
        entry_type = FOLDER_TYPE_MAP[folder_name]

        sync_entries.append(
            {
                "url": file_entry["url"],
                "name": proj_name,
                "version": version["id"],  # 8-char Modrinth version ID
                "type": entry_type,
            }
        )

    # Sort alphabetically by name for readability
    sync_entries.sort(key=lambda e: e["name"].lower())

    # 8. Cross-check against existing schema for removed entries
    removal_entries = []
    script_dir = os.path.dirname(os.path.abspath(__file__))
    output_path = os.path.join(script_dir, "sync_schema.json")

    if os.path.isfile(output_path):
        ans = (
            input("\nExisting schema found. Cross-check for removed entries? (Y/n): ")
            .strip()
            .lower()
        )
        if ans == "y" or len(ans) == 0:
            with open(output_path, encoding="utf-8") as f:
                old_schema = json.load(f)

            new_slugs = {e["name"] for e in sync_entries}
            old_sync_entries = [
                e
                for e in old_schema.get("sync", [])
                if e.get("type") != "remove"  # ignore existing removal entries
            ]

            removed = [e for e in old_sync_entries if e["name"] not in new_slugs]

            if removed:
                print(f"\n[~] {len(removed)} entry/entries removed from instance:")
                for e in removed:
                    folder = TYPE_FOLDER_MAP.get(e["type"], e["type"] + "s")
                    ext = "jar" if e["type"] == "mod" else "zip"
                    pattern = f"^{folder}/{e['name']}-.*\\.{ext}$"
                    print(f"    - {e['name']}  →  {pattern}")
                    removal_entries.append(
                        {
                            "type": "remove",
                            "pattern": pattern,
                            "path": ".",
                        }
                    )
            else:
                print("\n[+] No removed entries detected.")

    schema = {
        "sync_version": SCHEMA_VERSION,
        "sync": sync_entries,
        **({"modify": removal_entries} if removal_entries else {}),
    }

    # 9. Save output next to this script

    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(schema, f, indent=2, ensure_ascii=False)

    print(f"\n[+] Done! {len(sync_entries)} entry/entries written to:")
    print(f"    {output_path}")


if __name__ == "__main__":
    main()
