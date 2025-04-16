package com.shakenov.bitbucket.reviewer.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service responsible for extracting the latest commit SHA
 * from Bitbucket pull request JSON details.
 */
@ApplicationScoped
public class CommitService {

    private static final Pattern COMMIT_SHA_PATTERN =
            Pattern.compile("\"fromRef\"\\s*:\\s*\\{[^}]*\"latestCommit\"\\s*:\\s*\"(\\w+)\"");

    /**
     * Extracts the latest commit SHA from the pull request JSON.
     *
     * @param prDetailsJson the JSON response from Bitbucket PR details API
     * @return the commit SHA string
     * @throws IllegalStateException if the commit SHA could not be extracted
     */
    public String extractCommitSha(String prDetailsJson) {
        Matcher matcher = COMMIT_SHA_PATTERN.matcher(prDetailsJson);
        if (!matcher.find()) {
            throw new IllegalStateException("Unable to extract commit SHA from PR");
        }
        return matcher.group(1);
    }
}
