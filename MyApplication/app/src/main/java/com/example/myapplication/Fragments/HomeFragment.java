package com.example.myapplication.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.myapplication.MQTTConnection.MQTTPublisher;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Objects;

public class HomeFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static int i = 0;
    @SuppressLint("StaticFieldLeak")
    private static TextView txtView;
    private final Handler hdlr = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle   savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.pBar);
        progressBar.setMax(MainActivity.measurementsSend);
        txtView = (TextView) view.findViewById(R.id.tView);
        if(MainActivity.restart) {
            progressBar.setVisibility(View.GONE); // to hide
            txtView.setVisibility(View.GONE);
        }
        final Button btn = (Button)view.findViewById(R.id.btnShow);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    new Thread(new Runnable() {
                        public void run() {
                            MQTTPublisher MQTTPub = null;
                            try {
                                MQTTPub=new MQTTPublisher(getActivity().getApplicationContext());
                            } catch (MqttException e) {
                                e.printStackTrace();
                                return;
                            }
                            try {
                                MainActivity.csvReader.resetFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                            while (i < MainActivity.measurementsSend && !MainActivity.restart) {
                                try {
                                    String toSend=MainActivity.csvReader.readLine();
                                    if(!MQTTPub.publish_message(toSend)) break;
                                } catch (MqttException | IOException e) {
                                    e.printStackTrace();
                                    return;
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
                                    return;
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