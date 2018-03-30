package com.dante.ui.splash;

import com.dante.data.model.User;
import com.dante.ui.BaseView;

/**
 * @author flymegoc
 * @date 2017/12/21
 */

public interface SplashView extends BaseView {
    void loginSuccess(User user);

    void loginError(String message);
}
