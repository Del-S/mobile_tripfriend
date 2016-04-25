package com.tripfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tripfriend.configuration.Configuration;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.order.OrderActivity;
import com.tripfriend.front.list.ListScheduleActivity;

import org.json.JSONException;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;

public class MainActivity extends Activity {

    LoadConfiguration loadConfiguration;
    Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadConfiguration = LoadConfiguration.getInstance();

        // Todo: wait fot his async task to finish - or do on finish enable ordering
        if(config == null) {
            new AsyncTask<String, Void, Configuration>() {
                @Override
                protected Configuration doInBackground(String... params) {
                    try {
                        loadConfiguration.loadConfiguration();
                        config = loadConfiguration.getConfig();

                        Format dateFormat = DateFormat.getDateFormat(getApplicationContext());
                        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();

                        config.setDate_format(pattern);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

        Button button_schedule = (Button) findViewById(R.id.main_button_schedule);
        button_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        Button button_view_schedules = (Button) findViewById(R.id.main_button_list_schedules);
        button_view_schedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListScheduleActivity.class);
                startActivity(intent);
            }
        });
    }
}
