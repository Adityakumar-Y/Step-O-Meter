package com.example.steptracker.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.steptracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(checkPermission()) {
            gotoMainActivity();
        }
    }

    private boolean checkPermission(){
        // Check which permissions are granted
        List<String> listPermissionNeeded = new ArrayList<>();
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                listPermissionNeeded.add(permission);
            }
        }

        // Ask for non-granted permission
        if(!listPermissionNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
            return false;
        }

        // App has all the permissions proceed ahead
        return true;
    }


    private void gotoMainActivity() {
        new Handler().postDelayed(
                () -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                },
                5000
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;

            for(int i=0; i<grantResults.length; i++){
                // Adding Permissions which are denied
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            // Check if all permissions are granted
            if(deniedCount == 0){
                gotoMainActivity();
            }else{
                for(Map.Entry<String, Integer> entry : permissionResults.entrySet()){
                    String permissionName = entry.getKey();

                    /*Permission is denied for first time and never ask again is not checked
                      So ask again explaining usage of permission*/
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)){
                        showAlertDialog("", getString(R.string.permission_explanation_msg),
                                "Yes, Grant",
                                (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    checkPermission();
                                },
                                "No, Exit",
                                (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    finish();
                                }, false);
                    }
                    // Permission Denied and Never Ask Again checked
                    else{
                        showAlertDialog("", getString(R.string.goto_settings_msg),
                                "Go to Settings",
                                (dialogInterface, i) -> {
                                    dialogInterface.dismiss();

                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                },
                                "No, Exit App",
                                (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    finish();
                                }, false);
                    }
                }
            }
        }
    }

    public AlertDialog showAlertDialog(String title, String msg,
                                  String positiveLabel,
                                  DialogInterface.OnClickListener positiveOnClick,
                                  String negativeLabel,
                                  DialogInterface.OnClickListener negativeOnClick,
                                  boolean isCancelable)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(isCancelable);
        builder.setPositiveButton(positiveLabel, positiveOnClick);
        builder.setNegativeButton(negativeLabel, negativeOnClick);

        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
