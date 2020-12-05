package com.example.myapplication.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.myapplication.MQTTConnection.MQTTPublisher;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import org.eclipse.paho.client.mqttv3.MqttException;

public class HomeFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static int i = 0;
    @SuppressLint("StaticFieldLeak")
    private static TextView txtView;
    private final Handler hdlr = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle   savedInstanceState) {
        final int max=10;
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.pBar);
        progressBar.setMax(max);
        txtView = (TextView) view.findViewById(R.id.tView);
        if(MainActivity.restart) {
            progressBar.setVisibility(View.GONE); // to hide
            txtView.setVisibility(View.GONE);
        }
        final Button btn = (Button)view.findViewById(R.id.btnShow);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MQTTPublisher MQTTPub = null;
                try {
                    MQTTPub=new MQTTPublisher(getActivity().getApplicationContext());
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                if (btn.getText().length() == 0 || btn.getText().equals("Stop Progress")) {
                    btn.setText("Start Progress");
                    MainActivity.restart=true;
                    progressBar.setVisibility(View.GONE); // to hide
                    txtView.setVisibility(View.GONE);
                }
                else {
                    MainActivity.restart=false;
                    btn.setText("Stop Progress");
                    progressBar.setVisibility(View.VISIBLE); //to show
                    txtView.setVisibility(View.VISIBLE);
                    i = 0;
                    final MQTTPublisher finalMQTTPub = MQTTPub;
                    new Thread(new Runnable() {
                        public void run() {
                            while (i < max && !MainActivity.restart) {
                                try {
                                    finalMQTTPub.publish_message("lala"+i);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                                i += 1;
                                // Update the progress bar and display the current value in text view
                                hdlr.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(i);
                                        txtView.setText(i+"/"+progressBar.getMax());
                                    }
                                });
                                try {
                                    // Sleep for 100 milliseconds to show the progress slowly.
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }
        });
        return view;
    }
}