package com.dante.ui.user;

import android.graphics.Bitmap;

import com.dante.data.model.User;
import com.dante.ui.BaseView;

/**
 * @author flymegoc
 * @date 2017/12/10
 */

public interface UserView extends BaseView {

    void loginSuccess(User user);

    void loginError(String message);

    void registerSuccess(User user);

    void registerFailure(String message);

    void loadCaptchaSuccess(Bitmap bitmap);

    void loadCaptchaFailure(String errorMessage, int code);
}
