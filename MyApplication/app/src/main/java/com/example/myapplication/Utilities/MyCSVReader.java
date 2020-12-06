package com.example.myapplication.Utilities;

import android.content.Context;
import android.widget.Toast;
import com.example.myapplication.MainActivity;
import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Objects;

public class MyCSVReader {
    int linesNumber;
    CSVReader reader;
    String path;
    public MyCSVReader(Context context){
        path = context.getExternalFilesDir(null).toString()+"/vehicle_26.csv";
        try {
            reader = new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        linesNumber=0;
        while (true) {
            try {
                if (reader.readNext() == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            linesNumber++;
        }

    }

    public String readLine() throws IOException {
        String[] a=reader.readNext();
        return Arrays.toString(a);
    }

    public void resetFile() throws IOException {
        reader.close();
        reader = new CSVReader(new FileReader(path));
    }

    public int getLinesNumber(){
        return linesNumber;
    }
}
