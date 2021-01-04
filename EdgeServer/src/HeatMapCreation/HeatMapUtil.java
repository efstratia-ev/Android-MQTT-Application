package HeatMapCreation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class HeatMapUtil {

    private enum Limits {
        MIN_LAT(37.9668800),MAX_LAT(37.9686200),MIN_LONG(23.7647600),MAX_LONG(23.7753900);
        private final double value;
        private Limits(double value) {
            this.value = value;
        }
        public double value() {
            return value;
        }
    }
    public static void createHeatMap() throws Exception {
        //convert all 3 xml files to csv
        CreateCSV.XMLFileToCSV("InputData/all_vehicles.xml","OutputData/all_vehicles.csv");
        CreateCSV.XMLFileToCSV("InputData/vehicle_26.xml","OutputData/vehicle_26.csv");
        CreateCSV.XMLFileToCSV("InputData/vehicle_27.xml","OutputData/vehicle_27.csv");

        //produce heat maps
        HeatMap heatMapRSSI=new HeatMap(Limits.MIN_LAT.value(),Limits.MAX_LAT.value(),Limits.MIN_LONG.value(),Limits.MAX_LONG.value());
        HeatMap heatMapThroughput=new HeatMap(Limits.MIN_LAT.value(),Limits.MAX_LAT.value(),Limits.MIN_LONG.value(),Limits.MAX_LONG.value());
        HeatMapUtil.readCSVandProduceHeatMap("OutputData/all_vehicles.csv",heatMapRSSI,heatMapThroughput);
        HeatMapUtil.combineMapAndHeatMap("InputData/Map.png","OutputData/rssi.png","OutputData/heatmapRSSI.png");
        HeatMapUtil.combineMapAndHeatMap("InputData/Map.png","OutputData/throughput.png","OutputData/heatmapThroughput.png");


        //Dummy Funtion
        readCSVDummyFunction("OutputData/vehicle_26.csv",heatMapRSSI,heatMapThroughput);
        readCSVDummyFunction("OutputData/vehicle_27.csv",heatMapRSSI,heatMapThroughput);
    }

    public static void combineMapAndHeatMap(String inputFile1,String inputFile2,String outputFile) throws Exception{
        BufferedImage imageMap = ImageIO.read(new File(inputFile1));
        BufferedImage imageHeat = ImageIO.read(new File(inputFile2));
        imageHeat = resizeImage(imageHeat,1339,275);


        Graphics2D g = imageHeat.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g.drawImage(imageMap,  (imageHeat.getWidth() - imageMap.getWidth())/2,(imageHeat.getHeight()-imageMap.getHeight())/2, null);
        g.dispose();

        ImageIO.write(imageHeat, "png", new File(outputFile));
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    static public void readCSVandProduceHeatMap(String csvFilename, HeatMap heatMapRSSI, HeatMap heatMapThroughput) throws Exception{
        String line = "";
        String cvsSplitBy = ",";

        BufferedReader br = new BufferedReader(new FileReader(csvFilename));
        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] vehicleData = line.split(cvsSplitBy);
            double lat=Double.parseDouble(vehicleData[2]),lon=Double.parseDouble(vehicleData[3]);
            if (heatMapRSSI.isInGrid(lat,lon)) {
                heatMapRSSI.updateValues(heatMapRSSI.getRow(lat), heatMapRSSI.getColumn(lon), Double.parseDouble(vehicleData[6]));
                heatMapThroughput.updateValues(heatMapThroughput.getRow(lat), heatMapThroughput.getColumn(lon), Double.parseDouble(vehicleData[7]));
            }
            //System.out.println( vehicleData[0]+","+vehicleData[2] + "," + vehicleData[3] + "," + vehicleData[6] + ","+  vehicleData[7]+"\n");
        }
        heatMapRSSI.makeHeatMapPNG("OutputData/rssi.png");
        heatMapThroughput.makeHeatMapPNG("OutputData/throughput.png");
    }

    static public void readCSVDummyFunction(String csvFilename,HeatMap heatMapRSSI,HeatMap heatMapThroughput) throws Exception{
        String line = "";
        String cvsSplitBy = ",";
        final double r=6371000,t=1.0;
        int vehicleId;
        boolean firstTime;
        DatabaseUtil vehicleDatabase=new DatabaseUtil();

        BufferedReader br = new BufferedReader(new FileReader(csvFilename));
        ArrayList<PredictionData> predictionData = new ArrayList<>();
        PredictionData currentVehicle;
        while ((line = br.readLine()) != null) {
            String[] vehicleData = line.split(cvsSplitBy);
            //timestep:0 id:1 lat(y):2 lon(x):3 angle:4 speed:5 rssi:6 throughput:7
            vehicleId=Integer.parseInt(vehicleData[1]);
            currentVehicle=containsId(predictionData,vehicleId);
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
            System.out.format("Predictions are -- latS: %.6f and lonS:  %.6f -- latE:  %.6f and lonE:  %.6f\n",latStart,longStart,latEnd,longEnd);
            System.out.format("Rssi: %.6f and Throughput %.6f\n",heatMapRSSI.getValue(latEnd,longEnd),heatMapThroughput.getValue(latEnd,longEnd));


            if(!firstTime) {
                currentVehicle.updateRealData(latStart,longStart,heatMapRSSI.getValue(latStart,longStart),heatMapThroughput.getValue(latStart,longStart));
                currentVehicle.updateSum(currentVehicle.distance(latStart,longStart,currentVehicle.getPredictedLat(),currentVehicle.getPredictedLong()));
                currentVehicle.updateCount();
                System.out.println("*** database insert ***");
                vehicleDatabase.insert(currentVehicle);
            }
            currentVehicle.setTimeStep(Double.parseDouble(vehicleData[0]));
            currentVehicle.updatePredictedData(latEnd,longEnd,heatMapRSSI.getValue(latEnd,longEnd),heatMapThroughput.getValue(latEnd,longEnd));

            //System.out.println( vehicleData[0]+","+vehicleData[2] + "," + vehicleData[3] + "," + vehicleData[6] + ","+  vehicleData[7]+"\n");
        }
        calculateMeanErrors(predictionData);
        vehicleDatabase.closeConnection();
    }

    public static PredictionData containsId(ArrayList<PredictionData> predictionData,final int id){
        return predictionData.stream().filter(vehicle -> id == vehicle.getDeviceId()).findFirst().orElse(null);
    }

    public static void calculateMeanErrors(ArrayList<PredictionData> predictionData){
        predictionData.stream().forEach(vehicle -> {System.out.println("Mean Error of Vehicle "+vehicle.getDeviceId()+" is : "+vehicle.getMeanError());});
    }

}
