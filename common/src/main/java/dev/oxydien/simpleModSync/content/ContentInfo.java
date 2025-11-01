package dev.oxydien.simpleModSync.content;

public class ContentInfo<T extends Content> {
    private final T content;

    public ContentInfo(T content) {
        this.content = content;
    }

    public String GetTitle() {
        return this.content.getName().substring(0, Math.min(22, this.content.getName().length()));
    }

    public T GetContent() {
        return this.content;
    }

    public ContentType GetContentType() {
        return this.content.getType();
    }
}
