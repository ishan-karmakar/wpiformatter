package io.github.ishankarmakar.wpiformatter;

import javax.inject.Inject;

import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecOperations;

abstract class WpiformatterLintTask extends WpiformatterBaseTask {
    @Inject
    public WpiformatterLintTask(ExecOperations execOperations) {
        super(execOperations);
        setDescription("Formats the target directories with wpiformat, but RESTORES CHANGES");
    }

    @TaskAction
    void taskAction() {
        execOperations.exec(exec -> exec.commandLine("git", "add", "-u"));
        execOperations
                .exec(exec -> exec.commandLine("git", "commit", "--allow-empty", "-m", "\"TMP: Formatting changes\""));
        super.taskAction();
        execOperations.exec(exec -> exec.commandLine("git", "reset", "--hard", "HEAD"));
        execOperations.exec(exec -> exec.commandLine("git", "reset", "HEAD~1"));
    }
}
