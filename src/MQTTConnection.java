import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MQTTConnection {
    public MqttClient client;

    final String serverURI = "tcp://127.0.0.1:1883";
    final String clientId = "ExampleClient";
    final String subscriptionTopic = "sensor";


    public MQTTConnection() throws MqttException {
        client = new MqttClient(serverURI,clientId, new MemoryPersistence()); //Persistence
        setCallback();
        client.connect();
        subscribeToTopic();
    }

    public void setCallback() {
        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) { //Called when the client lost the connection to the broker
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println(topic + ": " + message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {//Called when a outgoing publish is complete
            }
        });
    }

    public void publish_message(String payload){
        try {
            client.publish("topic",payload.getBytes(UTF_8), 2, false);
        }
        catch (MqttException e) {
            e.printStackTrace();
        }
    }


    private void subscribeToTopic() {
        try {
            client.subscribe(subscriptionTopic,2);

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }
}
