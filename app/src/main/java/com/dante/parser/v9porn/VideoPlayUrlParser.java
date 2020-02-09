package com.dante.parser.v9porn;

import com.dante.data.model.User;
import com.dante.data.model.VideoResult;

public interface VideoPlayUrlParser {
    VideoResult parseVideoPlayUrl(String html, User user);
}
