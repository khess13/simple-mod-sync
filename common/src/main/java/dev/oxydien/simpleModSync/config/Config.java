package dev.oxydien.simpleModSync.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.oxydien.simpleModSync.log.Log;

import java.io.*;
import java.nio.file.Path;

public class Config {
    private final Path path;
    private boolean autoDownload;
    private String downloadUrl;
    public static Config instance;


    public Config(Path configFilePath) {
        this.path = configFilePath;
        this.autoDownload = true;
        this.downloadUrl = "";

        this.load();
        this.save();

        instance = this;

        Log.debug("Config file loaded");
    }

    public Path getPath() {
        return this.path;
    }

    public boolean getAutoDownload() {
        return this.autoDownload;
    }

    public void setAutoDownload(boolean autoDownload) {
        this.autoDownload = autoDownload;
        this.save();
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        this.save();
    }

    // Deserialize from json file

    public void load() {
        // Read from json file
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(this.getPath().toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            Log.warning("config.load", "Config file not found, creating a default one", e);
            return;
        } catch (IOException e) {
            Log.error("config.load.IOException", "Failed to read config file", e);
        }

        // Parse json
        JsonElement jsonElement = JsonParser.parseString(content.toString());

        this.autoDownload = jsonElement.getAsJsonObject().get("auto_download") == null ||
                jsonElement.getAsJsonObject().get("auto_download").getAsBoolean();

        var downloadUrl = jsonElement.getAsJsonObject().get("download_url");
        if (downloadUrl != null && !downloadUrl.getAsString().isEmpty()) {
            this.downloadUrl = downloadUrl.getAsString();
        }
    }

    // Serialize to json file
    public void save() {
        // Create json
        JsonObject json = new JsonObject();
        json.addProperty("auto_download", this.autoDownload);
        json.addProperty("download_url", this.downloadUrl);

        // Write to json file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.getPath().toFile()))) {
            bw.write(json.toString());
        } catch (IOException e) {
            Log.error("config.save.IOException", "Failed to write config file", e);
        }
    }
}