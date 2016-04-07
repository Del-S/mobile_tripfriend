package com.cviceni.cv7;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileService extends IntentService {

    /* Todo: Revision !! */

    public DownloadFileService(String name) {
        super(name);
    }

    public DownloadFileService() {
        super("DownloadFileService");
    }

    // Download files
    @Override
    protected void onHandleIntent(Intent intent) {
        String download_url = intent.getStringExtra("download_url");

        try {
            URL url = new URL(download_url);
            URLConnection connection = url.openConnection();
            connection.connect();

            String filename = url.getFile();
            filename = filename.substring(filename.lastIndexOf("/") + 1);

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            OutputStream outputStream = openFileOutput(filename, MODE_PRIVATE);

            byte data[] = new byte[1024];
            int count;
            while ((count = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, count);
            }
            outputStream.close();
            inputStream.close();

            showNotification(true, "Staženo");

        } catch (Exception e) {
            e.printStackTrace();
            showNotification(false, e.getMessage());
        }
    }

    private void showNotification(boolean success, String title) {
        String text;
        if(success) {
            text = "Všechno proběhlo v pořádku.";
        } else {
            text = "Nastala chyba.";
        }

        // Notification in top bar
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_dialog_info);

        // Build and show this notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
