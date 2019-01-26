package org.lightquark.parallelfileprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lightquark.parallelfileprocessor.properties.AppProperties;
import org.lightquark.parallelfileprocessor.utils.CardNumberUtils;
import org.lightquark.parallelfileprocessor.utils.Constants;
import org.lightquark.parallelfileprocessor.utils.FileUtils;
import org.lightquark.parallelfileprocessor.utils.ParallelUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamApiService {

    private final AppProperties appProperties;
    private final AppPropertiesService appPropertiesService;

    public void start() throws Exception {
        String sourceDir = appProperties.getSourceDir();
        String destinationDir = appProperties.getDestinationDir();
        log.info("Source dir: {}", sourceDir);
        log.info("Destination dir: {}", destinationDir);

        List<Path> sourcePaths = Files.walk(Paths.get(sourceDir))
                                      .filter(p -> p.toString().endsWith(Constants.EXTENSION))
                                      .distinct()
                                      .collect(Collectors.toList());

        ParallelUtils.run(getRunnable(sourcePaths, Paths.get(destinationDir)), appPropertiesService.getParallelism());
    }

    private Runnable getRunnable(List<Path> sourcePaths, Path destinationPath) {
        return () -> sourcePaths.stream()
                                .parallel()
                                .forEach(sourcePath -> processFile(sourcePath,
                                        destinationPath.resolve(sourcePath.getFileName())));
    }

    private void processFile(Path sourceFile, Path destinationFile) {
        try {
            Files.lines(sourceFile)
                 .parallel()
                 .filter(this::hasCardNumber)
                 .forEach(line -> FileUtils.writeLine(destinationFile, line + StringUtils.LF));
        } catch (IOException e) {
            log.error("Error occurs while processing file: {}.", sourceFile, e);
        }
    }

    private boolean hasCardNumber(String line) {
        return Arrays.stream(line.split(","))
                     .anyMatch(CardNumberUtils::validate);
    }
}
