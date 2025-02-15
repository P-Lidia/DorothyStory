package ru.plidia.dorothy.main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;


public class _Main {
    public static void main(String[] args) throws IOException {
        // удаляет старые папки
        Path directoryPath = Paths.get("data");
        if (isEmpty(directoryPath) == false) {
            deleteDirectory(directoryPath.toFile());
        }
        // создает новую папку
        String separator = File.separator;
        File directory = Files.createDirectories(new File(new StringBuilder()
                        .append("data")
                        .append(separator)
                        .append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        .toString())
                        .toPath())
                .toFile();
        // файл с текстом сказки
        File story = new File(new StringBuilder()
                .append("src")
                .append(separator)
                .append("main")
                .append(separator)
                .append("resources")
                .append(separator)
                .append("story.txt")
                .toString());
        // перебор и сортировка слов и знаков препинания
        Set<String> evenVowels = new HashSet<>();
        Set<String> oddVowels = new HashSet<>();
        Map<Character, Integer> punctuation = new HashMap();
        List<String> robotWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(story))) {
            while (br.ready()) {
                String[] storyText = br.readLine().split(" ");
                for (String word : storyText) {
                    int count = 0;
                    for (int i = word.length() - 1; i >= 0; i--) {
                        char ch = word.charAt(i);
                        if (ch == ',' || ch == '.' || ch == ':' || ch == ';' || ch == '?' || ch == '!') {
                            if (punctuation.containsKey(ch)) {
                                punctuation.put(ch, punctuation.get(ch) + 1);
                            } else {
                                punctuation.put(ch, 1);
                            }
                            word = word.substring(0, i);
                        } else {
                            if (Character.isUpperCase(ch)) {
                                ch = Character.toLowerCase(ch);
                            }
                            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' || ch == 'y') {
                                count++;
                            }
                        }
                    }
                    if (word.chars().anyMatch(ch -> Character.isDigit(ch))) {
                        robotWords.add(word);
                    } else {
                        if (count % 2 == 0) {
                            evenVowels.add(word);
                        } else {
                            oddVowels.add(word);
                        }
                    }
                }
            }
        }
        // записываем файл со знаками препинания
        File punctuationFile = Paths.get(new StringBuilder()
                        .append(directory)
                        .append(separator)
                        .append("punctuation.txt")
                        .toString())
                .toFile();
        try (PrintWriter pw = new PrintWriter(punctuationFile)) {
            punctuation.entrySet().stream()
                    .forEach(sign -> pw.println(new StringBuilder()
                            .append(sign.getKey())
                            .append(" - ")
                            .append(sign.getValue())));
        }
        // создаем файлы и записываем туда слова
        File file1 = Paths.get(new StringBuilder()
                        .append(directory)
                        .append(separator)
                        .append(robotWords.get(0))
                        .append(".txt")
                        .toString())
                .toFile();
        try (PrintWriter pw = new PrintWriter(file1)) {
            evenVowels.stream().forEach(word -> pw.print(word + " "));
        }
        File file2 = Paths.get(new StringBuilder()
                        .append(directory)
                        .append(separator)
                        .append(robotWords.get(1))
                        .append(".txt")
                        .toString())
                .toFile();
        try (PrintWriter pw = new PrintWriter(file2)) {
            oddVowels.stream().forEach(word -> pw.print(word + " "));
        }
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