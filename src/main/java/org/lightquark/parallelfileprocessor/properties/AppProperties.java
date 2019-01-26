package org.lightquark.parallelfileprocessor.properties;

import lombok.Getter;
import lombok.Setter;
import org.lightquark.parallelfileprocessor.model.Command;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Component
@ConfigurationProperties
public class AppProperties {

    @NotNull
    private Integer parallelism;
    @NotNull
    private Command command;
    @NotBlank
    private String sourceDir;
    @NotBlank
    private String destinationDir;
    @NotNull
    @Value("${generate-test-files.num-of-files}")
    private Integer numOfFiles;
    @NotNull
    @Value("${generate-test-files.num-of-lines}")
    private Integer numOfLines;

}
