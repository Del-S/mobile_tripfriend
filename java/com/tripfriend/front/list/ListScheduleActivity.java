package com.tripfriend.front.list;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.tripfriend.R;
import com.tripfriend.configuration.ApiService;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.Friend;
import com.tripfriend.front.Schedule;
import com.tripfriend.front.order.OrderCompleteActivity;
import com.tripfriend.front.order.PickFriendAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListScheduleActivity extends FragmentActivity implements ListDialogFragment.OnCompleteTListener {

    ListDialogFragment dialogFragment;
    LoadConfiguration loadConfiguration;
    List<Schedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule);

        setTitle(getResources().getText(R.string.list_schedule_heading));
        loadConfiguration = LoadConfiguration.getInstance(ListScheduleActivity.this);

        SharedPreferences sp = getSharedPreferences("TripFriend", 0);
        String email = sp.getString("email", "");

        // Display only if email is not in SharedPreferences
        // Else it will load AsyncTask
        if(email.equals("")) {
            dialogFragment = new ListDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("titleDialog", getString(R.string.list_dialog_title));
            bundle.putSerializable("headingDialog", getString(R.string.list_dialog_heading));
            bundle.putSerializable("hintEditTextDialog", getString(R.string.list_dialog_edit_text_hint));
            dialogFragment.setArguments(bundle);

            dialogFragment.show(getSupportFragmentManager(), "emailSet");
        } else {
            getSchedules(email);
        }
    }

    @Override
    public void onEmailSet(String email) {
        // save to SharedPreferences
        SharedPreferences sp = getSharedPreferences("TripFriend", 0);
        SharedPreferences.Editor se = sp.edit();
        se.putString("email", email);
        se.apply();

        getSchedules(email);
    }

    private void getSchedules(String email) {
        if(loadConfiguration != null) {
            try {
                loadConfiguration.getSchedules(email);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displaySchedules() {
        ListView listViewSchedules = (ListView) findViewById(R.id.list_schedule_listview);
        ListScheduleAdapter schedulesAdapter = new ListScheduleAdapter(this, R.layout.item_schedule, schedules);
        listViewSchedules.setAdapter(schedulesAdapter);
        schedulesAdapter.notifyDataSetChanged();
    }

    public void parseData(JSONObject result) {
        schedules = new ArrayList<>();
        try {
            JSONObject schedulesObject = result.getJSONObject("shedules");

            JSONArray ids = schedulesObject.names();
            for(int i = 0; schedulesObject.length() > i; i++ ) {
                String idString = (String) ids.get(i);
                JSONObject scheduleObject = schedulesObject.getJSONObject(idString);

                int location = scheduleObject.getInt("location_id");
                int language = scheduleObject.getInt("service_id");
                int id_friend = scheduleObject.getInt("staff_id");
                int time_span = scheduleObject.getInt("timespan");
                int group = scheduleObject.getInt("group");

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String date = scheduleObject.getString("date");
                Calendar calendar_start = Calendar.getInstance();
                calendar_start.setTime( dateFormat.parse(date) );

                String name = scheduleObject.getString("name");
                String surname = scheduleObject.getString("surname");
                String email = scheduleObject.getString("email");
                String phone_number = scheduleObject.getString("phone");
                String pickup_location = scheduleObject.getString("pickup_location");
                String notes = scheduleObject.getString("notes");

                JSONArray preferencesArray = scheduleObject.getJSONArray("preferences");
                JSONArray availableFriendsArray = scheduleObject.getJSONArray("availableFriends");
                List<String> preferences = loadConfiguration.parseSingleArrayNoID(preferencesArray);
                List<String> availableFriends = loadConfiguration.parseSingleArrayNoID(availableFriendsArray);

                int id = Integer.valueOf(idString);
                Schedule s = new Schedule(id, location, language, time_span, id_friend, group, calendar_start, name, surname, email, phone_number, pickup_location, notes, preferences, availableFriends);

                schedules.add(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
