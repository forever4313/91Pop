package com.dante.ui.favorite;

/**
 * @author flymegoc
 * @date 2017/11/28
 * @describe
 */

public interface IFavorite extends IBaseFavorite {

    void loadRemoteFavoriteData(boolean pullToRefresh);

    void deleteFavorite(String rvid);

    void exportData(boolean onlyUrl);
}
