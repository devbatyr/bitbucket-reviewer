package com.shakenov.bitbucket.reviewer.client;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.shakenov.bitbucket.reviewer.model.BitbucketCommentRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client interface for interacting with the Bitbucket Server REST API v1.0.
 */
@RegisterRestClient(configKey = "bitbucket-api")
@Path("/rest/api/1.0")
public interface BitbucketApiClient {

    /**
     * Retrieves the raw diff of a specific pull request.
     *
     * @param workspace the project key (e.g. BCON)
     * @param repo      the repository slug (e.g. msa)
     * @param prId      the ID of the pull request
     * @return the raw diff as plain text
     */
    @GET
    @Path("/projects/{workspace}/repos/{repo_slug}/pull-requests/{pr_id}/diff")
    @Produces(MediaType.TEXT_PLAIN)
    String getPullRequestDiff(
            @PathParam("workspace") String workspace,
            @PathParam("repo_slug") String repo,
            @PathParam("pr_id") int prId
    );

    /**
     * Retrieves metadata/details of a specific pull request.
     *
     * @param workspace the project key
     * @param repo      the repository slug
     * @param prId      the pull request ID
     * @return the PR details as JSON string
     */
    @GET
    @Path("/projects/{workspace}/repos/{repo_slug}/pull-requests/{pr_id}")
    @Produces(MediaType.APPLICATION_JSON)
    String getPullRequestDetails(
            @PathParam("workspace") String workspace,
            @PathParam("repo_slug") String repo,
            @PathParam("pr_id") int prId
    );

    /**
     * Fetches the raw content of a file at a specific commit.
     *
     * @param workspace the project key
     * @param repo      the repository slug
     * @param filePath  path to the file (relative in the repo)
     * @param commitSha the commit hash to fetch the file from
     * @return file contents as plain text
     */
    @GET
    @Path("/projects/{workspace}/repos/{repo_slug}/raw/{path}")
    @Produces(MediaType.TEXT_PLAIN)
    String getFileContent(
            @PathParam("workspace") String workspace,
            @PathParam("repo_slug") String repo,
            @PathParam("path") String filePath,
            @QueryParam("at") String commitSha
    );

    /**
     * Posts a general (not inline) comment to a pull request.
     *
     * @param workspace the project key
     * @param repo      the repository slug
     * @param prId      the pull request ID
     * @param comment   the comment payload
     * @return the response from the Bitbucket API
     */
    @POST
    @Path("/projects/{workspace}/repos/{repo_slug}/pull-requests/{pr_id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    Response postComment(
            @PathParam("workspace") String workspace,
            @PathParam("repo_slug") String repo,
            @PathParam("pr_id") int prId,
            BitbucketCommentRequest comment
    );
}
