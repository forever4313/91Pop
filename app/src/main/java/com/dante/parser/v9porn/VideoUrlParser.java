package com.dante.parser.v9porn;

import com.dante.data.model.User;
import com.dante.data.model.VideoResult;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
        String videoUrl = element.selectFirst("source").attr("src");
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