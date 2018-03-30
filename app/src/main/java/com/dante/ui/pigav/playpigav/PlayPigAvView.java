package com.dante.ui.pigav.playpigav;

import com.dante.data.model.PigAv;
import com.dante.data.model.PigAvVideo;
import com.dante.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/30
 */

public interface PlayPigAvView extends BaseView {
    void playVideo(PigAvVideo pigAvVideo);

    void listVideo(List<PigAv> pigAvList);
}
