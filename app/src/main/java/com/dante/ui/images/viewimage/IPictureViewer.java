package com.dante.ui.images.viewimage;

/**
 * @author flymegoc
 * @date 2018/1/26
 */

public interface IPictureViewer {
    void listMeZiPicture(int id, boolean pullToRefresh);

    void listRosiMmPicture(int id, String imageUrl, boolean pullToRefresh);
}
