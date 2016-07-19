package com.tech.petabyteboy.hisaab;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by petabyteboy on 17/07/16.
 */
public class GlobalFunctions {

    static Context context;
    public static SharedPreferences sharedPreferences;

    public static String getuser(Context context, String tag) {

        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            return sharedPreferences.getString(tag, null);
        } catch (Exception e) {
            return null;
        }


    }

    public static String getStatus(Context context, String tag) {

        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            return sharedPreferences.getString(tag, "false");
        }
        catch (Exception e){
            return null;
        }


    }
}
