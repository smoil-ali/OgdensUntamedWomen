package com.cgit.ogdensuntamedwomen.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

public class Utils {
    public static void setReadPermission(Context context,boolean permission1){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("permissions",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("permission1",permission1);
        editor.apply();
    }

    public static void setWritePermission(Context context,boolean permission2){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("permissions",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("permission2",permission2);
        editor.apply();
    }

    public static boolean ReadPermission(Context context){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("permissions",0);
        boolean check = preferences.getBoolean("permission1",false);
        return check;
    }

    public static boolean WritePermission(Context context){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("permissions",0);
        boolean check = preferences.getBoolean("permission2",false);
        return check;
    }
}
