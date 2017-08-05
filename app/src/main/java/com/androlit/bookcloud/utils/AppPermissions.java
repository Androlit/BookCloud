package com.androlit.bookcloud.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by rubel on 5/21/2017.
 */

public final class AppPermissions {

    public static final int REQUEST_LOCATION = 401;
    public static final String REQUEST_LOCATION_MESSAGE = "This app needs location permission to work properly!";

    public static boolean checkLocationPermission(final Context context){
        final String fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

        if(shouldAskPermission()){
            if(ContextCompat.checkSelfPermission(context, fineLocationPermission)
                    != PackageManager.PERMISSION_GRANTED ){
                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        fineLocationPermission)){

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{fineLocationPermission},
                        REQUEST_LOCATION);

                }else{
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{fineLocationPermission},
                            REQUEST_LOCATION);
                }
                // user denied permission handle from calling activity's onRequestPermissionsResult
                return false;
            }

            return true;
        }

        return true;
    }

    private static boolean shouldAskPermission(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
