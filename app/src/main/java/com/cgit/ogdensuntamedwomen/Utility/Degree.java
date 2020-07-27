package com.cgit.ogdensuntamedwomen.Utility;


import android.util.Log;

public class Degree {

    public static float getDegree(String currentLocation,String destination){
        Log.i("degrees",currentLocation);
        float degree = (float) 0.0;

        String[] current = currentLocation.split(",");
        String[] dest = destination.split(",");

        double current_Lat = Math.toRadians(Double.parseDouble(current[0]));
        double current_Lng = Double.parseDouble(current[1]);

        double dest_Lat = Math.toRadians(Double.parseDouble(dest[0]));
        double dest_Lng = Double.parseDouble(dest[1]);

        double lng_def = Math.toRadians(dest_Lng-current_Lng);

        double y= Math.sin(lng_def)*Math.cos(dest_Lat);
        double x=Math.cos(current_Lat)*Math.sin(dest_Lat)-Math.sin(current_Lat)*Math.cos(dest_Lat)*Math.cos(lng_def);
        double result = (Math.toDegrees(Math.atan2(y, x))+360)%360;
        degree = (float) result;
        Log.i("degree", String.valueOf(degree));
        return degree;
    }
}

