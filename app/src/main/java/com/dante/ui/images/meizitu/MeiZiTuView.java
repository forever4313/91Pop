package com.dante.ui.images.meizitu;

import com.dante.data.model.MeiZiTu;
import com.dante.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/25
 */

public interface MeiZiTuView extends BaseView {
    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<MeiZiTu> meiZiTuList);

    void loadData(boolean pullToRefresh, boolean cleanCache);

    void setData(List<MeiZiTu> meiZiTuList);
}
