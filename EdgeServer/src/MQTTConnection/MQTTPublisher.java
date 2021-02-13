package MQTTConnection;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MQTTPublisher {
    String clientId;
    MqttClient client;
    MqttConnectOptions options;

    public MQTTPublisher() throws MqttException {
        clientId=MQTTInfo.getClient()+"pub";

        //Connect client to MQTT Broker
        client = new MqttClient(MQTTInfo.getServerURI(),clientId, new MemoryPersistence());
        options=new MqttConnectOptions();
        options.setCleanSession(true);
    }

    public void publish_message(String payload,String ID) throws MqttException {
        client.connect(options);
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(2);
        client.publish(MQTTInfo.getPublishingTopic(ID),message);
        client.disconnect();
    }
}
