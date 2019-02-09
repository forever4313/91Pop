package com.dante.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.dante.R;
import com.dante.data.DataManager;
import com.dante.data.model.User;
import com.dante.ui.MvpActivity;
import com.dante.ui.main.MainActivity;
import com.dante.ui.user.UserPresenter;
import com.dante.utils.AddressHelper;
import com.dante.utils.UserHelper;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

/**
 * @author flymegoc
 */
public class SplashActivity extends MvpActivity<SplashView, SplashPresenter> implements SplashView {

    private static final String TAG = SplashActivity.class.getSimpleName();

    public static int FULL_SCREEN_UI = View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    @Inject
    protected AddressHelper addressHelper;
    @Inject
    protected UserPresenter userPresenter;
    @Inject
    protected SplashPresenter splashPresenter;
    @Inject
    protected DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止重复开启程序造成多次登录
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //结束你的activity
            Logger.t(TAG).d("重复打开了....");
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return;
        }

        setContentView(R.layout.activity_splash);
        StatusBarUtil.setTransparentForImageViewInFragment(this,null);
        if (UserHelper.isUserInfoComplete(user)) {
            startMain();
        }
        String username = dataManager.getPorn91VideoLoginUserName();
        String password = dataManager.getPorn91VideoLoginUserPassword();

        boolean isAutoLogin = dataManager.isPorn91VideoUserAutoLogin();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && isAutoLogin && !TextUtils.isEmpty(addressHelper.getVideo91PornAddress())) {
            String captcha = UserHelper.randomCaptcha();
            login(username, password, captcha);
        } else {
            startMain();
        }
    }

    private void login(String username, String password, String captcha) {
        presenter.login(username, password, captcha);
    }

    private void startMain() {
//        SystemClock.sleep(3000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        getActivityComponent().inject(this);
        return splashPresenter;
    }

    @Override
    public void loginSuccess(User user) {
        user.copyProperties(this.user);
        startMain();
    }

    @Override
    public void loginError(String message) {
        startMain();
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }
}
