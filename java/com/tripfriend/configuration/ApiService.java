package com.tripfriend.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.tripfriend.front.list.ListScheduleActivity;
import com.tripfriend.front.list.ListScheduleAdapter;
import com.tripfriend.front.order.OrderActivity;
import com.tripfriend.front.order.OrderUserInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ApiService {
    private Context context;
    private int mode;

    public ApiService(Context context) {
        this.context = context;
    }

    public ApiService() {}

    public void sendPost(int mode, String urlString, JSONObject jsonSend) throws IOException, JSONException {
        AsyncTaskApi asyncTaskApi = new AsyncTaskApi(context);
        this.mode = mode;
        asyncTaskApi.execute(urlString, jsonSend.toString());
    }

    public JSONObject sendPostApi(String urlString, String jsonSendString) throws IOException, JSONException {
        URL url = new URL(urlString);
        String result = "";

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        //connection.setReadTimeout(10000 /*milliseconds*/);
        //connection.setConnectTimeout(15000 /* milliseconds */);
        //connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        connection.connect();

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonSendString);
        writer.close();
        os.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            result = sb.toString();
        }
        connection.disconnect();
        return new JSONObject(result);
    }

    public JSONObject getJsonObject(String urlString) throws IOException, JSONException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStreamReader in = new InputStreamReader(connection.getInputStream());

        StringBuilder jsonResult = new StringBuilder();

        int read;
        char[] buff = new char[1024];

        while((read = in.read(buff)) != -1) {
            jsonResult.append(buff, 0, read);
        }

        return new JSONObject(jsonResult.toString());
    }

    public List<String> downloadFiles(String[] downloadUrls) {
        List<String> downloads = new ArrayList<>();
        for(String downloadUrl : downloadUrls) {
            String downloaded_file = downloadFile( downloadUrl );
            downloads.add(downloaded_file);
        }
        return downloads;
    }

    public String downloadFile(String downloadUrl) {
        // Alter url from localhost to 10.0.2.2 for localhost only
        if(downloadUrl.contains("localhost")) {
            downloadUrl = downloadUrl.replace("localhost", "10.0.2.2");
        }

        File dir = context.getFilesDir();
        boolean download = true;
        String filename = downloadUrl;
        filename = filename.substring(filename.lastIndexOf("/") + 1);
        for(File file : dir.listFiles()) {
            if( file.getName().equals(filename) ) {
                download = false;
                break;
            }
        }

        if(download) {
            Intent intent = new Intent(context, com.tripfriend.configuration.DownloadFileService.class);
            intent.putExtra("download_url", downloadUrl);

            context.startService(intent);
        }
        return filename;
    }

    class AsyncTaskApi extends AsyncTask<String, Void, JSONObject> {
        private final Context context;

        public AsyncTaskApi(Context context){
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                return sendPostApi(params[0], params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if( result != null ) {
                returnAsyncData(result);
            } else {
                Toast.makeText(context, "Unable to load api data. Please try again or restart app.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    public void returnAsyncData(JSONObject result) {
        switch (mode) {
            case 0:
                OrderActivity oa = (OrderActivity) context;
                oa.completeActivity(result);
                break;
            case 1:
                OrderUserInfoActivity ouia = (OrderUserInfoActivity) context;
                ouia.completeActivity(result);
                break;
            case 2:
                ListScheduleActivity lsa = (ListScheduleActivity) context;
                lsa.parseData(result);
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
