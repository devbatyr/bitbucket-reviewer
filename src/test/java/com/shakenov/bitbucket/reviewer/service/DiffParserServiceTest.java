package com.shakenov.bitbucket.reviewer.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiffParserServiceTest {

    private final DiffParserService diffParserService = new DiffParserService();

    @Test
    void shouldExtractModifiedPathsFromDiff() {
        String diff = """
                diff --git a/src/main/java/com/example/Foo.java b/src/main/java/com/example/Foo.java
                diff --git a/app/Bar.java b/app/Bar.java
                """;

        List<String> paths = diffParserService.extractModifiedPaths(diff);

        assertEquals(List.of(
                "a/src/main/java/com/example/Foo.java",
                "a/app/Bar.java"
        ), paths);
    }

    @Test
    void shouldIgnoreNonDiffLines() {
        String diff = """
                some random line
                another irrelevant line
                """;

        List<String> paths = diffParserService.extractModifiedPaths(diff);

        assertEquals(List.of(), paths);
    }
}
