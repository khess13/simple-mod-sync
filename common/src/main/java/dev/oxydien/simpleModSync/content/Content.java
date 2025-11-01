package dev.oxydien.simpleModSync.content;

public class Content {
    private final String uri;
    private final ContentType type;
    private final String name;
    private final String version;

    public Content(String uri, ContentType type, String name, String version) {
        this.uri = uri;
        this.type = type;
        this.name = name;
        this.version = version;
    }

    public String getUri() {
        return uri;
    }

    public ContentType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
