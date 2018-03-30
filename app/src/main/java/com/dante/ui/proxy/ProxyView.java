package com.dante.ui.proxy;

import com.dante.data.model.ProxyModel;
import com.dante.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/20
 */

public interface ProxyView extends BaseView {
    void testProxySuccess(String message);

    void testProxyError(String message);

    void parseXiCiDaiLiSuccess(List<ProxyModel> proxyModelList);

    void loadMoreDataComplete();

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<ProxyModel> proxyModelList);

    void beginParseProxy();
}
