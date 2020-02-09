package com.dante.ui.images.rosi;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.dante.custom.TastyToast;
import com.dante.R;
import com.dante.adapter.MmRosiAdapter;
import com.dante.data.model.MmRosi;
import com.dante.ui.MvpFragment;
import com.dante.ui.images.viewimage.PictureViewerActivity;
import com.dante.utils.AppUtils;
import com.dante.utils.constants.Keys;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author flymegoc
 */
public class MmRosiFragment extends MvpFragment<MmRosiView, MmRosiPresenter> implements MmRosiView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    protected MmRosiPresenter mmRosiPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    Unbinder unbinder;
    private MmRosiAdapter mmRosiAdapter;

    public MmRosiFragment() {
        // Required empty public constructor
    }

    public static MmRosiFragment getInstance() {
        return new MmRosiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mmRosiAdapter = new MmRosiAdapter(R.layout.item_rosi);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_rosi, container, false);
    }

    @NonNull
    @Override
    public MmRosiPresenter createPresenter() {
        getActivityComponent().inject(this);

        return mmRosiPresenter;
    }

    @Override
    protected void onLazyLoadOnce() {
        super.onLazyLoadOnce();
        loadData(true, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        swipeLayout.setOnRefreshListener(this);
        AppUtils.setColorSchemeColors(context, swipeLayout);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mmRosiAdapter);
        mmRosiAdapter.setWidth(QMUIDisplayHelper.getScreenWidth(context) / 2);
        mmRosiAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadData(false, false);
            }
        });
        mmRosiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MmRosi mmRosi = (MmRosi) adapter.getItem(position);
                if (mmRosi == null) {
                    return;
                }
                Intent intent = new Intent(context, PictureViewerActivity.class);
                intent.putExtra(Keys.KEY_INTENT_99_MM_ITEM, mmRosi);
                startActivityWithAnimotion(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        swipeLayout.setRefreshing(pullToRefresh);
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
        swipeLayout.setRefreshing(false);
        showMessage(message, TastyToast.ERROR);
    }

    @Override
    public void loadMoreFailed() {
        mmRosiAdapter.loadMoreFail();
    }

    @Override
    public void noMoreData() {
        mmRosiAdapter.loadMoreEnd(true);
    }

    @Override
    public void setMoreData(List<MmRosi> mmRosiList) {
        mmRosiAdapter.loadMoreComplete();
        mmRosiAdapter.addData(mmRosiList);
    }

    @Override
    public void loadData(boolean pullToRefresh, boolean cleanCache) {
        presenter.loadData(category.getCategoryValue(), pullToRefresh, cleanCache);
    }

    @Override
    public void setData(List<MmRosi> data) {
        mmRosiAdapter.setNewData(data);
    }

    @Override
    public void onRefresh() {
        loadData(true, true);
    }
}
