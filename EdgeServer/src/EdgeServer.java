import MQTTConnection.MQTTSubscriber;
import org.eclipse.paho.client.mqttv3.MqttException;

import static HeatMapCreation.HeatMapUtil.createHeatMap;

class EdgeServer{
    public static void main(String[] argv){
        try {
            createHeatMap();
            //MQTTSubscriber subscriber=new MQTTSubscriber();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

