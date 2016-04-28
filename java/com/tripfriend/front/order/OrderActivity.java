package com.tripfriend.front.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tripfriend.R;
import com.tripfriend.configuration.Configuration;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.Schedule;

import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderActivity extends FragmentActivity implements OrderDialogFragment.OnCompleteODFListener {

    Configuration config;
    Schedule schedule;
    Button buttonDate;
    Button buttonTime;
    Calendar scheduled_date;
    OrderDialogFragment dialogFragment;
    TextView locationView, languageView, timespanView;
    Boolean sDate, sTime, sLocation, sLanguage, sTimespan;
    List<String> schedule_preference;
    DateFormat df;
    LoadConfiguration loadConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        config = Configuration.getInstance();
        schedule = Schedule.getInstance();
        scheduled_date = schedule.getCalendar_start();
        dialogFragment = new OrderDialogFragment();
        df = new SimpleDateFormat(config.getDate_format());

        sDate = true;
        sTime = true;
        sLocation = true;
        sLanguage = true;
        sTimespan = true;

        if(scheduled_date == null) {
            scheduled_date = Calendar.getInstance();
            sDate = false;
            sTime = false;
        }
        if(schedule.getLocation() == -1) {
            sLocation = false;
        }
        if(schedule.getLanguage() == -1) {
            sLanguage = false;
        }
        if(schedule.getTime_span() == -1) {
            sTimespan = false;
        }

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
        if(schedule.getLocation() != -1) {
            locationView.setText((String) config.getLocations().get(schedule.getLocation()));
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("locations", config.getLocations());
                bundle.putInt("locations_selected", schedule.getLocation());
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getSupportFragmentManager(), "locationPicker");
            }
        });

        LinearLayout language = (LinearLayout) findViewById(R.id.order_layout_language);
        languageView = (TextView) findViewById(R.id.order_textView_language_item);
        if(schedule.getLanguage() != -1) {
            languageView.setText((String) config.getLanguages().get(schedule.getLanguage()));
        }

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("languages", config.getLanguages());
                bundle.putInt("languages_selected", schedule.getLanguage());
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getSupportFragmentManager(), "languagePicker");
            }
        });

        LinearLayout timespan = (LinearLayout) findViewById(R.id.order_layout_timespan);
        timespanView = (TextView) findViewById(R.id.order_textView_timespan_item);
        if(schedule.getTime_span() != -1) {
            timespanView.setText((String) config.getTime_spans().get(schedule.getTime_span()));
        }

        timespan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("timespans", config.getTime_spans());
                bundle.putInt("timespans_selected", schedule.getTime_span());
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getSupportFragmentManager(), "timespanPicker");
            }
        });
    }

    private void loadDateTime() {
        buttonDate = (Button) findViewById(R.id.order_button_datepicker);
        buttonTime = (Button) findViewById(R.id.order_button_timepicker);
        if(schedule.getCalendar_start() != null) {
            Integer minute = scheduled_date.get(Calendar.MINUTE);
            Integer hour = scheduled_date.get(Calendar.HOUR);
            String bText = String.format("%02d",hour) + ":" + String.format("%02d",minute);
            buttonTime.setText(bText);
            buttonDate.setText(df.format(scheduled_date.getTime()));
        }

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
        schedule_preference = schedule.getPreferences();

        for( int i = 0; i < preferences_data.size(); i++ ) {
            String preference = preferences_data.get(i);

            CheckBox preferenceCheckBox = new CheckBox(getApplicationContext());
            preferenceCheckBox.setText(preference);
            if(schedule_preference.contains(preference)) {
                preferenceCheckBox.setChecked(true);
            }
            preferenceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        schedule_preference.add(buttonView.getText().toString());
                    }else{
                        schedule_preference.remove(buttonView.getText().toString());
                    }
                }
            });
            preferences.addView(preferenceCheckBox);
        }

    }

    @Override
    public void onDatePicked(int year, int monthOfYear, int dayOfMonth) {
        /* Save Date from datepicker */
        scheduled_date.set(Calendar.YEAR, year);
        scheduled_date.set(Calendar.MONTH, monthOfYear);
        scheduled_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        schedule.setCalendar_start(scheduled_date);
        sDate = true;

        buttonDate.setText(df.format(scheduled_date.getTime()));
    }

    @Override
    public void onTimePicked(int hour, int minute) {
        /* Save Time from timepicker */
        scheduled_date.set(Calendar.HOUR, hour);
        scheduled_date.set(Calendar.MINUTE, minute);
        schedule.setCalendar_start(scheduled_date);
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
            schedule.setPreferences(schedule_preference);
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
