package com.tripfriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tripfriend.configuration.Configuration;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.order.OrderActivity;
import com.tripfriend.front.list.ListScheduleActivity;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;

public class MainActivity extends Activity {

    LoadConfiguration loadConfiguration;
    Configuration config;
    Button button_schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = Configuration.getInstance();

        button_schedule = (Button) findViewById(R.id.main_button_schedule);
        button_schedule.setEnabled(false);
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

        if(config.isSet() == false) {
            new AsyncTaskConfig(MainActivity.this).execute();
        } else {
            enableButtons();
        }
    }

    class AsyncTaskConfig extends AsyncTask<String, Void, Boolean> {
        private final Context context;

        public AsyncTaskConfig(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                LoadConfiguration.getInstance(MainActivity.this).loadConfiguration();

                Format dateFormat = DateFormat.getDateFormat(getApplicationContext());
                String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();

                config = Configuration.getInstance();
                config.setDate_format(pattern);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if( result ) {
                enableButtons();
            } else {
                Toast.makeText(context, "Unable to load config. Please restart app.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    public void enableButtons() {
        button_schedule.setEnabled(true);
    }
}
