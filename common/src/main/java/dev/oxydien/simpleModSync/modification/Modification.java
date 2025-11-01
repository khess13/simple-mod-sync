package dev.oxydien.simpleModSync.modification;

public class Modification {
    private final ModificationType type;
    private final String pattern;
    private final String path;

    public Modification(ModificationType type, String pattern, String path) {
        this.type = type;
        this.pattern = pattern;
        this.path = path;
    }

    public ModificationType getType() {
        return this.type;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String getPath() {
        return this.path;
    }
}
