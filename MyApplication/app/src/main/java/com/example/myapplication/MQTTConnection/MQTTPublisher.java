package com.example.myapplication.MQTTConnection;

import android.content.Context;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MQTTPublisher {
    private final int qos = 2;
    String publishTopic;
    String clientId;
    MqttAndroidClient client;
    MqttConnectOptions mqttConnectOptions;

    public MQTTPublisher(Context context) throws MqttException {
        clientId=MQTTInfo.getClient()+"pub";
        publishTopic=MQTTInfo.getTopic();

        //Connect client to MQTT Broker
        client = new MqttAndroidClient(context,MQTTInfo.getServerURI(),clientId,new MemoryPersistence()); //Persistence
        mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
    }

    public void publish_message(final String payload) throws MqttException {
        client.connect(mqttConnectOptions,new IMqttActionListener(){
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                MqttMessage message = new MqttMessage(payload.getBytes());
                message.setQos(qos);
                try {
                    client.publish(publishTopic, message);
                    IMqttToken disconnect = client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }
}
