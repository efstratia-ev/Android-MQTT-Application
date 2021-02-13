package Predictions;

import java.util.Arrays;

public class PredictionData {
    private double timeStep;
    private int deviceId;
    private double realLat;
    private double realLong;
    private double predictedLat;
    private double predictedLong;
    private double realRssi;
    private double realThroughput;
    private double predictedRssi;
    private double predictedThroughput;
    private int count;
    private double sum;
    private boolean end;

    public PredictionData(int deviceId) {
        this.deviceId = deviceId;
        this.count=0;
        this.sum=0.0;
        this.end=false;
    }

    public String getInfo(){
        String[] info={String.valueOf(timeStep), String.valueOf(deviceId), String.valueOf(predictedLat), String.valueOf(predictedLong), String.valueOf(predictedRssi), String.valueOf(predictedThroughput)};
        return Arrays.toString(info);
    }

    public void updatePredictedData(double predictedLat,double predictedLong,double predictedRssi,double predictedThroughput){
        this.predictedLat = predictedLat;
        this.predictedLong = predictedLong;
        this.predictedRssi = predictedRssi;
        this.predictedThroughput = predictedThroughput;
    }

    public void updateRealData(double realLat,double realLong,double realRssi,double realThroughput){
        this.realLat = realLat;
        this.realLong = realLong;
        this.realRssi = realRssi;
        this.realThroughput = realThroughput;
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getIdAsString() {
        return String.valueOf(deviceId);
    }
    public double getPredictedLat() {
        return predictedLat;
    }

    public double getPredictedLong() {
        return predictedLong;
    }

    public double getMeanError() {
        return sum/count;
    }

    public void updateCount() {
        this.count++;
    }

    public void updateSum(double sum) {
        this.sum += sum;
    }

    public static double formulaLat(double d, double lat, double angle){
        return Math.toDegrees(Math.asin(Math.sin(lat)*Math.cos(d)+Math.cos(lat)*Math.sin(d)*Math.cos(angle)));
    }

    public static double formulaLong(double d, double lon,double latStart,double latEnd, double angle){
        return Math.toDegrees(lon+Math.atan2(Math.sin(angle)*Math.sin(d)*Math.cos(latStart),Math.cos(d)-Math.sin(latStart)*Math.sin(latEnd)));
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return dist * 1.609344 * 1000;

        }
    }

    public double getTimeStep() {
        return timeStep;
    }

    public double getRealLat() {
        return realLat;
    }

    public double getRealLong() {
        return realLong;
    }

    public double getRealRssi() {
        return realRssi;
    }

    public double getRealThroughput() {
        return realThroughput;
    }

    public double getPredictedRssi() {
        return predictedRssi;
    }

    public double getPredictedThroughput() {
        return predictedThroughput;
    }

    public void completed(){
        this.end=true;
    }

    public boolean hasCompleted(){return this.end;}
}
