package com.dante.ui.images;


import android.support.v4.app.Fragment;

import com.dante.data.model.Category;
import com.dante.ui.basemain.BaseMainFragment;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author flymegoc
 */
public class MainMeiZiTuFragment extends BaseMainFragment {

    public static MainMeiZiTuFragment getInstance() {
        return new MainMeiZiTuFragment();
    }

    @Override
    public int getCategoryType() {
        return Category.TYPE_MEI_ZI_TU;
    }

    @Override
    public boolean isNeedDestroy() {
        return true;
    }
}
