import org.tc33.jheatchart.HeatChart;

import java.awt.*;
import java.io.File;

public class heatMap {
    int rows;
    int columns;
    double min_lat;
    double max_lat;
    double lat_interval;
    double min_long;
    double max_long;
    double long_interval;
    double [][] gridValues ;
    int [][] gridCounts ;

    public heatMap(double min_lat, double max_lat, double min_long, double max_long) {
        this.rows=4;
        this.columns=10;
        this.min_lat = min_lat;
        this.max_lat = max_lat;
        this.min_long = min_long;
        this.max_long = max_long;
        lat_interval=(max_lat-min_lat)/rows;
        long_interval=(max_long-min_long)/columns;
        gridValues = new double[rows][columns];
        gridCounts = new int[rows][columns];
    }

    public boolean isInGrid(double lat,double lon){
        return (!(lat > max_lat) && !(lat < min_lat)) && (!(lon > max_long) && !(lon < min_long));

    }

    public int getRow(double value){
        return rows-(int)((value-min_lat)/lat_interval)-1;
    }
    
    public int getColumn(double value){
        return (int)((value-min_long)/long_interval);
    }

    public void updateValues(int row,int column,double value){
        //System.out.print("BEFORE: ["+row+","+column+"]"+gridCounts[row][column]+"--"+gridValues[row][column]);
        gridCounts[row][column]++;
        gridValues[row][column]+=value;
        //System.out.println("   AFTER: "+gridCounts[row][column]+"--"+gridValues[row][column]);
    }

    public void produceAverageValues(){
        for(int i=0;i<rows;i++) {
            for (int j = 0; j < columns; j++) {
                gridValues[i][j] = gridCounts[i][j] == 0 ? 0 : gridValues[i][j] / gridCounts[i][j];
            }
        }
    }

    public void heapMapCreator(String filename) throws Exception {
        HeatChart map = new HeatChart(this.gridValues);

        map.setColourScale(13f);
        map.setLowValueColour(Color.RED);
        map.setHighValueColour(Color.GREEN);
        map.setTitle(null);
        map.setXAxisLabel(null);
        map.setYAxisLabel(null);
        map.setShowXAxisValues(false);
        map.setShowYAxisValues(false);
        map.setChartMargin(0);
        map.setAxisThickness(0);
        map.saveToFile(new File(filename));
    }

    public void makeHeatMapPNG(String filename) throws Exception{
        produceAverageValues();
        heapMapCreator(filename);
    }

}
