package com.dante.ui.images;

import com.dante.data.model.Category;
import com.dante.ui.basemain.BaseMainFragment;

/**
 * @author flymegoc
 * @date 2018/2/1
 */

public class MainRosiFragment extends BaseMainFragment {


    public static MainRosiFragment getInstance() {
        return new MainRosiFragment();
    }

    @Override
    public int getCategoryType() {
        return Category.TYPE_ROSI;
    }

    @Override
    public boolean isNeedDestroy() {
        return true;
    }
}
