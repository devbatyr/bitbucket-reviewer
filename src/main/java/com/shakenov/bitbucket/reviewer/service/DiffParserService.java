package com.shakenov.bitbucket.reviewer.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Service for parsing a Git diff and extracting modified file paths.
 */
@ApplicationScoped
public class DiffParserService {

    /**
     * Extracts a list of unique modified file paths from the provided diff text.
     * <p>
     * The method looks for lines starting with {@code diff --git} and retrieves the "b/" path.
     * It also strips optional {@code src://} prefixes from paths.
     *
     * @param diffText the raw diff output from Bitbucket
     * @return a list of modified file paths
     */
    public List<String> extractModifiedPaths(String diffText) {
        return diffText.lines()
                .filter(line -> line.startsWith("diff --git"))
                .map(line -> {
                    String[] parts = line.split(" ");
                    return parts[2].replace("b/", "").replace("src://", "");
                })
                .distinct()
                .toList();
    }
}
