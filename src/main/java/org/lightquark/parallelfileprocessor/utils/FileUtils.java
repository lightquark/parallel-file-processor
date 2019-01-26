package org.lightquark.parallelfileprocessor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {

    public static void writeLine(Path path, String line) {
        try {
            Files.write(path, line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("Error occurs while writing to file: {}.", path, e);
        }
    }
}
