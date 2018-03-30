package com.dante.ui.porn91video.play;

import com.dante.data.model.UnLimit91PornItem;

/**
 * @author flymegoc
 * @date 2017/11/27
 * @describe
 */

public interface IPlay extends IBasePlay {
    void loadVideoUrl(UnLimit91PornItem unLimit91PornItem);

    void loadVideoComment(String videoId, String viewKey, boolean pullToRefresh);

    void commentVideo(String comment, String uid, String vid, String viewKey);

    void replyComment(String comment, String username, String vid, String commentId, String viewKey);
}
