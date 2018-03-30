package com.dante.ui.download;

import com.dante.data.model.UnLimit91PornItem;
import com.dante.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/27
 * @describe
 */

public interface DownloadView extends BaseView {
    void setDownloadingData(List<UnLimit91PornItem> unLimit91PornItems);

    void setFinishedData(List<UnLimit91PornItem> unLimit91PornItems);
}
