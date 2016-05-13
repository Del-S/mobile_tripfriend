package com.tripfriend;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tripfriend.configuration.Configuration;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.Schedule;
import com.tripfriend.front.about.AboutActivity;
import com.tripfriend.front.order.OrderActivity;
import com.tripfriend.front.list.ListScheduleActivity;

import org.json.JSONException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class MainActivity extends Activity {

    Configuration config;
    Button button_schedule, button_view_schedules;
    ImageSwitcher imgSlider;
    TextSwitcher textSlider;
    String[] imgIds;
    int imgCount = 0;
    int currImgID = -1;
    final int textIDs[] = { R.string.main_slider_content_1, R.string.main_slider_content_2, R.string.main_slider_content_3, R.string.main_slider_content_4 };
    int textCount = textIDs.length;
    int currTextID = -1;
    final String[] ext = { "gif", "png", "bmp", "jpg" };
    boolean userData = false;

    // TODO:
    // Block return from Complete order
    // view schedules by email
    // date and time starts at current time and not picked time (only on nth pick)
    // fill up data about tripfrined
    // add images to slider and try to do automatic switches
    // complete the styling for all activities
    // Extra: map for pickup location?
    // Extra: Loading dialog with loading spinner :)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = Configuration.getInstance();
        if(!userData) { loadUserData(); }
        loadSlider();

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

        if(!config.isSet()) {
            new AsyncTaskConfig(MainActivity.this).execute();
        } else {
            enableButtons();
        }
    }

    private void loadUserData() {
        Schedule s = Schedule.getInstance();
        SharedPreferences sp = getSharedPreferences("TripFriend", 0);

        s.setName( sp.getString("name", "") );
        s.setSurname( sp.getString("surname", "") );
        s.setEmail( sp.getString("email", "") );
        s.setPhone_number( sp.getString("phone", "") );

        userData = true;
    }

    private void loadSlider() {
        // Load all images
        try {
            List<String> images = new ArrayList<>();
            String[] files = getAssets().list("slider");
            for (String f : files) {
                if( checkExtenstion(f) ) {
                    images.add(f);
                }
            }
            imgIds = images.toArray(new String[images.size()]);
            imgCount = imgIds.length;
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgSlider = (ImageSwitcher) findViewById(R.id.main_imageSwitcher_slider);
        imgSlider.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.WRAP_CONTENT, ImageSwitcher.LayoutParams.WRAP_CONTENT));
                return imageView;
            }
        });
        imgSlider.setInAnimation(this, android.R.anim.fade_in);
        imgSlider.setOutAnimation(this, android.R.anim.fade_out);

        textSlider = (TextSwitcher) findViewById(R.id.main_textSwitcher_slider);
        textSlider.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tw = new TextView(getApplicationContext());
                tw.setTextColor(getResources().getColor(R.color.colorWhite));
                tw.setGravity(Gravity.CENTER);
                return tw;
            }
        });
        textSlider.setInAnimation(this, android.R.anim.fade_in);
        textSlider.setOutAnimation(this, android.R.anim.fade_out);

        updateSliderNext();

        ImageButton buttonSliderPrev = (ImageButton) findViewById(R.id.main_button_slider_prev);
        buttonSliderPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSliderPrev();
            }
        });

        ImageButton buttonSliderNext = (ImageButton) findViewById(R.id.main_button_slider_next);
        buttonSliderNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSliderNext();
            }
        });
    }

    private boolean checkExtenstion(String name) {
        for (String extS : ext) {
            if (name.endsWith("." + extS)) {
                return (true);
            }
        }
        return (false);
    }

    private void enableButtons() {
        button_schedule.setEnabled(true);
        button_view_schedules.setEnabled(true);
    }

    private void updateSliderNext() {
        currImgID++;
        currTextID++;
        if( (currImgID == imgCount) && (imgCount != 0) ) {
            currImgID = 0;
        }
        if( (currTextID == textCount) && (textCount != 0) ) {
            currTextID = 0;
        }

        try {
            InputStream is = getAssets().open("slider/"+imgIds[currImgID]);
            Drawable d = Drawable.createFromStream(is, null);
            imgSlider.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textSlider.setText(getString(textIDs[currTextID]));
    }

    private void updateSliderPrev() {
        currImgID--;
        currTextID--;
        if( (currImgID < 0) && (imgCount != 0) ) {
            currImgID = (imgIds.length - 1);
        }
        if( (currTextID < 0) && (textCount != 0) ) {
            currTextID = (textIDs.length - 1);
        }

        try {
            InputStream is = getAssets().open("slider/"+imgIds[currImgID]);
            Drawable d = Drawable.createFromStream(is, null);
            imgSlider.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textSlider.setText(getString(textIDs[currTextID]));
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
}
