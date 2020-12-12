package com.example.myapplication.MQTTConnection;

import android.content.Context;
import com.example.myapplication.MainActivity;
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
        publishTopic="ATtoES"+ MainActivity.TerminalID;

        //Connect client to MQTT Broker
        client = new MqttAndroidClient(context,MQTTInfo.getServerURI(),clientId,new MemoryPersistence()); //Persistence
        mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
    }

    public boolean publish_message(final String payload) throws MqttException {
        final boolean[] returnValue = {true};
        client.connect(mqttConnectOptions,new IMqttActionListener(){
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                MqttMessage message = new MqttMessage(payload.getBytes());
                message.setQos(qos);
                try {
                    client.publish(publishTopic, message);
                } catch (MqttException e) {
                    returnValue[0] =false;
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
        return returnValue[0];
    }
}
