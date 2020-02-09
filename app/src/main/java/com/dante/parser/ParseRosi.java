package com.dante.parser;

import android.text.TextUtils;

import com.dante.data.network.Api;
import com.orhanobut.logger.Logger;
import com.dante.data.model.BaseResult;
import com.dante.data.model.MmRosi;
import com.dante.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author flymegoc
 * @date 2018/2/1
 */

public class ParseRosi {
    private static final String TAG = ParseRosi.class.getSimpleName();

    public static BaseResult<List<MmRosi>> parseRosiMmList(String html, int page) {
        BaseResult<List<MmRosi>> baseResult = new BaseResult<>();
        baseResult.setTotalPage(1);
        Logger.t(TAG).d(html);
        Document doc = Jsoup.parse(html);
        Element ul = doc.getElementById("container");
        Elements lis = ul.select("div.post-home");
        List<MmRosi> mmRosiList = new ArrayList<>();
        for (Element li : lis) {
            MmRosi mmRosi = new MmRosi();
            Element a = li.selectFirst("a");
            String contentUrl = a.attr("href");
            mmRosi.setContentUrl(contentUrl);

            int startIndex = contentUrl.lastIndexOf("/rosi-");
            int endIndex = contentUrl.lastIndexOf(".");
            String idStr = StringUtils.subString(contentUrl, startIndex + 6, endIndex);

            if (!TextUtils.isEmpty(idStr) && TextUtils.isDigitsOnly(idStr)) {
                mmRosi.setId(Integer.parseInt(idStr));
            } else {
                Logger.t(TAG).d(idStr);
            }

            Element img = a.selectFirst("img");
            String title = a.attr("title");
            mmRosi.setTitle(title);
            String imgUrl = img.attr("src");
            mmRosi.setImgUrl(imgUrl);
//            int imgWidth = Integer.parseInt(img.attr("width"));
            mmRosi.setImgWidth(285);

            mmRosiList.add(mmRosi);
        }

        if (page == 1) {
            Elements pageElements = doc.getElementById("pagenavi").select("li");
            if(pageElements.size()>=3){
                Element ele = pageElements.get(pageElements.size() - 2);
                int totalPage = Integer.parseInt(ele.selectFirst("a").html());
                baseResult.setTotalPage(totalPage);
            }

        }

        baseResult.setData(mmRosiList);
        return baseResult;
    }

    public static List<String> parseRosiMmAlbumList(String html) {
        List<String> list = new ArrayList<>();
        Logger.t(TAG).d(html);
        Document doc = Jsoup.parse(html);
        Element ul = doc.getElementById("single-content");
        Element picsContainer = ul.selectFirst("div.post-content");
        Element first = picsContainer.selectFirst("a");
        String firstHref = first.attr("href");
        list.add(firstHref);
        Elements lis = picsContainer.select("span");
        for (Element li : lis) {
            String imgUrl = li.selectFirst("a").attr("href");

            list.add(imgUrl);
        }
        return list;
    }
}
