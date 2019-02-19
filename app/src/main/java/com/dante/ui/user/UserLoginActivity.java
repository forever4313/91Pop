package com.dante.ui.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.devbrackets.android.exomedia.util.ResourceUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.dante.custom.TastyToast;
import com.dante.R;
import com.dante.data.DataManager;
import com.dante.data.model.User;
import com.dante.ui.MvpActivity;
import com.dante.ui.favorite.FavoriteActivity;
import com.dante.ui.porn91video.search.SearchActivity;
import com.dante.ui.setting.SettingActivity;
import com.dante.utils.AddressHelper;
import com.dante.utils.DialogUtils;
import com.dante.utils.GlideApp;
import com.dante.utils.constants.Keys;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dante.utils.constants.KeysActivityRequestResultCode.LOGIN_ACTION_FOR_GET_UID;
import static com.dante.utils.constants.KeysActivityRequestResultCode.LOGIN_ACTION_FOR_LOOK_AUTHOR_VIDEO;
import static com.dante.utils.constants.KeysActivityRequestResultCode.RESULT_CODE_FOR_REFRESH_GET_UID;
import static com.dante.utils.constants.KeysActivityRequestResultCode.RESULT_FOR_LOOK_AUTHOR_VIDEO;

/**
 * @author flymegoc
 */
public class UserLoginActivity extends MvpActivity<UserView, UserPresenter> implements UserView {

    public static final int LOGIN_ACTION_FOR_LOOK_MY_FAVORITE = 1;
    public static final int LOGIN_ACTION_FOR_SEARCH_91PRON_VIDEO = 2;

    private static final String TAG = UserLoginActivity.class.getSimpleName();
    @Inject
    protected AddressHelper addressHelper;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    @BindView(R.id.wb_captcha)
    ImageView simpleDraweeView;
    @BindView(R.id.bt_user_login)
    Button btUserLogin;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cb_remenber_password)
    CheckBox cbRemenberPassword;
    @BindView(R.id.cb_auto_login)
    CheckBox cbAutoLogin;
    @Inject
    UserPresenter userPresenter;
    @Inject
    DataManager dataManager;
    private AlertDialog alertDialog;
    private String username;
    private String password;
    private int loginForAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ButterKnife.bind(this);
        initToolBar(toolbar);
        loginForAction = getIntent().getIntExtra(Keys.KEY_INTENT_LOGIN_FOR_ACTION, 0);
        if (!TextUtils.isEmpty(addressHelper.getVideo91PornAddress())) {
            loadCaptcha();
        }
        btUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etAccount.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                String captcha = etCaptcha.getText().toString().trim();
                login(username, password, captcha);
            }
        });
        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCaptcha();
            }
        });

        cbAutoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAutoLogin.isChecked()) {
                    cbAutoLogin.setChecked(true);
                    cbRemenberPassword.setChecked(true);
                } else {
                    cbAutoLogin.setChecked(false);
                }
            }
        });

        cbRemenberPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRemenberPassword.isChecked()) {
                    cbRemenberPassword.setChecked(true);
                } else {
                    cbRemenberPassword.setChecked(false);
                    cbAutoLogin.setChecked(false);
                }
            }
        });

        alertDialog = DialogUtils.initLodingDialog(this, "登录中，请稍后...");
        setUpUserInfo();
    }

    private void setUpUserInfo() {
        username = dataManager.getPorn91VideoLoginUserName();
        password = dataManager.getPorn91VideoLoginUserPassword();
        if (!TextUtils.isEmpty(password)) {
            cbRemenberPassword.setChecked(true);
        }
        boolean isAutoLogin = dataManager.isPorn91VideoUserAutoLogin();
        cbAutoLogin.setChecked(isAutoLogin);

        etAccount.setText(username);
        etPassword.setText(password);
    }

    private void login(String username, String password, String captcha) {
        if (TextUtils.isEmpty(username)) {
            showMessage("请填写用户名", TastyToast.INFO);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showMessage("请填写密码", TastyToast.INFO);
            return;
        }
        if (TextUtils.isEmpty(captcha)) {
            showMessage("请填写验证码", TastyToast.INFO);
            return;
        }
        QMUIKeyboardHelper.hideKeyboard(getCurrentFocus());
        presenter.login(username, password, captcha);
        saveUserInfoPrf(username, password);

    }

    /**
     * 加载验证码，目前似乎是非必须，不填也是可以登录的
     */
    private void loadCaptcha() {
        presenter.loadCaptcha();

    }


    @NonNull
    @Override
    public UserPresenter createPresenter() {
        getActivityComponent().inject(this);

        if (TextUtils.isEmpty(addressHelper.getVideo91PornAddress())) {
            showNeedSetAddressFirstDialog();
        }
        return userPresenter;
    }

    private void showNeedSetAddressFirstDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("温馨提示");
        builder.setMessage("还未设置91porn视频地址,无法登录或注册，现在去设置？");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, SettingActivity.class);
                startActivityWithAnimotion(intent);
                finish();
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void loginSuccess(User user) {
        user.copyProperties(this.user);
        showMessage("登录成功", TastyToast.SUCCESS);
        switch (loginForAction) {
            case LOGIN_ACTION_FOR_LOOK_MY_FAVORITE:
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivityWithAnimotion(intent);
                finish();
                break;
            case LOGIN_ACTION_FOR_SEARCH_91PRON_VIDEO:
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivityWithAnimotion(intentSearch);
                finish();
                break;
            case LOGIN_ACTION_FOR_GET_UID:
                setResult(RESULT_CODE_FOR_REFRESH_GET_UID);
                onBackPressed();
                break;
            case LOGIN_ACTION_FOR_LOOK_AUTHOR_VIDEO:
                setResult(RESULT_FOR_LOOK_AUTHOR_VIDEO);
                onBackPressed();
                break;
            default:
                setResult(RESULT_OK);
                onBackPressed();
        }

    }

    private void saveUserInfoPrf(String username, String password) {
        dataManager.setPorn91VideoLoginUserName(username);
        //记住密码
        if (cbRemenberPassword.isChecked()) {
            dataManager.setPorn91VideoLoginUserPassWord(password);
        } else {
            dataManager.setPorn91VideoLoginUserPassWord("");
        }
        //自动登录
        if (cbAutoLogin.isChecked()) {
            dataManager.setPorn91VideoUserAutoLogin(true);
        } else {
            dataManager.setPorn91VideoUserAutoLogin(false);
        }
    }

    @Override
    public void loginError(String message) {
        showMessage(message, TastyToast.ERROR);
    }

    @Override
    public void registerSuccess(User user) {
        user.copyProperties(this.user);
    }

    @Override
    public void registerFailure(String message) {

    }

    @Override
    public void loadCaptchaSuccess(Bitmap bitmap) {
        simpleDraweeView.setImageBitmap(bitmap);
    }

    @Override
    public void loadCaptchaFailure(String errorMessage, int code) {
        simpleDraweeView.setImageDrawable(ResourceUtil.getDrawable(this, R.drawable.ic_refresh));
        showError("无法加载验证码,点击刷新重试");
    }


    @Override
    public void showError(String message) {

    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        if (alertDialog == null) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user_register) {
            Intent intent = new Intent(this, UserRegisterActivity.class);
            startActivityWithAnimotion(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
