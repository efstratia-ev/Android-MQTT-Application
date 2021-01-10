package com.example.myapplication.Utilities;

import android.graphics.Color;
import com.example.myapplication.MainActivity;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

abstract public class CreateMarkers {
    public static void createMarker(double latitude, double longitude, String title, String snippet,String color) {
        MainActivity.markersList.add(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(getMarkerIcon(color))
                .snippet(snippet));
    }

    private static BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
