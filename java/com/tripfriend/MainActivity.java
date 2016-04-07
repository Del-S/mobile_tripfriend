package com.tripfriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tripfriend.configuration.Configuration;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.order.OrderActivity;
import com.tripfriend.front.list.ListScheduleActivity;

import java.text.Format;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Load this from xml, db or api */
        LoadConfiguration.loadConfiguration();
        Configuration config = LoadConfiguration.getConfig();

        Format dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();

        config.setDate_format(pattern);

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
