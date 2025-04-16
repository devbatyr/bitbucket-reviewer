package com.shakenov.bitbucket.reviewer.service;

import com.shakenov.bitbucket.reviewer.client.BitbucketApiClient;
import com.shakenov.bitbucket.reviewer.config.PmdConfigProperties;
import com.shakenov.bitbucket.reviewer.model.BitbucketPRContext;
import com.shakenov.pmdcore.config.PmdConfig;
import com.shakenov.pmdcore.service.PmdService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.Optional;

/**
 * Service responsible for analyzing pull requests in Bitbucket using PMD.
 * <p>
 * It fetches the diff, retrieves modified files, runs PMD analysis,
 * and posts relevant feedback as comments on the pull request.
 * <p>
 * This is the central component coordinating code analysis and result publishing.
 */
@ApplicationScoped
public class BitbucketService {

    private final PmdService pmdService = new PmdService();

    private static final Logger log = Logger.getLogger(BitbucketService.class);

    @Inject CommitService commitService;
    @Inject DiffParserService diffParser;
    @Inject BitbucketCommentPublisher commentPublisher;

    @Inject
    PmdConfigProperties pmdProperties;
    @Inject @RestClient
    BitbucketApiClient apiClient;

    /**
     * Analyzes a pull request in Bitbucket.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Fetches PR metadata and the latest commit SHA</li>
     *     <li>Parses the list of modified files from the diff</li>
     *     <li>Fetches the content of each modified file</li>
     *     <li>Runs PMD analysis on each file</li>
     *     <li>Posts formatted results as comments on the pull request</li>
     * </ul>
     *
     * @param context the context of the pull request containing workspace, repository, and PR ID
     */
    public void analyzePullRequest(BitbucketPRContext context) {
        String workspace = context.getWorkspace();
        String repo = context.getRepoSlug();
        int prId = context.getPrId();

        String prJson = apiClient.getPullRequestDetails(workspace, repo, prId);
        String commitSha = commitService.extractCommitSha(prJson);

        String diff = apiClient.getPullRequestDiff(workspace, repo, prId);
        var paths = diffParser.extractModifiedPaths(diff);

        if (paths.isEmpty()) {
            log.warnf("No modified files found in PR #%d", prId);
            return;
        }

        paths.stream()
                .map(path -> fetchFileContentSafe(workspace, repo, path, commitSha))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(file -> createPmdConfig(file.path(), file.code()))
                .map(pmdService::runPmd)
                .forEach(response -> commentPublisher.publish(context, response.getFormattedOutput()));
    }

    /**
     * Attempts to fetch the content of a file from Bitbucket.
     * <p>
     * Any exceptions during retrieval are logged, and an empty Optional is returned.
     *
     * @param workspace the project key
     * @param repo the repository slug
     * @param path the file path
     * @param commitSha the commit hash to fetch from
     * @return an Optional containing the file content if successful
     */
    private Optional<AnalyzedFile> fetchFileContentSafe(String workspace, String repo, String path, String commitSha) {
        try {
            String code = apiClient.getFileContent(workspace, repo, path, commitSha);
            return Optional.of(new AnalyzedFile(path, code));
        } catch (Exception e) {
            log.errorf("Failed to fetch content for file %s: %s", path, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Builds a {@link PmdConfig} object for the given file path and content.
     *
     * @param path the file path
     * @param code the file content
     * @return a configured {@link PmdConfig} instance
     */
    private PmdConfig createPmdConfig(String path, String code) {
        return PmdConfig.builder()
                .fileName(path)
                .code(code)
                .pmdPath(pmdProperties.path())
                .rulesetPath(pmdProperties.ruleset())
                .suppressionsPath(pmdProperties.suppressions())
                .build();
    }

    /**
     * Internal record representing a file and its content to be analyzed.
     *
     * @param path the file path
     * @param code the file content
     */
    private record AnalyzedFile(String path, String code) {}

}
