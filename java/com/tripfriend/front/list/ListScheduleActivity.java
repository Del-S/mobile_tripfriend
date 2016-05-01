package com.tripfriend.front.list;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.tripfriend.R;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.Friend;
import com.tripfriend.front.Schedule;
import com.tripfriend.front.order.PickFriendAdapter;

import java.util.List;

public class ListScheduleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule);

        setTitle(getResources().getText(R.string.list_schedule_heading));
        LoadConfiguration loadConfiguration = LoadConfiguration.getInstance(ListScheduleActivity.this);


        ListView listViewSchedules = (ListView) findViewById(R.id.list_schedule_listview);
        List<Schedule> schedules = loadConfiguration.getSchedulesDummy();
        ListScheduleAdapter schedulesAdapter = new ListScheduleAdapter(this, R.layout.item_schedule, schedules);
        listViewSchedules.setAdapter(schedulesAdapter);
        schedulesAdapter.notifyDataSetChanged();
    }
}
