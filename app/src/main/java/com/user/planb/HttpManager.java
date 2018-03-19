package com.user.planb;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 1/20/2018.
 */

public class HttpManager {

    public static String getData(RequestPackage requestPackage){
        BufferedReader reader  = null;
        String uri = requestPackage.getUri();
        if(requestPackage.getMethod().equals("GET")){
            uri += "?" + requestPackage.getEncodedParams();
        }

        Log.e("pradeepplanb","pradeepplanb "+uri);
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod(requestPackage.getMethod());
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            httpURLConnection.setConnectTimeout(100*1000);
            if(requestPackage.getMethod().equals("POST")){
                httpURLConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                outputStreamWriter.write(requestPackage.getEncodedParams());
                outputStreamWriter.flush();
            }
            StringBuilder stringBuilder = new StringBuilder();
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode(); //can call this instead of con.connect()
            if (responseCode >= 400 && responseCode <= 499) {
                throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
            }
            else {
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            }
            String data;
            while ((data=reader.readLine()) != null){
                stringBuilder.append(data + "\n");
            }
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
