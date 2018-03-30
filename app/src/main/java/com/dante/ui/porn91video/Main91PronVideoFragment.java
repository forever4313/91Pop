package com.dante.ui.porn91video;


import android.support.v4.app.Fragment;

import com.dante.data.model.Category;
import com.dante.ui.basemain.BaseMainFragment;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author flymegoc
 */
public class Main91PronVideoFragment extends BaseMainFragment {

    public static Main91PronVideoFragment getInstance() {
        return new Main91PronVideoFragment();
    }

    @Override
    public int getCategoryType() {
        return Category.TYPE_91PORN;
    }
}
