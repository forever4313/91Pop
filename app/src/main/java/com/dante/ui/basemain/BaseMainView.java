package com.dante.ui.basemain;

import com.dante.data.model.Category;
import com.dante.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/25
 */

public interface BaseMainView extends BaseView {
    void onLoadCategoryData(List<Category> categoryList);

    void onLoadAllCategoryData(List<Category> categoryList);
}
