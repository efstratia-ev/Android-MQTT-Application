package com.example.myapplication.Utilities;

import android.content.Context;
import android.widget.Toast;
import com.example.myapplication.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MyCSVReader {
    int linesNumber;
    CSVReader reader;
    String path;
    public MyCSVReader(Context context){
        path = context.getExternalFilesDir(null).toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        String name="";
        for (int i = 0; i < files.length; i++) {
            if(files[i].getName().startsWith("vehicle")){
                name=files[i].getName();
                break;
            }
        }
        linesNumber=0;
        path+="/"+name;
        try {
            reader = new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        String[] line;
        MainActivity.markersArray = new ArrayList<MarkerOptions>();
        while (true) {
            try {
                line=reader.readNext();
            } catch (IOException e) {
                e.printStackTrace();
                linesNumber=0;
                break;
            }
            if(line == null) break;
            if(linesNumber==0)
                MainActivity.TerminalID = Integer.parseInt(line[1]);
            else
                MainActivity.markersArray.add(createMarker(Double.parseDouble(line[2]),Double.parseDouble(line[3]),line[0],"RSSI:"+line[6]+"\nThroughput: "+line[7]));
            linesNumber++;
        }

    }

    protected MarkerOptions createMarker(double latitude, double longitude, String title, String snippet) {
        return new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet);
    }

    public String readLine() throws IOException {
        return Arrays.toString(reader.readNext());
    }

    public void resetFile() throws IOException {
        if(reader!=null) reader.close();
        reader = new CSVReader(new FileReader(path));
    }

    public int getLinesNumber(){
        return linesNumber;
    }
}
