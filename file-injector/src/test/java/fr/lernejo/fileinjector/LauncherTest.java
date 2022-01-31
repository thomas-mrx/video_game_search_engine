package fr.lernejo.fileinjector;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class LauncherTest {

    @Test
    void start_and_try_to_send() {
        File resourcesDirectory = new File("src/test/resources");
        String file = resourcesDirectory.getAbsolutePath()+"/games.json";
        assertTimeoutPreemptively(
            Duration.ofSeconds(10L),
            () -> Launcher.main(new String[]{file}));
    }

    @Test
    void no_file() {
        assertTimeoutPreemptively(
            Duration.ofSeconds(10L),
            () -> Launcher.main(new String[]{}));
    }

    @Test
    void wrong_file() {
        File resourcesDirectory = new File("src/test/resources");
        String file = resourcesDirectory.getAbsolutePath()+"/wrong.json";
        assertThrows(IOException.class, () -> Launcher.main(new String[]{file}));
    }
}
