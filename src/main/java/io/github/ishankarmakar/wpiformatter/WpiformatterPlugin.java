package io.github.ishankarmakar.wpiformatter;

import org.gradle.api.Project;

import org.gradle.api.Plugin;

public class WpiformatterPlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getExtensions().add("wpiformatter", WpiformatterExtension.class);
        project.getTasks().register("format", WpiformatterFormatTask.class);
        project.getTasks().register("lint", WpiformatterLintTask.class);
    }
}
