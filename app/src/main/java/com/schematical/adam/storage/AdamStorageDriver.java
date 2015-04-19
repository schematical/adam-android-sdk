package com.schematical.adam.storage;

import android.app.Activity;
import android.content.Context;

import com.schematical.adam.AdamWorldActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user1a on 4/19/15.
 */
public class AdamStorageDriver {
    protected static  final String FILENAME = "cache_file";
    protected static FileOutputStream outputStream;
    protected static  FileInputStream inputStream;
    public static void init() throws IOException {
        Activity context = AdamWorldActivity.getInstance();

        outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

        outputStream.write("".getBytes());
        outputStream.close();
    }
    public static void write(String strData) throws IOException {
        Activity context = AdamWorldActivity.getInstance();

        outputStream = context.openFileOutput(FILENAME, Context.MODE_APPEND);
        strData += "\n";
        outputStream.write(strData.getBytes());
        outputStream.close();
    }
    public static String read() throws IOException {
        inputStream = AdamWorldActivity.getInstance().openFileInput(FILENAME);
        String rString = new String();
        int content;
        while ((content = inputStream.read()) != -1) {
            // convert to char and display it
            rString += (char) content;
        }
        inputStream.close();
        return rString;
    }
    public static  String[] readAsArray() throws IOException {

        String rString = read();
        return rString.split("\n");

    }

    public static void clear(){
        AdamWorldActivity.getInstance().deleteFile(FILENAME);
    }

}
