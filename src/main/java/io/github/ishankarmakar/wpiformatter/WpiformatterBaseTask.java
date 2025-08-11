package io.github.ishankarmakar.wpiformatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.process.ExecOperations;

abstract class WpiformatterBaseTask extends DefaultTask {
    protected ExecOperations execOperations;

    WpiformatterBaseTask(ExecOperations execOperations) {
        this.execOperations = execOperations;
        setGroup("Formatting");
    }

    @TaskAction
    void taskAction() {
        Project project = getProject();
        WpiformatterExtension ext = project.getExtensions().findByType(WpiformatterExtension.class);
        Objects.requireNonNull(ext);
        if (ext.dirs == null) {
            project.getLogger().error("No wpiformat source directories specified");
            return;
        }
        if (ext.compileCommandsPath == null) {
            project.getLogger().error("No compile commands path specified");
            return;
        }

        List<String> args = new ArrayList<>(Arrays.asList("wpiformat", "-f"));
        for (String dir : ext.dirs)
            args.add(dir);
        args.addAll(Arrays.asList(
            "-compile-commands", ext.compileCommandsPath,
            OperatingSystem.current().isWindows() ? "-tidy-all" : "-tidy-changed"
        ));

        execOperations.exec(exec -> {
            exec.commandLine(args);
            exec.setIgnoreExitValue(true);
        });
    }
}
