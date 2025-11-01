package dev.oxydien.simpleModSync.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public class ZipUtils {
    public static List<Path> ExtractZipFile(Path zipFilePath, Path destinationDirectory) throws IOException {
        List<Path> extractedFiles = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(zipFilePath.toFile())) {
            zipFile.entries().asIterator().forEachRemaining(zipEntry -> {
                try {
                    Path path = destinationDirectory.resolve(zipEntry.getName());
                    if (zipEntry.isDirectory()) {
                        Files.createDirectories(path);
                        extractedFiles.add(path.toAbsolutePath());
                    } else {
                        Files.createDirectories(path.getParent());
                        Files.copy(zipFile.getInputStream(zipEntry), path);
                        extractedFiles.add(path.toAbsolutePath());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return extractedFiles;
    }
}
