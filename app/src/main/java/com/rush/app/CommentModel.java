package com.rush.app;

public class CommentModel {
    private String comment;
    private String userId;
    private String postId;
    private String commentId;

    public CommentModel(String id, String postId, String uid, String comment) {
    }

    public CommentModel(String comment, String userId, String postId) {
        this.comment = comment;
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
