package com.example.myapplication.MQTTConnection;

import android.content.Context;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Utilities.CreateMarkers;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTSubscriber implements MqttCallback {
    private final int qos = 2;
    String subscriptionTopic;
    String clientId;
    MqttAndroidClient client;

    public MQTTSubscriber(Context context) throws MqttException {
        clientId=MQTTInfo.getClient()+"sub";
        subscriptionTopic="EStoAT"+ MainActivity.TerminalID;

        //Connect client to MQTT Broker
        client = new MqttAndroidClient(context,MQTTInfo.getServerURI(),clientId,new MemoryPersistence());
        MqttConnectOptions options=new MqttConnectOptions();
        options.setCleanSession(true);

        client.setCallback(this);
        client.connect(options);
        client.connect(options,new IMqttActionListener(){
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {
                    client.subscribe(subscriptionTopic,qos);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {}
        });

    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost, "+throwable);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String[] line = mqttMessage.toString().replace("[","").replace("]","").split(", ");
        CreateMarkers.createPredictionMarker(Double.parseDouble(line[2]),Double.parseDouble(line[3]),line[0],"Latitude:"+line[2]
                +"\nLongitude:"+line[3]+"\nRSSI:"+line[4]+"\nThroughput: "+line[5]);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
