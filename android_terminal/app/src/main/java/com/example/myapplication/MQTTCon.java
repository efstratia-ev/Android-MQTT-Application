package com.example.myapplication;


import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


import static java.nio.charset.StandardCharsets.UTF_8;

public class MQTTCon {
    public MqttAndroidClient client;

    String serverURI = "tcp://192.168.1.3:1883";
    final String clientId = "ExampleAndroidClient";
    final String subscriptionTopic = "sensor";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MQTTCon(Context context) throws MqttException {
        client = new MqttAndroidClient(context,serverURI,clientId,new MemoryPersistence()); //Persistence

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        client.connect(mqttConnectOptions,new IMqttActionListener(){
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                subscribeToTopic();
                publish_message("freudhfner");
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }

    public void setCallback() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
               // MainActivity.dataReceived.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    public void publish_message(String payload){
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        message.setQos(2);
        try {
            client.publish(subscriptionTopic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribeToTopic() {
        try {
            client.subscribe(subscriptionTopic,2);

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }
}
