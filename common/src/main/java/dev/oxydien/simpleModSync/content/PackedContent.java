package dev.oxydien.simpleModSync.content;

public class PackedContent extends Content{
    private String directory = "";

    public PackedContent(String uri, ContentType type, String name, String version) {
        super(uri, type, name, version);
    }

    public PackedContent(String uri, ContentType type, String name, String version, String directory) {
        super(uri, type, name, version);
        this.directory = directory;
    }

    public String getDirectory() {
        return this.directory;
    }
}
