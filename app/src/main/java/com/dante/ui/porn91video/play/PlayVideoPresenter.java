package com.dante.ui.porn91video.play;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bugsnag.android.Bugsnag;
import com.bugsnag.android.Severity;
import com.dante.cookie.CookieManager;
import com.dante.custom.TastyToast;
import com.dante.data.DataManager;
import com.dante.data.model.UnLimit91PornItem;
import com.dante.data.model.User;
import com.dante.data.model.VideoComment;
import com.dante.data.model.VideoResult;
import com.dante.di.PerActivity;
import com.dante.exception.VideoException;
import com.dante.rxjava.CallBackWrapper;
import com.dante.rxjava.RetryWhenProcess;
import com.dante.rxjava.RxSchedulersHelper;
import com.dante.ui.download.DownloadPresenter;
import com.dante.ui.favorite.FavoritePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author flymegoc
 * @date 2017/11/15
 * @describe
 */
@PerActivity
public class PlayVideoPresenter extends MvpBasePresenter<PlayVideoView> implements IPlay {

    private static final String TAG = PlayVideoPresenter.class.getSimpleName();

    private FavoritePresenter favoritePresenter;
    private DownloadPresenter downloadPresenter;

    private LifecycleProvider<Lifecycle.Event> provider;

    private int start = 1;
    private DataManager dataManager;
    private CookieManager cookieManager;

    @Inject
    public PlayVideoPresenter(FavoritePresenter favoritePresenter, DownloadPresenter downloadPresenter, LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager, CookieManager cookieManager) {
        this.favoritePresenter = favoritePresenter;
        this.downloadPresenter = downloadPresenter;
        this.provider = provider;
        this.dataManager = dataManager;
        this.cookieManager = cookieManager;
    }

    @Override
    public void loadVideoUrl(final UnLimit91PornItem v9PornItem) {
        String viewKey = v9PornItem.getViewKey();
        dataManager.loadPorn91VideoUrl(viewKey)
                .map(new Function<VideoResult, VideoResult>() {
                    @Override
                    public VideoResult apply(VideoResult videoResult) throws VideoException {
                        if (TextUtils.isEmpty(videoResult.getVideoUrl())) {
                            if (VideoResult.OUT_OF_WATCH_TIMES.equals(videoResult.getId())) {
                                //尝试强行重置，并上报异常
                                cookieManager.resetPorn91VideoWatchTiem(true);
                                // Bugsnag.notify(new Throwable(TAG + "Ten videos each day address: " + dataManager.getPorn9VideoAddress()), Severity.WARNING);
                                throw new VideoException("观看次数达到上限了,请更换地址或者代理服务器！");
                            } else {
                                throw new VideoException("解析视频链接失败了");
                            }
                        }
                        return videoResult;
                    }
                })
                .retryWhen(new RetryWhenProcess(RetryWhenProcess.PROCESS_TIME))
                .compose(RxSchedulersHelper.<VideoResult>ioMainThread())
                .compose(provider.<VideoResult>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<VideoResult>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<PlayVideoView>() {
                            @Override
                            public void run(@NonNull PlayVideoView view) {
                                view.showParsingDialog();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final VideoResult videoResult) {
                        cookieManager.resetPorn91VideoWatchTiem(false);
                        ifViewAttached(new ViewAction<PlayVideoView>() {
                            @Override
                            public void run(@NonNull PlayVideoView view) {
                                view.parseVideoUrlSuccess(saveVideoUrl(videoResult, v9PornItem));
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<PlayVideoView>() {
                            @Override
                            public void run(@NonNull PlayVideoView view) {
                                view.errorParseVideoUrl(msg);
                            }
                        });
                    }
                });
    }

    @Override
    public boolean isUserLogin() {
        return dataManager.isUserLogin();
    }

    @Override
    public int getLoginUserId() {
        return dataManager.getUser().getUserId();
    }

    public void updateUnLimit91PornItem(UnLimit91PornItem v9PornItem) {
        dataManager.updateUnLimit91PornItem(v9PornItem);
    }


    @Override
    public void setFavoriteNeedRefresh(boolean favoriteNeedRefresh) {
        dataManager.setFavoriteNeedRefresh(favoriteNeedRefresh);
    }

    private UnLimit91PornItem saveVideoUrl(VideoResult videoResult, UnLimit91PornItem v9PornItem) {
        dataManager.saveVideoResult(videoResult);
        v9PornItem.setVideoResult(videoResult);
        v9PornItem.setViewHistoryDate(new Date());
        dataManager.saveUnLimit91PornItem(v9PornItem);
        return v9PornItem;
    }


    @Override
    public void downloadVideo(UnLimit91PornItem unLimit91PornItem, boolean isDownloadNeedWifi, boolean isForceReDownload) {
        downloadPresenter.downloadVideo(unLimit91PornItem, isDownloadNeedWifi, isForceReDownload, new DownloadPresenter.DownloadListener() {
            @Override
            public void onSuccess(final String message) {
                ifViewAttached(new ViewAction<PlayVideoView>() {
                    @Override
                    public void run(@NonNull PlayVideoView view) {
                        view.showMessage(message, TastyToast.SUCCESS);
                    }
                });
            }

            @Override
            public void onError(final String message) {
                ifViewAttached(new ViewAction<PlayVideoView>() {
                    @Override
                    public void run(@NonNull PlayVideoView view) {
                        view.showMessage(message, TastyToast.ERROR);
                    }
                });
            }
        });
    }

    @Override
    public void favorite(String uId, String videoId, String ownnerId) {
        favoritePresenter.favorite(uId, videoId, ownnerId, new FavoritePresenter.FavoriteListener() {
            @Override
            public void onSuccess(String message) {
                ifViewAttached(new ViewAction<PlayVideoView>() {
                    @Override
                    public void run(@NonNull PlayVideoView view) {
                        view.favoriteSuccess();
                    }
                });
            }

            @Override
            public void onError(final String message) {
                ifViewAttached(new ViewAction<PlayVideoView>() {
                    @Override
                    public void run(@NonNull PlayVideoView view) {
                        view.showError(message);
                    }
                });
            }
        });
    }



    /**
     * 是否需要为了解析uid，只有登录状态下且uid还未解析过才需要解析
     *
     * @return true
     */
    public boolean isLoadForUid() {
        User user = dataManager.getUser();
        return dataManager.isUserLogin() && user.getUserId() == 0;
    }
}