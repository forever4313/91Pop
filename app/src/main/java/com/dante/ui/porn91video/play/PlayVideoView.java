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

    void parseVideoUrlSuccess(UnLimit91PornItem v9PornItem);

    void errorParseVideoUrl(String errorMessage);

    void favoriteSuccess();

}
