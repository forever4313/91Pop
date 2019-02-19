package com.dante.ui.porn91video.comment;

import com.dante.data.model.VideoComment;
import com.dante.ui.BaseView;

import java.util.List;

public interface CommentView extends BaseView {

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
