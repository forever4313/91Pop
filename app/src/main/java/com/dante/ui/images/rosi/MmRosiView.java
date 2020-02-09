package com.dante.ui.images.rosi;

import com.dante.data.model.MmRosi;
import com.dante.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/2/1
 */

public interface MmRosiView extends BaseView {
    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<MmRosi> unLimit91PornItemList);

    void loadData(boolean pullToRefresh, boolean cleanCache);

    void setData(List<MmRosi> data);
}
