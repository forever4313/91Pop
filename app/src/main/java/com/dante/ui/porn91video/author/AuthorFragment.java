package com.dante.ui.porn91video.author;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dante.R;
import com.dante.adapter.UnLimit91Adapter;
import com.dante.custom.TastyToast;
import com.dante.data.model.UnLimit91PornItem;
import com.dante.ui.MvpFragment;
import com.dante.ui.porn91video.play.BasePlayVideo;
import com.dante.utils.AppUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * 作者视频
 *
 * @author megoc
 */
public class AuthorFragment extends MvpFragment<AuthorView, AuthorPresenter> implements AuthorView {

    private static final String TAG = AuthorFragment.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    Unbinder unbinder;

    private UnLimit91PornItem v9PornItem;

    private UnLimit91Adapter mV91PornAdapter;

    @Inject
    protected AuthorPresenter authorPresenter;

    @Inject
    public AuthorFragment() {
        // Required empty public constructor
        Logger.t(TAG).d("AuthorFragment初始化了.....");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mV91PornAdapter = new UnLimit91Adapter(R.layout.item_unlimit_91porn, null);
        mV91PornAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UnLimit91PornItem v9PornItems = (UnLimit91PornItem) adapter.getData().get(position);
                BasePlayVideo basePlayVideo = (BasePlayVideo) getActivity();
                if (basePlayVideo != null) {
                    basePlayVideo.setV9PornItems(v9PornItems);
                    basePlayVideo.initData();
                }
            }
        });
        mV91PornAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (canLoadAuthorVideos()) {
                    presenter.authorVideos(v9PornItem.getVideoResult().getOwnerId(), false);
                }

            }
        }, recyclerView);

    }

    public void setV9PornItem(UnLimit91PornItem v9PornItem) {
        this.v9PornItem = v9PornItem;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_author, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        AppUtils.setColorSchemeColors(getContext(), swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (canLoadAuthorVideos()) {
                    presenter.authorVideos(v9PornItem.getVideoResult().getOwnerId(), true);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mV91PornAdapter);
    }

    @Override
    protected void onLazyLoadOnce() {
        super.onLazyLoadOnce();
        loadAuthorVideos();
    }

    public void loadAuthorVideos() {
        presenter.authorVideos(v9PornItem.getVideoResult().getOwnerId(), false);

    }

    private boolean canLoadAuthorVideos() {
        return presenter.isUserLogin() && v9PornItem != null && v9PornItem.getVideoResultId() != 0;
    }

    @Override
    public String getTitle() {
        return "作者";
    }

    @NonNull
    @Override
    public AuthorPresenter createPresenter() {
        return authorPresenter;
    }

    @Override
    public void loadMoreDataComplete() {
        mV91PornAdapter.loadMoreComplete();
    }

    @Override
    public void loadMoreFailed() {
        mV91PornAdapter.loadMoreFail();
    }

    @Override
    public void noMoreData() {
        mV91PornAdapter.loadMoreEnd(true);
    }

    @Override
    public void setMoreData(List<UnLimit91PornItem> v9PornItemList) {
        mV91PornAdapter.addData(v9PornItemList);
    }

    @Override
    public void setData(List<UnLimit91PornItem> data) {
        mV91PornAdapter.setNewData(data);
        recyclerView.smoothScrollToPosition(0);
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void showContent() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }

    @Override
    public void showError(String message) {
        showMessage(message, TastyToast.ERROR);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
