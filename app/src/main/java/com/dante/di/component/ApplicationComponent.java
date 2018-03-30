package com.dante.di.component;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;
import com.dante.MyApplication;
import com.dante.cookie.CookieManager;
import com.dante.data.DataManager;
import com.dante.data.model.User;
import com.dante.di.ApplicationContext;
import com.dante.di.module.ApiServiceModule;
import com.dante.di.module.ApplicationModule;
import com.dante.utils.AddressHelper;
import com.dante.utils.DownloadManager;
import com.dante.utils.MyProxySelector;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author flymegoc
 * @date 2018/2/4
 */
@Singleton
@Component(modules = {ApplicationModule.class, ApiServiceModule.class})
public interface ApplicationComponent {
    void inject(MyApplication myApplication);

    void inject(DownloadManager downloadManager);

    @ApplicationContext
    Context getContext();

    HttpProxyCacheServer getHttpProxyCacheServer();

    User getUser();

    AddressHelper getAddressHelper();

    MyProxySelector getMyProxySelector();

    Gson getGson();

    DataManager getDataManager();

    CookieManager getCookieManager();
}
