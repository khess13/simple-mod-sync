package dev.oxydien.simpleModSync.content.handler;

import dev.oxydien.simpleModSync.content.Content;

import java.nio.file.Path;

public class GameContentHandler extends ContentHandler<Content> {
    private final String extension;
    private final String directory;

    public GameContentHandler(String extension, String directory) {
        this.extension = extension;
        this.directory = directory;
    }

    public GameContentHandler(String directory) {
        this.extension = "zip";
        this.directory = directory;
    }


    @Override
    public String GetFileExtension() {
        return this.extension;
    }

    @Override
    public Path GetDirectory(Path basePath) {
        return basePath.resolve(this.directory);
    }
}
