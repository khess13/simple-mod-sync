# Simple Mod Sync Translators

## Overview

This is a collection of scripts that translate modpacks from various formats to the Simple Mod Sync format. 

### Currently Supported Translators

- **Maintained**:
  - [Minecraft instance](#minecraft-instance) (recommended)
    - Script: `minecraft_instance.py`
    - Dependencies: `requests`

- **Not maintained**:
  - These might still work, but nobody is checking them
  - [Modrinth app](#modrinth-app-translator)
      - Script: `index.js`
  - [Modrinth versions](#modrinth-versions)
      - Script: `modrinth_versions.py`
    - Dependencies: `requests`
  - [Static folder](#static-folder)
      - Script: `static.py`

### Contribute translator

If you want to help translate the modpacks, please [open an issue](https://github.com/oxydien/simple-mod-sync/issues/new) or [create a pull request](https://github.com/oxydien/simple-mod-sync/pulls).

This will be highly appreciated.

## Minecraft Instance

This tool scans your Minecraft instance folder and automatically builds a sync schema file
by identifying your installed mods, resource packs, and shader packs through the Modrinth database.

> **Don't worry if you're not tech-savvy** — the script is designed to guide you through every step interactively.
> If something goes wrong, the error messages will tell you exactly what the problem is.

Covers `mods`, `resourcepacks`, and `shaderpacks`. Any other content (e.g. worlds, configs) will need to be added to the schema manually.

If a schema file already exists next to the script, it can also detect entries that were
removed from your instance and automatically add removal instructions to the new schema.

### Prerequisites

- **Python 3.10 or newer** — see installation below if you don't have it
- **Internet connection** — used to look up your files on the Modrinth API

### Step 1 — Install Python (skip if you already have it)

Python is a free programming language runtime required to execute this script.

- **Windows (easiest):** Open the **Microsoft Store**, search for **Python**, and install the latest version (3.12 or higher).
- **Windows (alternative):** Download the installer from [python.org/downloads](https://www.python.org/downloads/) and run it.
  Make sure to check **"Add Python to PATH"** during installation — this is important!
- **macOS:** Python comes pre-installed, but it may be outdated. Install a fresh copy from [python.org/downloads](https://www.python.org/downloads/) if needed.
- **Linux:** Use your package manager, e.g. `sudo apt install python3` on Debian/Ubuntu.

### Step 2 — Download the Script

Download **[minecraft_instance.py](./minecraft_instance.py)** and save it somewhere easy to find,
like your Desktop or a dedicated folder. [[⬇ Direct download](https://raw.githubusercontent.com/oxydien/simple-mod-sync/refs/heads/main/translators/minecraft_instance.py)]

### Step 3 — Open a Terminal

A terminal is a text-based window you type commands into. It looks intimidating, but you'll only need it for a moment.

- **Windows:** Press `Win + R`, type `powershell`, and hit Enter.
- **macOS:** Open **Spotlight** (`Cmd + Space`), type **Terminal**, and hit Enter.
- **Linux:** You know the drill.

### Step 4 — Run the Script

In your terminal, type the following and press Enter:

```
py "C:\Path\To\minecraft_instance.py"
```

Replace the path with the actual location of the file you downloaded. On macOS/Linux, use `python3` instead of `py`.

> **Tip:** You can drag and drop the `.py` file into the terminal window instead of typing the path manually.

The script will handle the rest — including installing any missing dependencies automatically.
If it asks to install the `requests` library, type `y` and press Enter.


### Step 5 — Find Your Instance Folder

When prompted, enter the path to your Minecraft instance folder.
This is the folder that contains sub-folders like `mods`, `resourcepacks`, and `saves`.

| Launcher             | Operating System | Path                                                                         |
|----------------------|------------------|------------------------------------------------------------------------------|
| **Modrinth App**     | Windows          | `%AppData%\ModrinthApp\profiles\PROFILE_NAME`                                |
| **Modrinth App**     | Linux (Flathub)  | `~/.var/app/com.modrinth.ModrinthApp/data/ModrinthApp/profiles/PROFILE_NAME` |
| **Prism Launcher**   | Windows          | `%AppData%\PrismLauncher\instances\INSTANCE_NAME\.minecraft`                 |
| **Prism Launcher**   | Linux            | `~/.local/share/PrismLauncher/instances/INSTANCE_NAME\.minecraft`            |
| **Vanilla Launcher** | Windows          | `%AppData%\.minecraft`                                                       |
| **Vanilla Launcher** | macOS            | `~/Library/Application Support/minecraft`                                    |

> **Tip (Windows):** You can paste paths like `%AppData%\...` directly into the **File Explorer address bar** to navigate there quickly, then copy the full path from there.

### Output

The script saves `sync_schema.json` in the **same folder as the script itself**.
Any files that could not be matched to a Modrinth project will be listed as warnings and skipped —
you can add those manually to the schema afterwards if needed.

## Modrinth App Translator

The Modrinth app translator converts modpack instances from the Modrinth format to the Simple Mod Sync format.

### Prerequisites

- Node.js installed on your system
- Git (for cloning the repository)

### Initial setup

1. Clone the repository:
   ```
   git clone https://github.com/oxydien/simple-mod-sync.git
   cd simple-mod-sync
   ```

2. Navigate to the `translators` folder:
   ```
   cd translators
   ```

3. Create an `input` directory:
   ```
   mkdir input
   ```

### Usage

1. Open the Modrinth app and navigate to the content page of the instance you want to translate.

2. Open the Developer Tools:
    - Windows/Linux: Press `Ctrl + Shift + I`
    - macOS: Press `Cmd + Option + I`

3. Switch to the "Network" tab in the Developer Tools.

4. Refresh the page:
    - Windows/Linux: Press `Ctrl + Shift + R`
    - macOS: Press `Cmd + Option + R`

5. In the network request list, find the request containing `get_version_many`:
    - Click on the request
    - Copy the content of the response
    - Save it in the `input` folder as `input.json`

6. Find the request containing `get_project_many`:
    - Click on the request
    - Copy the content of the response
    - Save it in the `input` folder as `input-projects.json`

7. Run the translator script:
   ```
   node index.js
   ```

8. The translated output will be generated in the `output` folder.

### Notes

- Input file names are flexible:
    - The main input file must end with `.json`
    - The projects file must end with `-projects.json`
- You can translate multiple instances simultaneously by including multiple pairs of input files in the `input` folder.

### Output

The translator will generate files in the Simple Mod Sync format in the `output` folder.

### Troubleshooting

If you encounter any issues:
1. Ensure you have the latest version of Node.js installed
2. Check that the input JSON files are valid and complete
3. Verify that you have write permissions in the `output` folder

## Modrinth Versions

The Modrinth versions translator converts file containing versionIDs to the Simple Mod Sync format.

### Prerequisites

- Python 3 installed on your system
- Internet connection (Modrinth API)

### Usage

1. Create a text file (default: versions.txt) containing Modrinth version IDs, one per line
2. Run the script: `python3 modrinth_versions.py`.
3. The translated output will be generated in the `output` file (default: sync.json).

## Static Folder

The static folder translator converts all files in a given directory to the Simple Mod Sync format.

### Prerequisites

- Python 3 installed on your system

### Usage

1. Edit the configuration at the top of the file to match your needs.
2. Run the script: `python3 static.py`.
3. The translated output will be generated in the `output` file (default: sync.json).
4. Host the generated file on your server.
