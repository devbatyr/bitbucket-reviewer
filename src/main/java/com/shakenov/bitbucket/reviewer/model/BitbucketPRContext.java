package com.shakenov.bitbucket.reviewer.model;

/**
 * A DTO representing the context of a Bitbucket Pull Request.
 * Used for receiving webhook payloads and passing repository and PR metadata.
 */
public class BitbucketPRContext {

    private String workspace;
    private String repoSlug;
    private int prId;

    /**
     * Default constructor required for JSON deserialization.
     */
    public BitbucketPRContext() {}

    /**
     * Gets the Bitbucket project key (workspace).
     *
     * @return the workspace identifier
     */
    public String getWorkspace() {
        return workspace;
    }

    /**
     * Sets the Bitbucket project key (workspace).
     *
     * @param workspace the workspace identifier
     */
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    /**
     * Gets the repository slug.
     *
     * @return the repository slug
     */
    public String getRepoSlug() {
        return repoSlug;
    }

    /**
     * Sets the repository slug.
     *
     * @param repoSlug the repository slug
     */
    public void setRepoSlug(String repoSlug) {
        this.repoSlug = repoSlug;
    }

    /**
     * Gets the pull request ID.
     *
     * @return the pull request ID
     */
    public int getPrId() {
        return prId;
    }

    /**
     * Sets the pull request ID.
     *
     * @param prId the pull request ID
     */
    public void setPrId(int prId) {
        this.prId = prId;
    }
}
