package MQTTConnection;

import HeatMapCreation.HeatMapUtil;
import Predictions.PredictionDataUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static javafx.application.Platform.exit;

public class MQTTSubscriber implements MqttCallback {
    String[] topics;
    String clientId;
    MqttClient client;
    PredictionDataUtil predictions;

    public MQTTSubscriber() throws Exception {
        predictions=new PredictionDataUtil();
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
        String message=mqttMessage.toString();
        if ("END".equals(message.substring(0,3))) {
            predictions.vehicleCompleted(Integer.parseInt(message.substring(4)));
            if(predictions.allVehiclesCompleted()) {
                predictions.closeConnection();
                predictions.makeBarChart();
                System.exit(0);
            }
        }
        else predictions.predictData(message, HeatMapUtil.heatMapRSSI,HeatMapUtil.heatMapThroughput);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
