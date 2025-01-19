package ru.plidia.dorothy.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class _Main {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("data");
        if (isEmpty(path) == false) {
            File directory = path.toFile();
            deleteDirectory(directory);
        }
        Files.createDirectories(new File("data/"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .toPath());


    }

    public static boolean isEmpty(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            }
        }
        return false;
    }

    public static void deleteDirectory(File directory) {
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File file : contents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }

}
