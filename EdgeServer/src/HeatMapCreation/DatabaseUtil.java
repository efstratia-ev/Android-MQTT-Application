package HeatMapCreation;

import Predictions.PredictionData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtil {
    Connection conn = null;
    final String schema = "mydb";
    final String user = "root";
    final String password = "root";


    public DatabaseUtil() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        conn = DriverManager.getConnection("jdbc:mysql://localhost/"+schema+"?user="+user+"&password="+password);
    }

    public void insert(PredictionData predictionData) throws SQLException {
        // the mysql insert statement
        String query = " insert into Vehicle (device_id,timestep,real_lat,real_long,predicted_lat,predicted_long," +
                "real_rsssi,real_throughput,predicted_rssi,predicted_throughput)"
                + " values (?, ?, ?, ?, ?,?, ?, ?, ?, ?)";

        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1, predictionData.getDeviceId());
        preparedStmt.setDouble(2, predictionData.getTimeStep() );
        preparedStmt.setDouble(3, predictionData.getRealLat());
        preparedStmt.setDouble(4, predictionData.getRealLong());
        preparedStmt.setDouble(5, predictionData.getPredictedLat());
        preparedStmt.setDouble(6, predictionData.getPredictedLong());
        preparedStmt.setDouble(7, predictionData.getRealRssi());
        preparedStmt.setDouble(8, predictionData.getRealThroughput());
        preparedStmt.setDouble(9, predictionData.getPredictedRssi());
        preparedStmt.setDouble(10, predictionData.getPredictedThroughput());

        // execute the preparedstatement
        preparedStmt.execute();
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }


}

