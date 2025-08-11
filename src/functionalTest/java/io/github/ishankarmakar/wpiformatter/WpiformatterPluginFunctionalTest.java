package io.github.ishankarmakar.wpiformatter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.gradle.internal.impldep.org.eclipse.jgit.api.Git;
import org.gradle.internal.impldep.org.eclipse.jgit.api.errors.GitAPIException;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class WpiformatterPluginFunctionalTest {
    @TempDir
    File projectDir;

    @BeforeEach
    void createProject() throws IOException, GitAPIException {
        File file = new File(projectDir, "settings.gradle");
        file.createNewFile();
        Files.writeString(
            Paths.get(projectDir.getPath(), "build.gradle"),
            "plugins { id('io.github.ishan-karmakar.wpiformatter') }" + System.lineSeparator(),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
        );
        Git.init().setDirectory(projectDir).call().commit().setMessage("Empty commit").call();
    }

    GradleRunner getRunner() {
        GradleRunner runner = GradleRunner.create();
        return runner
                .forwardOutput()
                .withPluginClasspath()
                .withProjectDir(projectDir);
    }

    @Test
    void testNoSrcDirs() {
        GradleRunner runner = getRunner();
        BuildResult result = runner.withArguments("format").build();
        assertTrue(result.getOutput().contains("No wpiformat source directories specified"));
        result = runner.withArguments("lint").build();
        assertTrue(result.getOutput().contains("No wpiformat source directories specified"));
    }

    @Test
    void testSrcDirs() throws IOException {
        Files.writeString(
            Paths.get(projectDir.getPath(), "build.gradle"),
            "wpiformatter.dirs = ['src/']",
            StandardOpenOption.APPEND
        );
        System.out.println(projectDir.getAbsolutePath());
        GradleRunner runner = getRunner();
        BuildResult result = runner.withArguments("format").build();
        assertFalse(result.getOutput().contains("No wpiformat source directories specified"));
        result = runner.withArguments("lint").build();
        assertFalse(result.getOutput().contains("No wpiformat source directories specified"));
    }
    
    @Test
    void testNoCompileCommands() throws IOException {
        Files.writeString(
            Paths.get(projectDir.getPath(), "build.gradle"),
            "wpiformatter.dirs = ['src/']",
            StandardOpenOption.APPEND
        );
        GradleRunner runner = getRunner();
        BuildResult result = runner.withArguments("format").build();
        assertTrue(result.getOutput().contains("No compile commands path specified"));
        result = runner.withArguments("lint").build();
        assertTrue(result.getOutput().contains("No compile commands path specified"));
    }
}
