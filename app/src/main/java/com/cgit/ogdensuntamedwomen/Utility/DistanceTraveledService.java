package com.cgit.ogdensuntamedwomen.Utility;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;


public class DistanceTraveledService extends Service {

    DistanceTravelBinder mDistanceTravelBinder = new DistanceTravelBinder();
    static double distanceInMetres;
    public Location mlocation = null;
    double langitude;
    double longitude;
    Location targetLocation;
    public DistanceTraveledService() {
    }

    @Override
    public void onCreate() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("call","location listenr");
                langitude=location.getLatitude();
                longitude=location.getLongitude();
                mlocation=location;

                distanceInMetres = location.distanceTo(targetLocation);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1,
                locationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mDistanceTravelBinder;
    }

    public class DistanceTravelBinder extends Binder{
        public DistanceTraveledService getBinder(){
            return DistanceTraveledService.this;
        }
    }

    public double getDistanceTraveled(double latitude,double longitude ){
        targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(latitude);//your coords of course
        targetLocation.setLongitude(longitude);


        return distanceInMetres;
    }

    public String getlongilati(){
        return String.valueOf(langitude)+" , "+String.valueOf(longitude);

    }
}