package com.dante.ui.pigav;

import com.dante.data.model.PigAv;
import com.dante.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/30
 */

public interface PigAvView extends BaseView {
    void setData(List<PigAv> pigAvList);

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<PigAv> pigAvList);
}
