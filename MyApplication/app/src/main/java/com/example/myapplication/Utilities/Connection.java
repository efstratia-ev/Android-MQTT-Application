package com.example.myapplication.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import com.example.myapplication.MainActivity;

import static androidx.core.content.ContextCompat.startActivity;

public class Connection {
    public static void isConnected(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (!((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected()))) {
            showDialog();
        }
    }
    private static void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.context);
        builder.setMessage("There is no connection to Internet.")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        ((Activity)MainActivity.context).startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),0);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Activity)MainActivity.context).finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
