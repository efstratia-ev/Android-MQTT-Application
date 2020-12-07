package HeatMapCreation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import HeatMapCreation.HeatMap;


public class HeatMapUtil {
    public static void createHeatMap() throws Exception {
        //convert all 3 xml files to csv
        CreateCSV.XMLFileToCSV("InputData/all_vehicles.xml","OutputData/all_vehicles.csv");
        CreateCSV.XMLFileToCSV("InputData/vehicle_26.xml","OutputData/vehicle_26.csv");
        CreateCSV.XMLFileToCSV("InputData/vehicle_27.xml","OutputData/vehicle_27.csv");

        //produce heat maps
        HeatMap heatMapRSSI=new HeatMap(37.9668800,37.9686200,23.7647600,23.7753900);
        HeatMap heatMapThroughput=new HeatMap(37.9668800,37.9686200,23.7647600,23.7753900);
        HeatMapUtil.readCSV("OutputData/all_vehicles.csv",heatMapRSSI,heatMapThroughput);
        HeatMapUtil.combineMapAndHeatMap("InputData/Map.png","OutputData/rssi.png","OutputData/heatmapRSSI.png");
        HeatMapUtil.combineMapAndHeatMap("InputData/Map.png","OutputData/throughput.png","OutputData/heatmapThroughput.png");
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

    static public void  readCSV(String csvFilename, HeatMap heatMapRSSI, HeatMap heatMapThroughput) throws Exception{
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
}
