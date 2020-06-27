package com.cgit.ogdensuntamedwomen.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.cgit.ogdensuntamedwomen.Utility.DistanceTraveledService;
import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.Utility.Utils;
import com.cgit.ogdensuntamedwomen.adapters.PlacesAdapter;
import com.cgit.ogdensuntamedwomen.model.CSVFile;
import com.cgit.ogdensuntamedwomen.model.Places;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.cgit.ogdensuntamedwomen.Utility.Constants.PERMISSION_READ_EXTERNAL_STORAGE;
import static com.cgit.ogdensuntamedwomen.Utility.Constants.PERMISSION_WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Places> arrayList;
    PlacesAdapter adapter;
    TextView textView;
    public static final int REQUEST_CODE_LOCATION_PERMISSION=101;
    private boolean mLocationPermissionGranted = false;

    public static DistanceTraveledService mDistanceTraveledService;
    boolean bound = false;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DistanceTraveledService.DistanceTravelBinder distanceTravelBinder = (DistanceTraveledService.DistanceTravelBinder)service;
            mDistanceTraveledService = distanceTravelBinder.getBinder();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUpRecyclerView();

        if (mLocationPermissionGranted) {
            displayDistance();
        } else {
            getLocationPermission();
        }

     /*   if(checkPermission()){
            Toast.makeText(this, "from main", Toast.LENGTH_SHORT).show();
            openScreen();
        }else {
            getPermissions();
        }*/
    }

    //Get list of strings from CSV ready to use
    private ArrayList<Places> prepArray() {

        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVFile csvFile = new CSVFile(inputStream);
        ArrayList<Places> myList = csvFile.read();

        return myList;


    }


    private void setUpRecyclerView() {
        arrayList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PlacesAdapter(this,prepArray());
        recyclerView.setAdapter(adapter);
    }

    private void init(){
        recyclerView=findViewById(R.id.mRecyclerView);
        textView=findViewById(R.id.textview);
    }


    private void getPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).
                    setTitle("Permission needed")
                    .setMessage("Read External Storage  permission needed for this app to work. Grant permission")
                    .setNegativeButton("No",((dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    }))
                    .setPositiveButton("Ok",((dialog, which) ->
                            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_READ_EXTERNAL_STORAGE)))
                    .show();
        }else {
            if (Utils.ReadPermission(this)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_READ_EXTERNAL_STORAGE);
            }else {
                new AlertDialog.Builder(this).
                        setTitle("Read External Storage Permission needed")
                        .setMessage("You have to grant permission from Settings")
                        .setNegativeButton("No",((dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        }))
                        .setPositiveButton("Ok",((dialog, which) -> {
                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.cgit.ogdensuntamedwomen")));
                            finish();
                        }))
                        .show();
            }
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).
                    setTitle("Write External Storage Permission needed")
                    .setMessage("Write External Storage  permission needed for this app to work. Grant permission")
                    .setNegativeButton("No",((dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    }))
                    .setPositiveButton("Ok",((dialog, which) ->
                            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_WRITE_EXTERNAL_STORAGE)))
                    .show();
        }else {
            if (Utils.WritePermission(this)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_WRITE_EXTERNAL_STORAGE);
            }else {
                new AlertDialog.Builder(this).
                        setTitle("Permission needed")
                        .setMessage("You have to grant permission from Settings")
                        .setNegativeButton("No",((dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        }))
                        .setPositiveButton("Ok",((dialog, which) -> {
                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.cgit.ogdensuntamedwomen")));
                            finish();
                        }))
                        .show();
            }
        }
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.setReadPermission(this,true);
                if (Utils.ReadPermission(this) && Utils.WritePermission(this)){
                    Toast.makeText(this, "from read", Toast.LENGTH_SHORT).show();
                    openScreen();
                }
            }else {
                Utils.setReadPermission(this,false);
            }
        }else if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.setWritePermission(this, true);
                if (Utils.ReadPermission(this) && Utils.WritePermission(this)){
                    Toast.makeText(this, "from write", Toast.LENGTH_SHORT).show();
                    openScreen();
                }
            }else {
                Utils.setWritePermission(this,false);
            }
        }
    }*/

    private void openScreen() {
        Toast.makeText(this, "screen is open", Toast.LENGTH_SHORT).show();
    }


    private void displayDistance() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distance = 0;
                if(mDistanceTraveledService != null){
                    distance = mDistanceTraveledService.getDistanceTraveled(31.503802d,73.268355d);
                    textView.setText(String.valueOf(distance));
                }
                handler.postDelayed(this, 100);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, DistanceTraveledService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            displayDistance();

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
    }



}
