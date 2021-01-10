package Predictions;

import HeatMapCreation.DatabaseUtil;
import HeatMapCreation.HeatMap;
import MQTTConnection.MQTTPublisher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;

public class PredictionDataUtil {
     ArrayList<PredictionData> predictionData = new ArrayList<>();
     DatabaseUtil vehicleDatabase=new DatabaseUtil();

    public PredictionDataUtil() throws Exception {
    }

    public void predictData(String data, HeatMap heatMapRSSI, HeatMap heatMapThroughput) throws Exception {
         String[] vehicleData = data.replace("[","").replace("]","").split(", ");
         final double r=6371000,t=1.0;
         int vehicleId;
         boolean firstTime;
         PredictionData currentVehicle;

         //timestep:0 id:1 lat(y):2 lon(x):3 angle:4 speed:5 rssi:6 throughput:7
         vehicleId=Integer.parseInt(vehicleData[1]);
         currentVehicle=containsId(vehicleId);
         firstTime=false;
         if(currentVehicle==null) {
             currentVehicle=new PredictionData(vehicleId);
             predictionData.add(currentVehicle);
             firstTime=true;
         }

         double latStart=Double.parseDouble(vehicleData[2]),longStart=Double.parseDouble(vehicleData[3]),
                 angle=Double.parseDouble(vehicleData[4]),speed=Double.parseDouble(vehicleData[5]),d=t*speed/r;
         double latEnd=currentVehicle.formulaLat(Math.toRadians(d),Math.toRadians(latStart),Math.toRadians(angle)),
                 longEnd=currentVehicle.formulaLong(Math.toRadians(d),Math.toRadians(longStart),Math.toRadians(latStart),Math.toRadians(latEnd),Math.toRadians(angle));


         if(!firstTime) {
             currentVehicle.updateRealData(latStart,longStart,heatMapRSSI.getValue(latStart,longStart),heatMapThroughput.getValue(latStart,longStart));
             currentVehicle.updateSum(currentVehicle.distance(latStart,longStart,currentVehicle.getPredictedLat(),currentVehicle.getPredictedLong()));
             currentVehicle.updateCount();
            // System.out.println("*** database insert ***");
             vehicleDatabase.insert(currentVehicle);
             MQTTPublisher MQTTPub=new MQTTPublisher();
             MQTTPub.publish_message(currentVehicle.getInfo());
         }

        currentVehicle.setTimeStep(Double.parseDouble(vehicleData[0]));
        currentVehicle.updatePredictedData(latEnd,longEnd,heatMapRSSI.getValue(latEnd,longEnd),heatMapThroughput.getValue(latEnd,longEnd));


         //System.out.println( vehicleData[0]+","+vehicleData[2] + "," + vehicleData[3] + "," + vehicleData[6] + ","+  vehicleData[7]+"\n");
     }

    private PredictionData containsId(final int id){
        return predictionData.stream().filter(vehicle -> id == vehicle.getDeviceId()).findFirst().orElse(null);
    }

    private void calculateMeanErrors(){
        predictionData.stream().forEach(vehicle -> {System.out.println("Mean Error of Vehicle "+vehicle.getDeviceId()+" is : "+vehicle.getMeanError());});
    }

    public void calculateError() throws SQLException {
        calculateMeanErrors();
        vehicleDatabase.closeConnection();
    }
}
