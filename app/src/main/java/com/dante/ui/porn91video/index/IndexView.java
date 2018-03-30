package com.dante.ui.porn91video.index;

import com.dante.data.model.UnLimit91PornItem;
import com.dante.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/15
 * @describe
 */

public interface IndexView extends BaseView {

    void loadData(boolean pullToRefresh, boolean cleanCache);

    void setData(List<UnLimit91PornItem> data);
}
