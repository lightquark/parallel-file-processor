package org.lightquark.parallelfileprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightquark.parallelfileprocessor.properties.AppProperties;
import org.lightquark.parallelfileprocessor.utils.CardNumberUtils;
import org.lightquark.parallelfileprocessor.utils.Constants;
import org.lightquark.parallelfileprocessor.utils.FileUtils;
import org.lightquark.parallelfileprocessor.utils.ParallelUtils;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestFilesService {

    private static final float CARD_NUMBER_PROBABILITY = 0.1f; // 10%
    private static final Random RANDOM = new Random();

    private final AppProperties appProperties;
    private final AppPropertiesService appPropertiesService;

    public void start() {
        String sourceDir = appProperties.getSourceDir();
        log.info("Test files dir: {}", sourceDir);

        ParallelUtils.run(getRunnable(sourceDir), appPropertiesService.getParallelism());
    }

    private Runnable getRunnable(String sourceDir) {
        return () -> IntStream.range(0, appProperties.getNumOfFiles())
                              .parallel()
                              .forEach(intValue -> generateAndWriteLines(generateFilePath(sourceDir, intValue)));
    }

    private Path generateFilePath(String sourceDir, int value) {
        return Paths.get(sourceDir + StringUtils.leftPad(String.valueOf(value), 8, '0') + Constants.EXTENSION);
    }

    private void generateAndWriteLines(Path path) {
        Stream.iterate(generateLine(), line -> generateLine())
              .limit(appProperties.getNumOfLines())
              .parallel()
              .forEach(line -> FileUtils.writeLine(path, line));
    }

    private String generateLine() {
        return Stream.iterate(String.valueOf(System.currentTimeMillis()), value -> generateColumn())
                     .limit(10)
                     .collect(Collectors.joining(",", StringUtils.EMPTY, StringUtils.LF));
    }

    private String generateColumn() {
        return RANDOM.nextFloat() < CARD_NUMBER_PROBABILITY
                ? CardNumberUtils.generate(Constants.MASTERCARD_PREFIX, Constants.MASTERCARD_LENGTH)
                : RandomStringUtils.randomAlphanumeric(4, 12);
    }
}
