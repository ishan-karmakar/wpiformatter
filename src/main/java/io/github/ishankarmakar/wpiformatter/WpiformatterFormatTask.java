package io.github.ishankarmakar.wpiformatter;

import javax.inject.Inject;

import org.gradle.process.ExecOperations;

abstract class WpiformatterFormatTask extends WpiformatterBaseTask {
    @Inject
    public WpiformatterFormatTask(ExecOperations execOperations) {
        super(execOperations, false);
        setDescription("Formats the target directories with wpiformat, IN PLACE");
    }
}
