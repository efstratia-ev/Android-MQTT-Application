package com.example.myapplication;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {
    MQTTCon MQTTCon;
    TextView dataReceived;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataReceived = (TextView) findViewById(R.id.dataReceived);

        startMqtt();
        dataReceived.setText("dfsfsd");
    }

    private void startMqtt() {
        try {
            MQTTCon = new MQTTCon(getApplicationContext());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        // MQTTConnection.publish_message("lala");
        //MQTTConnection.setCallback();
       // MQTTConnection.subscribeToTopic();
    }
}