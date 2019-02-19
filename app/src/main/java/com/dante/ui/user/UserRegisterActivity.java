package com.dante.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.devbrackets.android.exomedia.util.ResourceUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.dante.custom.TastyToast;
import com.dante.R;
import com.dante.cookie.CookieManager;
import com.dante.data.DataManager;
import com.dante.data.model.User;
import com.dante.ui.MvpActivity;
import com.dante.ui.main.MainActivity;
import com.dante.utils.AddressHelper;
import com.dante.utils.DialogUtils;
import com.dante.utils.GlideApp;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author flymegoc
 */
public class UserRegisterActivity extends MvpActivity<UserView, UserPresenter> implements UserView {
    private static final String TAG = UserRegisterActivity.class.getSimpleName();
    @Inject
    protected AddressHelper addressHelper;
    @Inject
    protected UserPresenter userPresenter;
    @Inject
    protected CookieManager cookieManager;
    @Inject
    protected DataManager dataManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password_one)
    EditText etPasswordOne;
    @BindView(R.id.et_password_two)
    EditText etPasswordTwo;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    @BindView(R.id.wb_captcha)
    ImageView wbCaptcha;
    @BindView(R.id.bt_user_signup)
    Button btUserSignup;
    private AlertDialog alertDialog;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        ButterKnife.bind(this);
        initToolBar(toolbar);
        loadCaptcha();

        btUserSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etAccount.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String passwordOne = etPasswordOne.getText().toString().trim();
                String passwordTwo = etPasswordTwo.getText().toString().trim();
                String captcha = etCaptcha.getText().toString().trim();
                password = passwordOne;
                register(username, email, passwordOne, passwordTwo, captcha);
            }
        });
        wbCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCaptcha();
            }
        });
        alertDialog = DialogUtils.initLodingDialog(this, "注册中，请稍后...");

        cookieManager.cleanAllCookies();
    }

    /**
     * 跳转主界面
     */
    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityWithAnimotion(intent);
        finish();
    }

    private void register(String username, String email, String passwordOne, String passwordTwo, String captcha) {
        if (TextUtils.isEmpty(username)) {
            showMessage("用户名不能为空", TastyToast.INFO);
            return;
        }
        //服务器根本不会验证邮箱格式，貌似只要有@符号和.就可以通过注册了,不过如果后期验证邮箱....
        if (TextUtils.isEmpty(email)) {
            showMessage("邮箱不能为空", TastyToast.INFO);
            return;
        }
        if (TextUtils.isEmpty(passwordOne)) {
            showMessage("密码不能为空", TastyToast.INFO);
            return;
        }
        if (TextUtils.isEmpty(passwordTwo)) {
            showMessage("确认密码不能为空", TastyToast.INFO);
            return;
        }
        if (TextUtils.isEmpty(captcha)) {
            showMessage("验证码不能为空", TastyToast.INFO);
            return;
        }
        if (!passwordOne.equals(passwordTwo)) {
            showMessage("密码不一致，请检查", TastyToast.WARNING);
            return;
        }
        QMUIKeyboardHelper.hideKeyboard(getCurrentFocus());
        presenter.register(username, passwordOne, passwordTwo, email, captcha);
    }

    @NonNull
    @Override
    public UserPresenter createPresenter() {
        getActivityComponent().inject(this);
        return userPresenter;
    }

    /**
     * 加载验证码，目前似乎是非必须，不填也是可以登录的
     */
    private void loadCaptcha() {
        presenter.loadCaptcha();
    }

    @Override
    public void loginSuccess(User user) {
        user.copyProperties(this.user);
    }

    @Override
    public void loginError(String message) {

    }

    @Override
    public void registerSuccess(User user) {
        user.copyProperties(this.user);
        saveUserInfoPrf(username, password);
        startMain();
        showMessage("注册成功", TastyToast.SUCCESS);
    }

    @Override
    public void registerFailure(String message) {
        showMessage("注册失败", TastyToast.ERROR);

    }

    /**
     * 注册成功，默认保存用户名和密码
     */
    private void saveUserInfoPrf(String username, String password) {
        dataManager.setPorn91VideoLoginUserName(username);
        //记住密码
        dataManager.setPorn91VideoLoginUserPassWord(password);
    }

    @Override
    public void loadCaptchaSuccess(Bitmap bitmap) {
        wbCaptcha.setImageBitmap(bitmap);
    }

    @Override
    public void loadCaptchaFailure(String errorMessage, int code) {
        wbCaptcha.setImageDrawable(ResourceUtil.getDrawable(this, R.drawable.ic_refresh));
        showError("无法加载验证码,点击刷新重试");
    }


    @Override
    public void showError(String message) {

    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        if (alertDialog == null || isFinishing()) {
            return;
        }
        alertDialog.show();
    }

    @Override
    public void showContent() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }

}
