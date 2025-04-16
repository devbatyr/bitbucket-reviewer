package com.shakenov.bitbucket.reviewer;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Entry point for the Bitbucket Code Review Bot application.
 * <p>
 * This class uses the {@link QuarkusMain} annotation to start the Quarkus runtime.
 */
@QuarkusMain
public class Application {

    /**
     * Launches the Quarkus application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String... args) {
        Quarkus.run(args);
    }
}
