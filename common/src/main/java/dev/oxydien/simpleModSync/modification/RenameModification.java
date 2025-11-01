package dev.oxydien.simpleModSync.modification;

public class RenameModification extends Modification {
    private final String result;

    public RenameModification(ModificationType type, String pattern, String path, String result) {
        super(type, pattern, path);
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
