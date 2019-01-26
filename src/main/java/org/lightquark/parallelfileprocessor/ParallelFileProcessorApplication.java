package org.lightquark.parallelfileprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lightquark.parallelfileprocessor.model.Command;
import org.lightquark.parallelfileprocessor.properties.AppProperties;
import org.lightquark.parallelfileprocessor.service.StreamApiService;
import org.lightquark.parallelfileprocessor.service.TestFilesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class ParallelFileProcessorApplication implements CommandLineRunner {

    private final AppProperties appProperties;
    private final StreamApiService streamApiService;
    private final TestFilesService testFilesService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ParallelFileProcessorApplication.class);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Application started");
        Command command = appProperties.getCommand();
        log.info("Command: {}", command);

        switch (command) {
            case GENERATE_TEST_FILES:
                testFilesService.start();
                break;
            case RUN_STREAM_API:
                streamApiService.start();
                break;
        }

        log.info("Application finished");
    }
}

