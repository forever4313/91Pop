package com.dante.ui.user;

import android.arch.lifecycle.Lifecycle;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.dante.data.DataManager;
import com.dante.data.model.User;
import com.dante.rxjava.CallBackWrapper;
import com.dante.rxjava.RxSchedulersHelper;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * 用户登录
 *
 * @author flymegoc
 * @date 2017/12/10
 */

public class UserPresenter extends MvpBasePresenter<UserView> implements IUser {

    private LifecycleProvider<Lifecycle.Event> provider;
    private DataManager dataManager;

    @Inject
    public UserPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        this.provider = provider;
        this.dataManager = dataManager;
    }

    @Override
    public void login(String username, String password, String captcha) {
        login(username, password, captcha, null);
    }

    public void login(String username, String password, String captcha, final LoginListener loginListener) {
        dataManager.userLoginPorn91Video(username, password, captcha)
                .compose(RxSchedulersHelper.<User>ioMainThread())
                .compose(provider.<User>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<User>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<UserView>() {
                            @Override
                            public void run(@NonNull UserView view) {
                                if (loginListener == null) {
                                    view.showLoading(true);
                                }
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final User user) {
                        user.copyProperties(dataManager.getUser());
                        if (loginListener != null) {
                            loginListener.loginSuccess(user);
                        } else {
                            ifViewAttached(new ViewAction<UserView>() {
                                @Override
                                public void run(@NonNull UserView view) {
                                    view.showContent();
                                    view.loginSuccess(user);
                                }
                            });
                        }

                    }

                    @Override
                    public void onError(final String msg, int code) {
                        if (loginListener != null) {
                            loginListener.loginFailure(msg);
                        } else {
                            ifViewAttached(new ViewAction<UserView>() {
                                @Override
                                public void run(@NonNull UserView view) {
                                    view.showContent();
                                    view.loginError(msg);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void register(String username, String password1, String password2, String email, String captchaInput) {
        dataManager.userRegisterPorn91Video(username, password1, password2, email, captchaInput)
                .compose(RxSchedulersHelper.<User>ioMainThread())
                .compose(provider.<User>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<User>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<UserView>() {
                            @Override
                            public void run(@NonNull UserView view) {
                                view.showLoading(true);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final User user) {
                        ifViewAttached(new ViewAction<UserView>() {
                            @Override
                            public void run(@NonNull UserView view) {
                                view.showContent();
                                view.registerSuccess(user);
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<UserView>() {
                            @Override
                            public void run(@NonNull UserView view) {
                                view.showContent();
                                view.registerFailure(msg);
                            }
                        });
                    }
                });
    }


    @Override
    public void loadCaptcha() {
        dataManager.porn9VideoLoginCaptcha()
                .compose(RxSchedulersHelper.<Bitmap>ioMainThread())
                .compose(provider.<Bitmap>bindUntilEvent(Lifecycle.Event.ON_STOP))
                .subscribe(new CallBackWrapper<Bitmap>() {
                    @Override
                    public void onSuccess(final Bitmap bitmap) {
                        ifViewAttached(new ViewAction<UserView>() {
                            @Override
                            public void run(@NonNull UserView view) {
                                view.loadCaptchaSuccess(bitmap);
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, final int code) {
                        ifViewAttached(new ViewAction<UserView>() {
                            @Override
                            public void run(@NonNull UserView view) {
                                view.loadCaptchaFailure(msg, code);
                            }
                        });
                    }
                });
    }
    public interface LoginListener {
        void loginSuccess(User user);

        void loginFailure(String message);
    }
}
