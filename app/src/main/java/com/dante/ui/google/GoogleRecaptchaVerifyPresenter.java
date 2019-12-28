package com.dante.ui.google;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.dante.data.AppDataManager;
import com.dante.rxjava.CallBackWrapper;
import com.dante.rxjava.RetryWhenProcess;
import com.dante.rxjava.RxSchedulersHelper;
import com.dante.ui.MvpBasePresenter;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class GoogleRecaptchaVerifyPresenter  extends MvpBasePresenter<GoogleRecaptchaVerifyView> implements IGoogleRecaptchaVerify {

    private final static String TAG = GoogleRecaptchaVerifyPresenter.class.getSimpleName();

    private final AppDataManager dataManager;
    private final LifecycleProvider<Lifecycle.Event> provider;

    @Inject
    public GoogleRecaptchaVerifyPresenter( LifecycleProvider<Lifecycle.Event> provider,AppDataManager dataManager) {
        super(provider,dataManager);
        this.dataManager = dataManager;
        this.provider = provider;
    }

    @Override
    public void testV9Porn() {
        this.dataManager.testV9Porn(dataManager.getPorn91VideoAddress())
                .retryWhen(new RetryWhenProcess(RetryWhenProcess.PROCESS_TIME))
                .map(new Function<Response<ResponseBody>, String>() {
                    @Override
                    public String apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                        if (responseBodyResponse.isSuccessful()) {
                            Logger.t(TAG).d("加载成功，无需验证");
                            return "OK";
                        } else {
                            Logger.t(TAG).d("加载失败，需要验证");
                            if (responseBodyResponse.errorBody() != null) {
                                return injectJs(responseBodyResponse.errorBody().string());
                            }
                        }
                        return "";
                    }
                })
                .compose(RxSchedulersHelper.<String>ioMainThread())
                .compose(provider.<String>bindToLifecycle())
                .subscribe(new CallBackWrapper<String>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<GoogleRecaptchaVerifyView>() {
                            @Override
                            public void run(@NonNull GoogleRecaptchaVerifyView view) {
                                view.startCheckNeedVerify();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final String responseBody) {
                        if ("OK".equals(responseBody)) {
                            ifViewAttached(new ViewAction<GoogleRecaptchaVerifyView>() {
                                @Override
                                public void run(@NonNull GoogleRecaptchaVerifyView view) {
                                    view.noNeedVerifyRecaptcha();
                                }
                            });
                        } else {
                            if (TextUtils.isEmpty(responseBody)) {
                                ifViewAttached(new ViewAction<GoogleRecaptchaVerifyView>() {
                                    @Override
                                    public void run(@NonNull GoogleRecaptchaVerifyView view) {
                                        view.loadPageDataFailure();
                                    }
                                });
                            } else {
                                ifViewAttached(new ViewAction<GoogleRecaptchaVerifyView>() {
                                    @Override
                                    public void run(@NonNull GoogleRecaptchaVerifyView view) {
                                        view.needVerifyRecaptcha(responseBody);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        Logger.t(TAG).d("加载失败，网络异常");
                        ifViewAttached(new ViewAction<GoogleRecaptchaVerifyView>() {
                            @Override
                            public void run(@NonNull GoogleRecaptchaVerifyView view) {
                                view.loadPageDataFailure();
                            }
                        });
                    }
                });
    }

    private String injectJs(String oldHtml) {
        if (TextUtils.isEmpty(oldHtml)) {
            return "";
        }
        Document doc = Jsoup.parse(oldHtml);
        doc.head().append("<script type=\"text/javascript\">\n" +
                "        function getPostData() {\n" +
                "            let recaptcha = document.getElementById(\"g-recaptcha-response\").value;\n" +
                "            if (!recaptcha || recaptcha === '') {\n" +
                "                recaptcha = document.getElementById(\"g-recaptcha-response\").innerHTML;\n" +
                "            }\n" +
                "            const action = document.getElementById('challenge-form').getAttribute(\"action\");\n" +
                "            const r = document.getElementsByName(\"r\")[0].getAttribute(\"value\");\n" +
                "            const id = document.getElementById('id').getAttribute(\"value\");\n" +
                "            return action + \",\" + r + \",\" + id + \",\" + recaptcha;\n" +
                "        }\n" +
                "    </script>");

        String html = doc.outerHtml();
        Log.d(TAG, "JS注入完成");
        return html;
    }

    @Override
    public void verifyGoogleRecaptcha(String action, String r, String id, String recaptcha) {
        dataManager.verifyGoogleRecaptcha(action, r, id, recaptcha)

                .retryWhen(new RetryWhenProcess(RetryWhenProcess.PROCESS_TIME))
                .map(new Function<Response<ResponseBody>, Response<ResponseBody>>() {
                    @Override
                    public Response<ResponseBody> apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                        return responseBodyResponse;
                    }
                })
                .observeOn(Schedulers.io())
//                .compose(RxSchedulersHelper.ioMainThread())
//                .compose(provider.bindToLifecycle())
                .subscribe(new CallBackWrapper<Response<ResponseBody>>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<GoogleRecaptchaVerifyView>() {
                            @Override
                            public void run(@NonNull GoogleRecaptchaVerifyView view) {
                                view.startVerifyRecaptcha();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Response<ResponseBody> responseBodyResponse) {
                        if (responseBodyResponse.isSuccessful()) {
                            Log.d(TAG, "验证成功了");
                            ifViewAttached(new ViewAction<GoogleRecaptchaVerifyView>() {
                                @Override
                                public void run(@NonNull GoogleRecaptchaVerifyView view) {
                                    view.verifyRecaptchaSuccess();
                                }
                            });
                        } else {
                            Log.d(TAG, "验证失败了");
                            ifViewAttached(new ViewAction<GoogleRecaptchaVerifyView>() {
                                @Override
                                public void run(@NonNull GoogleRecaptchaVerifyView view) {
                                    view.verifyRecaptchaFailure();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        Log.d(TAG, "访问网络失败");
                        ifViewAttached(new ViewAction<GoogleRecaptchaVerifyView>() {
                            @Override
                            public void run(@NonNull GoogleRecaptchaVerifyView view) {
                                view.verifyRecaptchaFailure();
                            }
                        });
                    }
                });
    }

    @Override
    public String getBaseAddress() {
        return dataManager.getPorn91VideoAddress();
    }
}