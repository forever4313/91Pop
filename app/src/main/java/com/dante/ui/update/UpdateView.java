package com.dante.ui.update;

import com.dante.data.model.UpdateVersion;
import com.dante.ui.BaseView;

/**
 * @author flymegoc
 * @date 2017/12/22
 */

public interface UpdateView extends BaseView {
    void needUpdate(UpdateVersion updateVersion);

    void noNeedUpdate();

    void checkUpdateError(String message);
}
