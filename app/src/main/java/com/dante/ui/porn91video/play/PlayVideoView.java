package com.dante.ui.porn91video.play;

import com.dante.data.model.UnLimit91PornItem;
import com.dante.data.model.VideoComment;
import com.dante.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/15
 * @describe
 */

public interface PlayVideoView extends BaseView {
    void showParsingDialog();

    void playVideo(UnLimit91PornItem unLimit91PornItem);

    void errorParseVideoUrl(String errorMessage);

    void favoriteSuccess();

    void setVideoCommentData(List<VideoComment> videoCommentList, boolean pullToRefresh);

    void setMoreVideoCommentData(List<VideoComment> videoCommentList);

    void noMoreVideoCommentData(String message);

    void loadMoreVideoCommentError(String message);

    void loadVideoCommentError(String message);

    void commentVideoSuccess(String message);

    void commentVideoError(String message);

    void replyVideoCommentSuccess(String message);

    void replyVideoCommentError(String message);
}
