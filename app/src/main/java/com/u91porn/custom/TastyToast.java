package com.u91porn.custom;

import android.content.Context;

import es.dmoral.toasty.Toasty;

/**
 * Created by yons on 18/3/27.
 */

public class TastyToast {
    public static final int SUCCESS = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    public static final int INFO = 4;
    public static final int DEFAULT = 5;
    public static final int CONFUSING = 6;
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    public static void makeText(Context context, String msg, int length, int type) {
        switch (type) {
            case SUCCESS:
                Toasty.success(context, msg, length, true).show();
                break;
            case WARNING:
                Toasty.warning(context, msg, length, true).show();
                break;
            case ERROR:
                Toasty.error(context, msg, length, true).show();
                break;
            case DEFAULT:
                Toasty.info(context, msg, length, true).show();
                break;
            case CONFUSING:
                Toasty.info(context, msg, length, true).show();
                break;
            default:
                Toasty.info(context, msg, length, true).show();
                break;

        }
    }
}
