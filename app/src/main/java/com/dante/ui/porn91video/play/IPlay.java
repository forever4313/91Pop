package com.dante.ui.porn91video.play;

import com.dante.data.model.UnLimit91PornItem;

/**
 * @author flymegoc
 * @date 2017/11/27
 * @describe
 */

public interface IPlay extends IBasePlay {
    void loadVideoUrl(UnLimit91PornItem unLimit91PornItem);

    boolean isUserLogin();
    int getLoginUserId();
    void setFavoriteNeedRefresh(boolean favoriteNeedRefresh);



}
