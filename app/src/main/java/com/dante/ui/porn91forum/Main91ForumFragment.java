package com.dante.ui.porn91forum;


import android.support.v4.app.Fragment;

import com.dante.data.model.Category;
import com.dante.ui.basemain.BaseMainFragment;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author flymegoc
 */
public class Main91ForumFragment extends BaseMainFragment {


    private static final String TAG = Main91ForumFragment.class.getSimpleName();

    public static Main91ForumFragment getInstance() {
        return new Main91ForumFragment();
    }

    @Override
    public int getCategoryType() {
        return Category.TYPE_91PORN_FORUM;
    }
}
