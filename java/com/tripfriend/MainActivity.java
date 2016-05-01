package com.tripfriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.tripfriend.configuration.Configuration;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.about.AboutActivity;
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

    Configuration config;
    Button button_schedule, button_view_schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = Configuration.getInstance();

        // Testing
        ImageView imgSlider = new ImageView(MainActivity.this);
        imgSlider.setMinimumHeight(300);
        imgSlider.setScaleType(ImageView.ScaleType.FIT_XY);
        imgSlider.setImageResource(R.drawable.panorama_of_prague_castle);

        ImageSwitcher is = (ImageSwitcher) findViewById(R.id.main_imageSwitcher_slider);
        is.addView(imgSlider);

        TextView tw = new TextView(MainActivity.this);
        tw.setText(getString(R.string.main_slider_content_1));
        tw.setTextColor(getResources().getColor(R.color.colorWhite));
        tw.setGravity(Gravity.CENTER);
        TextSwitcher ts = (TextSwitcher) findViewById(R.id.main_textSwitcher_slider);
        ts.addView(tw);



        button_schedule = (Button) findViewById(R.id.main_button_schedule);
        button_schedule.setEnabled(false);
        button_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        Button button_about = (Button) findViewById(R.id.main_button_about);
        button_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        button_view_schedules = (Button) findViewById(R.id.main_button_list_schedules);
        button_view_schedules.setEnabled(false);
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
        button_view_schedules.setEnabled(true);
    }
}
