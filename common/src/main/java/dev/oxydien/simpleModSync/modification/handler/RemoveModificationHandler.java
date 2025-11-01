package dev.oxydien.simpleModSync.modification.handler;

import dev.oxydien.simpleModSync.modification.Modification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RemoveModificationHandler extends ModificationHandler<Modification> {
    @Override
    public void ApplyOn(Modification mod, Path filePath) throws IOException {
        Files.delete(filePath);
    }
}
