package dev.oxydien.simpleModSync.modification;

import dev.oxydien.simpleModSync.content.ContentType;

public class ModificationTypeUtils {
    public static String ToString(ModificationType type) {
        return switch (type) {
            case Remove -> "remove";
            case Rename -> "rename";
            case Unknown -> "unknown";
        };
    }

    public static ModificationType FromString(String type) {
        return switch (type) {
            case "rename" -> ModificationType.Rename;
            case "remove" -> ModificationType.Remove;
            default -> ModificationType.Unknown;
        };
    }
}
