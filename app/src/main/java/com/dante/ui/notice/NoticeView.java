package com.dante.ui.notice;

import com.dante.data.model.Notice;
import com.dante.ui.update.UpdateView;

/**
 * @author flymegoc
 * @date 2018/1/26
 */

public interface NoticeView extends UpdateView {
    void haveNewNotice(Notice notice);

    void noNewNotice();

    void checkNewNoticeError(String message);
}
