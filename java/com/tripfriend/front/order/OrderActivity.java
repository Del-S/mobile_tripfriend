package com.tripfriend.front.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tripfriend.R;
import com.tripfriend.configuration.Configuration;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.Schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements OrderDialogFragment.OnCompleteODFListener {

    Configuration config;
    Schedule schedule;
    Button buttonDate;
    Button buttonTime;
    Calendar scheduled_date;
    OrderDialogFragment dialogFragment;
    TextView locationView, languageView, timespanView;
    Boolean sDate, sTime, sLocation, sLanguage, sTimespan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        config = LoadConfiguration.getConfig();
        schedule = new Schedule();
        scheduled_date = Calendar.getInstance();
        dialogFragment = new OrderDialogFragment();
        sDate = false;
        sTime = false;
        sLocation = false;
        sLanguage = false;
        sTimespan = false;

        loadSelects();
        loadDateTime();
        loadPreferences();

        Button pickFriend = (Button) findViewById(R.id.order_button_pick_friend);
        pickFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkInputs() ) {
                    Intent intent = new Intent(OrderActivity.this, OrderPickFriendActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(OrderActivity.this, "Error you don't have all data filled (parametrize this)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadSelects() {
        LinearLayout location = (LinearLayout) findViewById(R.id.order_layout_location);
        locationView = (TextView) findViewById(R.id.order_textView_location_item);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("locations", new ArrayList<String>(config.getLocations()));
                bundle.putInt("locations_selected", config.getLocations().indexOf(locationView.getText()));
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getSupportFragmentManager(), "locationPicker");
            }
        });

        LinearLayout language = (LinearLayout) findViewById(R.id.order_layout_language);
        languageView = (TextView) findViewById(R.id.order_textView_language_item);

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("languages", new ArrayList<String>(config.getLanguages()));
                bundle.putInt("languages_selected", config.getLanguages().indexOf(languageView.getText()));
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getSupportFragmentManager(), "languagePicker");
            }
        });

        LinearLayout timespan = (LinearLayout) findViewById(R.id.order_layout_timespan);
        timespanView = (TextView) findViewById(R.id.order_textView_timespan_item);

        timespan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("timespans", new ArrayList<String>(config.getTime_spans()));
                bundle.putInt("timespans_selected", config.getTime_spans().indexOf(timespanView.getText()));
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getSupportFragmentManager(), "timespanPicker");
            }
        });
    }

    private void loadDateTime() {
        buttonDate = (Button) findViewById(R.id.order_button_datepicker);
        buttonTime = (Button) findViewById(R.id.order_button_timepicker);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
    }

    private void loadPreferences() {
        LinearLayout preferences = (LinearLayout) findViewById(R.id.order_layout_preferences);
        List<String> preferences_data = config.getPreferences();

        for( int i = 0; i < preferences_data.size(); i++  ) {
            String preference = preferences_data.get(i);

            CheckBox preferenceCheckBox = new CheckBox(getApplicationContext());
            preferenceCheckBox.setText(preference);
            preferences.addView(preferenceCheckBox);
        }

    }

    @Override
    public void onDatePicked(int year, int monthOfYear, int dayOfMonth) {
        /* Save Date from datepicker */
        scheduled_date.set(Calendar.YEAR, year);
        scheduled_date.set(Calendar.MONTH, monthOfYear);
        scheduled_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        sDate = true;

        DateFormat df = new SimpleDateFormat(config.getDate_format());
        buttonDate.setText(df.format(scheduled_date.getTime()));
    }

    @Override
    public void onTimePicked(int hour, int minute) {
        /* Save Time from timepicker */
        scheduled_date.set(Calendar.HOUR, hour);
        scheduled_date.set(Calendar.MINUTE, minute);
        sTime = true;

        String bText = String.format("%02d",hour) + ":" + String.format("%02d",minute);
        buttonTime.setText(bText);
    }

    @Override
    public void onLocationPicked(int location) {
        schedule.setLocation(location);
        sLocation = true;
        locationView.setText((String) config.getLocations().get(location));
    }

    @Override
    public void onLanguagePicked(int language) {
        schedule.setLanguage(language);
        sLanguage = true;
        languageView.setText((String) config.getLanguages().get(language));
    }

    @Override
    public void onTimespanPicked(int timespan) {
        schedule.setTime_span(timespan);
        sTimespan = true;
        timespanView.setText((String) config.getTime_spans().get(timespan));
    }

    private boolean checkInputs() {
        /* Check all inputs */
        if( sDate && sTime && sLocation && sLanguage && sTimespan ) {
            schedule.setCalendar_start(scheduled_date);
            return true;
        }
        if (!sDate) { /* styling error in date */ }
        if (!sTime) { /* styling error in date */ }
        if (!sLocation) { /* styling error in date */ }
        if (!sLanguage) { /* styling error in date */ }
        if (!sTimespan) { /* styling error in date */ }
        return false;
    }
}
