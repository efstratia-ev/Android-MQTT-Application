package com.example.myapplication.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.myapplication.MainActivity.markersList;
import static com.example.myapplication.MainActivity.predictionMarkersList;

public class PredictionsFragment extends Fragment implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_predictions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        final Handler handler=new Handler();
        final OnMapReadyCallback callback=this;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callback);
                }
                handler.postDelayed(this,500);
            }
        },500);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMinZoomPreference(15);
        map.setMaxZoomPreference(30);
        map.setBuildingsEnabled(true);
        Marker marker;
        if(predictionMarkersList.isEmpty()) return;
        for(MarkerOptions M:predictionMarkersList) {
            marker=map.addMarker(M);
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        //  map.animateCamera( CameraUpdateFactory.zoomTo( 25.0f ) );
        // map.animateCamera(cu);
        map.moveCamera(cu);
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(MainActivity.context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(MainActivity.context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(MainActivity.context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.showInfoWindow();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}
