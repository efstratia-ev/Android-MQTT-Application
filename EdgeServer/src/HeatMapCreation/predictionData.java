package HeatMapCreation;

public class predictionData {
    private long timestep;
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

    public predictionData(long timestep, int deviceId, double predictedLat, double predictedLong, double predictedRssi, double predictedThroughput) {
        this.timestep = timestep+1;
        this.deviceId = deviceId;
        this.predictedLat = predictedLat;
        this.predictedLong = predictedLong;
        this.predictedRssi = predictedRssi;
        this.predictedThroughput = predictedThroughput;
        this.count=0;
        this.sum=0.0;
    }
}
