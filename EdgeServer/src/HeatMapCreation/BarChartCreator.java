package HeatMapCreation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarChartCreator extends Application {

    @Override public void start(Stage stage) throws Exception{

        Parameters params = getParameters();
        List<String> vehicles = Arrays.asList(params.getNamed().get("vehicles").split("\\s*,\\s*"));
        List<String> meanErrors = Arrays.asList(params.getNamed().get("meanErrors").split("\\s*,\\s*"));

        stage.setTitle("Mean Errors Values");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        bc.setBarGap(15);
        //bc.setCategoryGap(20);
        bc.setTitle("Predicted Data Mean Errors");
        xAxis.setLabel("Vehicles");
        yAxis.setLabel("Mean Error(m)");

        XYChart.Series series1 = new XYChart.Series();
        for(int i=0;i<vehicles.size();i++){
            series1.getData().add(new XYChart.Data(vehicles.get(i),Double.valueOf(meanErrors.get(i))));
        }

        Scene scene  = new Scene(bc,300,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();

    }

}