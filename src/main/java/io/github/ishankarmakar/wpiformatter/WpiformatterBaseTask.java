package io.github.ishankarmakar.wpiformatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecOperations;

abstract class WpiformatterBaseTask extends DefaultTask {
    protected ExecOperations execOperations;

    WpiformatterBaseTask(ExecOperations execOperations) {
        this.execOperations = execOperations;
        setGroup("Formatting");
    }

    @TaskAction
    void taskAction() {
        WpiformatterExtension ext = getProject().getExtensions().findByType(WpiformatterExtension.class);
        Objects.requireNonNull(ext);

        List<String> args = new ArrayList<>(Arrays.asList("wpiformat", "-f"));
        for (String dir : ext.dirs)
            args.add(dir);
        args.addAll(Arrays.asList("-compile-commands", ext.compileCommandsPath));

        execOperations.exec(exec -> {
            exec.commandLine(args);
            exec.setIgnoreExitValue(true);
        });
    }
}
