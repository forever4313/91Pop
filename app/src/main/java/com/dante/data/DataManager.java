package com.dante.data;

import com.dante.data.db.DbHelper;
import com.dante.data.model.User;
import com.dante.data.network.ApiHelper;
import com.dante.data.prefs.PreferencesHelper;

/**
 * @author flymegoc
 * @date 2018/3/4
 */

public interface DataManager extends DbHelper, ApiHelper, PreferencesHelper {
    boolean isUserLogin();
    User getUser();

}
