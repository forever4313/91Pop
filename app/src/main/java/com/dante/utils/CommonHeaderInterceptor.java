package com.dante.utils;

import android.support.annotation.NonNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author flymegoc
 * @date 2018/1/17
 */

@Singleton
public class CommonHeaderInterceptor implements Interceptor {

    @Inject
    public CommonHeaderInterceptor() {
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //统一设置请求头
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder();
        requestBuilder.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299");
        requestBuilder.header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5");
        requestBuilder.header("Proxy-Connection", "keep-alive");
        requestBuilder.header("Cache-Control", "max-age=0");
        requestBuilder.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        requestBuilder.addHeader("Cookie","__cfduid=d28873cf4077504fad18bd2c6fab6a2251576594563; CLIPSHARE=o69vscqrr9gkemkto3tb714dg1; __utmc=50351329; __utmz=50351329.1576594565.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __dtsu=1EE7044586ECF85D602F82BF02BA6821; __utma=50351329.1205266433.1576594565.1576853435.1576861109.5; __utmb=50351329.0.10.1576861109; cf_clearance=9eae89f3ce4975352ae6eaa9bca7ec254749fce5-1576862675-0-250");
        requestBuilder.method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
