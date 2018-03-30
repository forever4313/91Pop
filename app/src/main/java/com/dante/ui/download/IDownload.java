package com.dante.ui.download;

import com.dante.data.model.UnLimit91PornItem;

/**
 * @author flymegoc
 * @date 2017/11/27
 * @describe
 */

public interface IDownload extends IBaseDownload {
    void downloadVideo(UnLimit91PornItem unLimit91PornItem, boolean isDownloadNeedWifi, boolean isForceReDownload, DownloadPresenter.DownloadListener downloadListener);

    void loadDownloadingData();

    void loadFinishedData();

    void deleteDownloadingTask(UnLimit91PornItem unLimit91PornItem);

    void deleteDownloadedTask(UnLimit91PornItem unLimit91PornItem, boolean isDeleteFile);

    UnLimit91PornItem findUnLimit91PornItemByDownloadId(int downloadId);
}
