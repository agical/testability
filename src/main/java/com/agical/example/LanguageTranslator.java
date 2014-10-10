package com.agical.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

public class LanguageTranslator {

    private static Collection<Character> vowels;

    static {
        Set<Character> set = new HashSet<>();
        final char[] chars = "aeiouyåäö".toCharArray();
        for (char c : chars) {
            set.add(c);
        }
        vowels = Collections.unmodifiableSet(set);
    }

    public static void main(String[] args) throws IOException {
        final Path pathToInputs = Paths.get("to-translate");
        final Path pathToResults = Paths.get("translated");
        Files.walkFileTree(pathToInputs, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                final BufferedReader bufferedReader = Files.newBufferedReader(file, Charset.forName("UTF-8"));
                final Path newFile = pathToResults.resolve(file.getFileName());
                if (!Files.exists(newFile)) {
                    Files.createFile(newFile);
                }
                try(BufferedWriter resultWriter = Files.newBufferedWriter(newFile, Charset.forName("UTF-8"), WRITE, TRUNCATE_EXISTING)) {
                    int read = bufferedReader.read();
                    while(read != -1) {
                        char character = (char) read;
                        String toWrite;
                        if (vowels.contains(character)) {
                            toWrite = String.valueOf(character);
                        } else {
                            toWrite = character + "o" + character;
                        }

                        resultWriter.write(toWrite);
                        read = bufferedReader.read();
                    }
                }
                return super.visitFile(file, attrs);
            }
        });
    }
}
