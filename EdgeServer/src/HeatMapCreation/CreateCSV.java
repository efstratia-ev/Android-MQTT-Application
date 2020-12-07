package HeatMapCreation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class CreateCSV {
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
}
