import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class esApp {

    public static void main(String[] argv) throws Exception {
        System.out.println("Hello World");
        //convert all 3 xml files to csv
        XMLFileToCSV("InputData/all_vehicles.xml","OutputData/all_vehicles.csv");
        XMLFileToCSV("InputData/vehicle_26.xml","OutputData/vehicle_26.csv");
        XMLFileToCSV("InputData/vehicle_27.xml","OutputData/vehicle_27.csv");

        //produce heat maps
        heatMap heatMapRSSI=new heatMap(37.9668800,37.9686200,23.7647600,23.7753900);
        heatMap heatMapThroughput=new heatMap(37.9668800,37.9686200,23.7647600,23.7753900);
        readCSV("OutputData/all_vehicles.csv",heatMapRSSI,heatMapThroughput);
        combineMapAndHeatMap("InputData/Map.png","OutputData/rssi.png","OutputData/heatmap.png");

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

    static public double getRSSI(){
        double aMean = 60.0f;
        double aVariance = 13.0f;
        Random fRandom = new Random();
        return aMean + fRandom.nextGaussian() * aVariance;

    }

    static public double getThroughput(double rssi, int maxLinkCapacity){
        return rssi*maxLinkCapacity/100;

    }

    static public void XMLFileToCSV(String inputFilename,String outputFilename) throws Exception{
        File inputFile = new File(inputFilename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList timestepList = doc.getElementsByTagName("timestep");

        FileWriter writer = new FileWriter(new File(outputFilename));

        for (int i = 0; i < timestepList.getLength(); i++) {
            Node timestepNode = timestepList.item(i);
            if (timestepNode.getNodeType() == Node.ELEMENT_NODE) {
                Element timestepElement = (Element) timestepNode;
                NodeList vehicleList = timestepElement.getElementsByTagName("vehicle");
                for (int j = 0; j < vehicleList.getLength(); j++) {
                    Node vehicleNode = vehicleList.item(j);

                    if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element vehicleElement = (Element) vehicleNode;
                        double rssi=getRSSI();
                        String newLine = (timestepElement.getAttribute("time")+","+vehicleElement.getAttribute("id")+","+
                                vehicleElement.getAttribute("y")+","+vehicleElement.getAttribute("x")+","+
                                vehicleElement.getAttribute("angle")+","+vehicleElement.getAttribute("speed")+","+
                                rssi+","+getThroughput(rssi,50)+"\n");
                        writer.append(newLine);
                    }
                }
            }
        }
        writer.flush();
        writer.close();
    }

    static public void  readCSV(String csvFilename,heatMap heatMapRSSI,heatMap heatMapThroughput) throws Exception{
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
