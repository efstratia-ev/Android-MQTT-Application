package MQTTConnection;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTSubscriber implements MqttCallback {
    String[] topics;
    String clientId;
    MqttClient client;

    public MQTTSubscriber() throws MqttException {
        clientId=MQTTInfo.getClient()+"sub";
        topics=MQTTInfo.getSubscriptionTopics();

        //Connect client to MQTT Broker
        client = new MqttClient(MQTTInfo.getServerURI(),clientId, new MemoryPersistence());
        MqttConnectOptions options=new MqttConnectOptions();
        options.setCleanSession(true);

        client.setCallback(this);
        this.client.connect(options);
        this.client.subscribe(topics,MQTTInfo.getQos());
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost, "+throwable);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println(topic + ": " + mqttMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
