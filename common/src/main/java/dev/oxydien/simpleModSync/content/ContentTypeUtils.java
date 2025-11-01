package dev.oxydien.simpleModSync.content;

public class ContentTypeUtils {
    public static String ToString(ContentType type) {
        return switch (type) {
            case Mod -> "mod";
            case ResourcePack -> "resourcepack";
            case ShaderPack -> "shader";
            case DataPack -> "datapack";
            case Packed, Config -> "packed";
        };
    }

    public static ContentType FromString(String type) {
        return switch (type) {
            case "resourcepack" -> ContentType.ResourcePack;
            case "datapack"  -> ContentType.DataPack;
            case "shader"  -> ContentType.ShaderPack;
            case "config", "packed" -> ContentType.Packed;
            default -> ContentType.Mod;
        };
    }
}
