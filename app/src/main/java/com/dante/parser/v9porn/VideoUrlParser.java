package com.dante.parser.v9porn;

import android.text.TextUtils;
import android.util.Base64;

import com.dante.data.model.User;
import com.dante.data.model.VideoResult;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class VideoUrlParser extends BaseVideoPlayUrlParser implements VideoPlayUrlParser {

    @Inject
    public VideoUrlParser() {

    }

    @Override
    public VideoResult parseVideoPlayUrl(String html, User user) {
        VideoResult videoResult = new VideoResult();
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("player_one");
        String videoUrl = null;
        if(element == null){
            videoUrl = element.selectFirst("source").attr("src");
        }
        if(videoUrl == null){
            Document doc = Jsoup.parse(html);
            // 先直接取source
            try {
                videoUrl = doc.select("video").first().select("source").first().attr("src");
            } catch (Exception e) {
                Logger.t(TAG).e("解析source失败，尝试获取加密链接");
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(videoUrl)) {
                // 找不到的话 解密
                final String reg = "document.write\\(strencode\\(\"(.+)\",\"(.+)\",.+\\)\\);";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(html);
                String param1 = "", param2 = "";
                if (m.find()) {
                    param1 = m.group(1);
                    param2 = m.group(2);
                    param1 = new String(Base64.decode(param1.getBytes(), Base64.DEFAULT));
                    String source_str = "";
                    for (int i = 0, k = 0; i < param1.length(); i++) {
                        k = i % param2.length();
                        source_str += "" + (char) (param1.codePointAt(i) ^ param2.codePointAt(k));
                    }
                    Logger.t(TAG).d("视频source1：" + source_str);
                    source_str = new String(Base64.decode(source_str.getBytes(), Base64.DEFAULT));
                    Logger.t(TAG).d("视频source2：" + source_str);
                    Document source = Jsoup.parse(source_str);
                    videoUrl = source.select("source").first().attr("src");
                } else {
                    //如果都获取不到就找分享链接
                    Logger.t(TAG).e("解析加密链接失败，尝试获取分享链接");
                    try {
                        String shareLink = doc.select("#linkForm2 #fm-video_link").text();
                        Document shareDoc = Jsoup.connect(shareLink)
                                .timeout(3000)
                                .get();
                        videoUrl = shareDoc.select("source").first().attr("src");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        videoResult.setVideoUrl(videoUrl);
        int startIndex = videoUrl.lastIndexOf("/");
        int endIndex = videoUrl.indexOf(".mp4");
        String videoId = videoUrl.substring(startIndex + 1, endIndex);
        videoResult.setVideoId(videoId);
        Logger.t(TAG).d("视频Id：" + videoId);
        parserOtherInfo(document, videoResult, user);
        return videoResult;
    }
}