package com.dante.ui.porn91video.comment;

public interface IComment {
    void loadVideoComment(String videoId, String viewKey, boolean pullToRefresh);

    void commentVideo(String comment, String uid, String vid, String viewKey);

    void replyComment(String comment, String username, String vid, String commentId, String viewKey);

    boolean isUserLogin();

    int getLoginUserId();
}
