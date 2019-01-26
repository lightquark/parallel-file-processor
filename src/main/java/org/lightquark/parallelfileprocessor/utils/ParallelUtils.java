package org.lightquark.parallelfileprocessor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParallelUtils {

    public static void run(Runnable runnable, int parallelism) {
        ForkJoinPool forkJoinPool = null;
        try {
            forkJoinPool = new ForkJoinPool(parallelism);
            long startTime = System.currentTimeMillis();
            log.info("Task started at {}", startTime);

            forkJoinPool.submit(runnable).get();

            log.info("Task completed. Elapsed time: {} seconds.", getElapsedTime(startTime));
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error occurs while running task.", e);
        } finally {
            if (forkJoinPool != null) {
                forkJoinPool.shutdown();
            }
        }
    }

    private static String getElapsedTime(long startTime) {
        return String.format("%.2f", (System.currentTimeMillis() - startTime) / 1000d);
    }
}
