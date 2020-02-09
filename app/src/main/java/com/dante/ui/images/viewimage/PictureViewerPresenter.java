package com.dante.ui.images.viewimage;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.dante.data.DataManager;
import com.dante.rxjava.CallBackWrapper;
import com.dante.rxjava.RxSchedulersHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author flymegoc
 * @date 2018/1/26
 */

public class PictureViewerPresenter extends MvpBasePresenter<PictureViewerView> implements IPictureViewer {

    private LifecycleProvider<Lifecycle.Event> provider;
    private DataManager dataManager;

    @Inject
    public PictureViewerPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        this.provider = provider;
        this.dataManager = dataManager;
    }

    @Override
    public void listMeZiPicture(int id, boolean pullToRefresh) {
        dataManager.meiZiTuImageList(id, pullToRefresh)
                .compose(RxSchedulersHelper.<List<String>>ioMainThread())
                .compose(provider.<List<String>>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<String>>() {

                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<PictureViewerView>() {
                            @Override
                            public void run(@NonNull PictureViewerView view) {
                                view.showLoading(true);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<String> strings) {
                        ifViewAttached(new ViewAction<PictureViewerView>() {
                            @Override
                            public void run(@NonNull PictureViewerView view) {
                                view.setData(strings);
                                view.showContent();
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<PictureViewerView>() {
                            @Override
                            public void run(@NonNull PictureViewerView view) {
                                view.showError(msg);
                            }
                        });
                    }
                });
    }

    @Override
    public void listRosiMmPicture(final int id, final String imageUrl, boolean pullToRefresh) {
        dataManager.mmRosiImageList(id, imageUrl, pullToRefresh)
                .compose(RxSchedulersHelper.<List<String>>ioMainThread())
                .compose(provider.<List<String>>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<String>>() {

                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<PictureViewerView>() {
                            @Override
                            public void run(@NonNull PictureViewerView view) {
                                view.showLoading(true);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<String> strings) {
                        ifViewAttached(new ViewAction<PictureViewerView>() {
                            @Override
                            public void run(@NonNull PictureViewerView view) {
                                view.setData(strings);
                                view.showContent();
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<PictureViewerView>() {
                            @Override
                            public void run(@NonNull PictureViewerView view) {
                                view.showError(msg);
                            }
                        });
                    }
                });

    }
}
