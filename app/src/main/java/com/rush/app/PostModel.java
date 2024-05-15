package com.rush.app;

public class PostModel {
    private  String postId, userId, postText, postImage, postLikes, postComments;
    private  Long postingTime;
    private  boolean isLiked;

    public PostModel(String postId, String userId, String postText, String postImage, String postLikes, String postComments, Long postingTime) {
        this.postId = postId;
        this.userId = userId;
        this.postText = postText;
        this.postImage = postImage;
        this.postLikes = postLikes;
        this.postComments = postComments;
        this.postingTime = postingTime;
    }



    public PostModel() {
    }

    public PostModel(String number) {

    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(String postLikes) {
        this.postLikes = postLikes;
    }

    public String getPostComments() {
        return postComments;
    }

    public void setPostComments(String postComments) {
        this.postComments = postComments;
    }

    public Long getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(Long postingTime) {
        this.postingTime = postingTime;
    }
}
