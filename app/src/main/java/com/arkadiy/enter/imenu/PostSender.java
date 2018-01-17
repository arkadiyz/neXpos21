package com.arkadiy.enter.imenu;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by vadnu on 11/01/2018.
 */

public class PostSender extends AsyncTask<String,Void,String> {
    private Callbacks listener;
    private int httpStatusCode;
    private String errorMessage;


    public PostSender(Callbacks listener) {
        this.listener = listener;
    }

    //before it starts
    protected void onPreExecute() {
        listener.onAboutToStart();

    }

    //Runs in separate thread and cannot talk to the UI thread
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... params) {
        int lastPercentReported = 0;
        String result = "";

        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;//----------------------
        OutputStreamWriter outputStreamWriter = null;//----------
        BufferedWriter bufferedWriter = null;//------------------
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;


        try {
            String webPage = "http://81.218.206.83:12985/datasnap/rest/tservermethods1/syncserver/0/admin/admin/1/";
            String name = "admin";
            String password = "admin";

            String authString = name + ":" + password;
            System.out.println("auth string: " + authString);
            String authStringEnc = Base64.getEncoder().encodeToString((name + ":" + password).getBytes(StandardCharsets.UTF_8));
            System.out.println("Base64 encoded auth string: " + authStringEnc);

            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            result = sb.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //    //in the UI thread
    protected void onPostExecute(String downloadedText) {
        if (downloadedText != null) {
            listener.onSuccess(downloadedText);
        } else {
            listener.onError(httpStatusCode, errorMessage);

        }
    }
}



