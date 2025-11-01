package dev.oxydien.simpleModSync.content;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class PackedContentMetadata {
    public static final int CURRENT_SCHEME_VERSION = 1;
    public static final List<Integer> ALLOWED_SCHEME_VERSIONS = List.of(1);

    private final int schemeVersion;
    /// List of modified files (absolute path)
    private final List<String> config;

    public PackedContentMetadata(List<String> config) {
        this.schemeVersion = CURRENT_SCHEME_VERSION;
        this.config = config;
    }

    public static PackedContentMetadata FromJson(JsonObject jsonObject) {
        int schemeVersion = jsonObject.get("scheme_version").getAsInt();
        if (!ALLOWED_SCHEME_VERSIONS.contains(schemeVersion)) {
            throw new UnsupportedOperationException("Invalid scheme version: " + schemeVersion);
        }

        List<String> config = jsonObject.get("config").getAsJsonArray()
                .asList()
                .stream()
                .map(JsonElement::getAsString)
                .collect(Collectors.toList());
        return new PackedContentMetadata(config);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("scheme_version", schemeVersion);

        JsonArray configArray = new JsonArray();
        for (String configItem : config) {
            configArray.add(configItem);
        }

        jsonObject.add("config", configArray);
        return jsonObject;
    }

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(toJson());
    }

    public int getSchemeVersion() {
        return schemeVersion;
    }

    /// List of modified files (absolute path)
    public List<String> getConfig() {
        return config;
    }
}
