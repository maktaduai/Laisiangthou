package com.zogamonline.laisiangthou.utils;

import android.app.Activity;
import android.content.Intent;

import com.zogamonline.laisiangthou.R;

/**
 * Creator Maktaduai on 2/8/2016.
 */
public class ThemeUtils {

    private static int mTheme;

    public static final int BLUE = 0;
    public static final int ORANGE = 1;
    public static final int DARK = 2;
    public static final int GREEN = 3;
    public static final int RED = 4;
    public static final int BLUEGREY = 5;
    public static final int INDIGO = 6;

    public static void changeToTheme(Activity activity, int theme){
        mTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));

    }

    public static void onActivityCreateSetTheme(Activity activity){
        switch (mTheme){
            default:
            case BLUE:
                activity.setTheme(R.style.AppTheme_Blue);
                break;
            case ORANGE:
                activity.setTheme(R.style.AppTheme_Orange);
                break;
            case DARK:
                activity.setTheme(R.style.AppTheme_Dark);
                break;
            case GREEN:
                activity.setTheme(R.style.AppTheme_Green);
                break;
            case RED:
                activity.setTheme(R.style.AppTheme_Red);
                break;
            case BLUEGREY:
                activity.setTheme(R.style.AppTheme_BlueGrey);
                break;
            case INDIGO:
                activity.setTheme(R.style.AppTheme_Indigo);
                break;
        }
    }

    public static void setTheme(int theme) {
        ThemeUtils.mTheme= theme;
    }
}
