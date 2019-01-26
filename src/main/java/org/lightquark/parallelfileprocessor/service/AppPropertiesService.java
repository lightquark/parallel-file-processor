package org.lightquark.parallelfileprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lightquark.parallelfileprocessor.properties.AppProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppPropertiesService {

    private final AppProperties appProperties;

    public int getParallelism() {
        int parallelism = appProperties.getParallelism();
        if (parallelism < 0 || parallelism >= Runtime.getRuntime().availableProcessors()) {
            parallelism = Runtime.getRuntime().availableProcessors() - 1;
        }
        log.info("Parallelism: {}", parallelism);
        return parallelism;
    }
}
