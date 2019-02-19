package com.dante.data.prefs;

import com.dante.data.model.User;

/**
 * @author flymegoc
 * @date 2018/2/12
 */

public interface PreferencesHelper {
    String getPorn91VideoAddress();

    void setPorn91VideoAddress(String address);

    String getPorn91ForumAddress();

    void setPorn91ForumAddress(String address);

    String getPigAvAddress();

    void setPigAvAddress(String address);

    String getPorn91VideoLoginUserName();

    void setPorn91VideoLoginUserName(String userName);

    void setPorn91VideoLoginUserPassWord(String passWord);

    String getPorn91VideoLoginUserPassword();

    boolean isPorn91VideoUserAutoLogin();

    void setPorn91VideoUserAutoLogin(boolean autoLogin);

    int getAppBarLayoutHeight();

    void setAppBarLayoutHeight(int height);

    boolean isFavoriteNeedRefresh();

    void setFavoriteNeedRefresh(boolean needRefresh);

    int getPlaybackEngine();

    void setPlaybackEngine(int playbackEngine);

    boolean isFirstInSearchPorn91Video();

    void setFirstInSearchPorn91Video(boolean firstInSearchPorn91Video);

    boolean isDownloadVideoNeedWifi();

    void setDownloadVideoNeedWifi(boolean downloadVideoNeedWifi);

    boolean isOpenHttpProxy();

    void setOpenHttpProxy(boolean openHttpProxy);

    boolean isOpenNightMode();

    void setOpenNightMode(boolean openNightMode);

    String getProxyIpAddress();

    void setProxyIpAddress(String proxyIpAddress);

    int getProxyPort();

    void setProxyPort(int port);

    boolean isNeverAskForWatchDownloadTip();

    void setNeverAskForWatchDownloadTip(boolean neverAskForWatchDownloadTip);

    int getIgnoreThisVersionUpdateTip();

    void setIgnoreThisVersionUpdateTip(int versionCode);

    boolean isForbiddenAutoReleaseMemory();

    void setForbiddenAutoReleaseMemory(boolean autoReleaseMemory);

    boolean isViewPorn91ForumContentShowTip();

    void setViewPorn91ForumContentShowTip(boolean contentShowTip);

    int getNoticeVersionCode();

    void setNoticeVersionCode(int noticeVersionCode);

    int getMainFirstTabShow();

    void setMainFirstTabShow(int firstTabShow);

    int getMainSecondTabShow();

    void setMainSecondTabShow(int secondTabShow);

    int getSettingScrollViewScrollPosition();

    void setSettingScrollViewScrollPosition(int position);

    boolean isOpenSkipPage();

    void setOpenSkipPage(boolean openSkipPage);

    String getCustomDownloadVideoDirPath();

    void setCustomDownloadVideoDirPath(String customDirPath);
}
