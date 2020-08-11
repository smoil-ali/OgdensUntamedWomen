package com.cgit.ogdensuntamedwomen.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cgit.ogdensuntamedwomen.Utility.Degree;
import com.cgit.ogdensuntamedwomen.Utility.DistanceTraveledService;
import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.Utility.Utils;
import com.cgit.ogdensuntamedwomen.adapters.PlacesAdapter;
import com.cgit.ogdensuntamedwomen.model.CSVFile;
import com.cgit.ogdensuntamedwomen.model.CSVReader;
import com.cgit.ogdensuntamedwomen.model.Places;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.cgit.ogdensuntamedwomen.Utility.Constants.PERMISSION_READ_EXTERNAL_STORAGE;
import static com.cgit.ogdensuntamedwomen.Utility.Constants.PERMISSION_WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView,navRecycler;
    ArrayList<Places> arrayList;
    PlacesAdapter adapter;
    PlacesAdapter compasAdapter;
    TextView textView;
    Location targetLocation;
    public static final int REQUEST_CODE_LOCATION_PERMISSION=101;
    private boolean mLocationPermissionGranted = false;
    public static int nearestDestance;
    ImageButton menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    public static boolean setEnabled=true;

    public static float[] locationInDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu=findViewById(R.id.menu);
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.menu_switch).setActionView(new Switch(this));
        Switch swich=((Switch)navigationView.getMenu().findItem(R.id.menu_switch).getActionView());
        swich.setChecked(true);
        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    setEnabled=true;
                    Log.i("checkon",String.valueOf(setEnabled));
                }
                else{
                    setEnabled=false;
                    Log.i("checkon",String.valueOf(setEnabled));
                }
                adapter.notifyDataSetChanged();
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        if(checkPermissions()){
            openScreen();
        }else {
            getPermissions();
        }



     /*   if(checkPermission()){
            Toast.makeText(this, "from main", Toast.LENGTH_SHORT).show();
            openScreen();
        }else {
            getPermissions();
        }*/
    }



    //Get list of strings from CSV ready to use
    private void prepArray() {
        arrayList=new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVReader csvReader=new CSVReader(MainActivity.this);
        arrayList=csvReader.read(inputStream);



       /* InputStream inputStream = null;
        try {
            inputStream = getAssets().open("data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVFile csvFile = new CSVFile(inputStream);
        arrayList = csvFile.read();*/
    }


    private void setUpRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PlacesAdapter(this,arrayList,"data");
        recyclerView.setAdapter(adapter);
    }

    private void setUpcompasView(){
        navRecycler.setLayoutManager(new LinearLayoutManager(this));
        compasAdapter=new PlacesAdapter(this,arrayList,"compass");
        navRecycler.setAdapter(compasAdapter);
    }

    private void init(){
        recyclerView=findViewById(R.id.mRecyclerView);
        textView=findViewById(R.id.textview);
        navRecycler=findViewById(R.id.compasLayout);
        targetLocation=new Location("");
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            setLocationListener();

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE ){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                openScreen();
            }
        }
    }

    private void openScreen() {
        init();
        prepArray();
        setUpRecyclerView();
        setUpcompasView();
        setLocationListener();
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }


    private void getPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION)){
            new AlertDialog.Builder(this).
                    setTitle("Permission needed")
                    .setMessage("Permissions needed for this app to work. Grant permission")
                    .setNegativeButton("No",((dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    }))
                    .setPositiveButton("Ok",((dialog, which) ->
                            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    WRITE_EXTERNAL_STORAGE,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},PERMISSION_READ_EXTERNAL_STORAGE)))
                    .show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,
                    ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},PERMISSION_READ_EXTERNAL_STORAGE);
        }
    }



    public void setLocationListener(){
        locationInDegree = new float[arrayList.size()];
        Log.i("distance",arrayList.get(0).getDistance());
       MainActivity.nearestDestance=Integer.parseInt(arrayList.get(0).getDistance());
    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i("check lcoation",String.valueOf(location.getLatitude()));
            for (int i=0;i<arrayList.size();i++){
               targetLocation.setLatitude(Double.parseDouble(arrayList.get(i).getLang()));
               targetLocation.setLongitude(Double.parseDouble(arrayList.get(i).getLng()));
               arrayList.get(i).getLng();
               double distance= (location.distanceTo(targetLocation)/1609.344);
               locationInDegree[i]=  Degree.getDegree(location.getLatitude()+","+location.getLongitude()
                        ,arrayList.get(i).getLang()+","+arrayList.get(i).getLng());
               arrayList.get(i).setDistance(String.valueOf(distance));
               if ((int)distance<MainActivity.nearestDestance){
                   Log.i("nearest distance",String.valueOf(MainActivity.nearestDestance));
                   Places.setNear(true);
               }
            }
            compasAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
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
            ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
            ACCESS_COARSE_LOCATION) !=
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_switch:
               Switch switch_item= (Switch)item.getActionView();
                switch_item.toggle();
                if (switch_item.isChecked()){
                    setEnabled=true;
                    Log.i("checkon",String.valueOf(setEnabled));

                }

                else
                   {

                       setEnabled=false;
                       Log.i("checkon",String.valueOf(setEnabled));
                   }
                adapter.notifyDataSetChanged();
                return true;
        }
        return true;
    }
}
