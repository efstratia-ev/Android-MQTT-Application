

class Project{
    public static void main(String[] argv) throws Exception {
        //convert all 3 xml files to csv
        esApp.XMLFileToCSV("InputData/all_vehicles.xml","OutputData/all_vehicles.csv");
        esApp.XMLFileToCSV("InputData/vehicle_26.xml","OutputData/vehicle_26.csv");
        esApp.XMLFileToCSV("InputData/vehicle_27.xml","OutputData/vehicle_27.csv");

        //produce heat maps
        heatMap heatMapRSSI=new heatMap(37.9668800,37.9686200,23.7647600,23.7753900);
        heatMap heatMapThroughput=new heatMap(37.9668800,37.9686200,23.7647600,23.7753900);
        esApp.readCSV("OutputData/all_vehicles.csv",heatMapRSSI,heatMapThroughput);
        esApp.combineMapAndHeatMap("InputData/Map.png","OutputData/rssi.png","OutputData/heatmapRSSI.png");
        esApp.combineMapAndHeatMap("InputData/Map.png","OutputData/throughput.png","OutputData/heatmapThroughput.png");

        MQTTConnection MQTTConnection = new MQTTConnection();
    }
}

