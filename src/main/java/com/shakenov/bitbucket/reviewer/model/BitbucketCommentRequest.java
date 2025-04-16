package com.shakenov.bitbucket.reviewer.model;

/**
 * Represents a comment to be posted on a Bitbucket pull request.
 * Only the {@code text} field is required when posting a general comment.
 * If you want to comment on a specific line, use an {@code anchor} as well.
 */
public class BitbucketCommentRequest {

    /**
     * The comment text to be posted.
     */
    private String text;

    public BitbucketCommentRequest() {
    }

    /**
     * Creates a comment request with the given text.
     *
     * @param text the content of the comment
     */
    public BitbucketCommentRequest(String text) {
        this.text = text;
    }

    /**
     * Returns the comment text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the comment text.
     *
     * @param text the content of the comment
     */
    public void setText(String text) {
        this.text = text;
    }
}
