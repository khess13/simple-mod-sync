package dev.oxydien.simpleModSync.content.handler;

import dev.oxydien.simpleModSync.content.Content;

import java.nio.file.Path;

public class ModContentHandler extends ContentHandler<Content> {
    @Override
    public String GetFileExtension() {
        return "jar";
    }

    @Override
    public Path GetDirectory(Path basePath) {
        return basePath.resolve("mods");
    }
}
