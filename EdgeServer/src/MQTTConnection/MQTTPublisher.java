package MQTTConnection;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MQTTPublisher {
    private final int qos = 2;
    String publishTopic;
    String clientId;
    MqttClient client;
    MqttConnectOptions options;

    MQTTPublisher() throws MqttException {
        clientId=MQTTInfo.getClient()+"pub";
        publishTopic=MQTTInfo.getTopic()+"pub";

        //Connect client to MQTT Broker
        client = new MqttClient(MQTTInfo.getServerURI(),clientId, new MemoryPersistence());
        options=new MqttConnectOptions();
        options.setCleanSession(true);
    }

    public void publish_message(String payload) throws MqttException {
        client.connect(options);
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        this.client.publish(this.publishTopic, message);
        client.disconnect();
    }
}
