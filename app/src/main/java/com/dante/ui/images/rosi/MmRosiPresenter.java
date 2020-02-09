package com.dante.ui.images.rosi;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.dante.data.DataManager;
import com.dante.data.model.BaseResult;
import com.dante.data.model.MmRosi;
import com.dante.rxjava.CallBackWrapper;
import com.dante.rxjava.RxSchedulersHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author flymegoc
 * @date 2018/2/1
 */

public class MmRosiPresenter extends MvpBasePresenter<MmRosiView> implements IMmRosi {

    private int page = 1;
    private int totalPage = 1;
    private LifecycleProvider<Lifecycle.Event> provider;
    private DataManager dataManager;

    @Inject
    public MmRosiPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        this.provider = provider;
        this.dataManager = dataManager;
    }

    @Override
    public void loadData(String category, final boolean pullToRefresh, boolean cleanCache) {
        if (pullToRefresh) {
            page = 1;
            totalPage = 1;
        }
        dataManager.listMmRosi(category, page, cleanCache)
                .map(new Function<BaseResult<List<MmRosi>>, List<MmRosi>>() {
                    @Override
                    public List<MmRosi> apply(BaseResult<List<MmRosi>> baseResult) throws Exception {
                        if (page == 1) {
                            totalPage = baseResult.getTotalPage();
                        }
                        return baseResult.getData();
                    }
                })
                .compose(RxSchedulersHelper.<List<MmRosi>>ioMainThread())
                .compose(provider.<List<MmRosi>>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<MmRosi>>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<MmRosiView>() {
                            @Override
                            public void run(@NonNull MmRosiView view) {
                                view.showLoading(pullToRefresh);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<MmRosi> mmRosis) {
                        ifViewAttached(new ViewAction<MmRosiView>() {
                            @Override
                            public void run(@NonNull MmRosiView view) {
                                if (page == 1) {
                                    view.setData(mmRosis);
                                    view.showContent();
                                } else {
                                    view.setMoreData(mmRosis);
                                }
                                //已经最后一页了
                                if (page >= totalPage) {
                                    view.noMoreData();
                                } else {
                                    page++;
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<MmRosiView>() {
                            @Override
                            public void run(@NonNull MmRosiView view) {
                                view.showError(msg);
                            }
                        });
                    }
                });
    }
}
