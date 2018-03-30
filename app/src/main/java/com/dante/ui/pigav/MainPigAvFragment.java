package com.dante.ui.pigav;

import com.dante.data.model.Category;
import com.dante.ui.basemain.BaseMainFragment;

/**
 * @author flymegoc
 * @date 2018/1/29
 */

public class MainPigAvFragment extends BaseMainFragment {

    public static MainPigAvFragment getInstance() {
        return new MainPigAvFragment();
    }

    @Override
    public int getCategoryType() {
        return Category.TYPE_PIG_AV;
    }

    @Override
    public boolean isNeedDestroy() {
        return true;
    }
}
