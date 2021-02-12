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
import androidx.fragment.app.Fragment;
import com.example.myapplication.MQTTConnection.MQTTPublisher;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

import static com.example.myapplication.MainActivity.MQTTPub;

public class HomeFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static int i = 0;
    @SuppressLint("StaticFieldLeak")
    private static TextView txtView;
    private static String button_value="Start Progress";
    private final Handler hdlr = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle   savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.pBar);
        progressBar.setMax(MainActivity.measurementsSend);
        txtView = (TextView) view.findViewById(R.id.tView);
        txtView.setText(i+"/"+MainActivity.measurementsSend);
        progressBar.setProgress(i);
        if(i==0) {
            progressBar.setVisibility(View.GONE); // to hide
            txtView.setVisibility(View.GONE);
        }
        final Button btn = (Button)view.findViewById(R.id.btnShow);
        btn.setText(button_value);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==MainActivity.max){
                    i=0;
                    btn.setText("Restart Progress");
                    button_value="Restart Progress";
                }
                else if (i!=0 && btn.getText().equals("Pause Progress")) {
                    btn.setText("Continue Progress");
                    button_value="Continue Progress";
                    MainActivity.restart=true;
                }
                else {
                    if(i==0){
                        try {
                            MainActivity.csvReader.resetFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    MainActivity.restart=false;
                    btn.setText("Pause Progress");
                    button_value="Pause Progress";
                    progressBar.setVisibility(View.VISIBLE); //to show
                    txtView.setVisibility(View.VISIBLE);
                    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            System.out.println("Caught " + e);
                        }
                    });
                    Thread t=new Thread(){
                        public void run() {
                            while (i < MainActivity.measurementsSend && !MainActivity.restart) {
                                try {
                                    String toSend=MainActivity.csvReader.readLine();
                                    if(!MQTTPub.publish_message(toSend)) break;
                                } catch (MqttException | IOException e) {
                                    e.printStackTrace();
                                    return;
                                }
                                i += 1;
                                hdlr.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(i);
                                        txtView.setText(i+"/"+progressBar.getMax());
                                    }
                                });
                                // Update the progress bar and display the current value in text view
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }
            }
        });
        return view;
    }
}