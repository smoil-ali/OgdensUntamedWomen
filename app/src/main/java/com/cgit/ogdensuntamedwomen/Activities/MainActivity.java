package com.cgit.ogdensuntamedwomen.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.cgit.ogdensuntamedwomen.R;
import com.cgit.ogdensuntamedwomen.Utility.Utils;

import java.util.IllegalFormatCodePointException;

import static com.cgit.ogdensuntamedwomen.Utility.Constants.PERMISSION_READ_EXTERNAL_STORAGE;
import static com.cgit.ogdensuntamedwomen.Utility.Constants.PERMISSION_WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(checkPermission()){
            Toast.makeText(this, "from main", Toast.LENGTH_SHORT).show();
            openScreen();
        }else {
            getPermissions();
        }
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

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    @Override
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

    }

    private void openScreen() {
        Toast.makeText(this, "screen is open", Toast.LENGTH_SHORT).show();
    }
}