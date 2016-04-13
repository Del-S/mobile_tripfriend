package com.tripfriend.front.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tripfriend.R;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.Friend;
import com.tripfriend.front.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderPickFriendActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    List<Friend> friends;
    ListView listViewFriends;
    Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pick_friend);

        schedule = Schedule.getInstance();
        friends = LoadConfiguration.getFriends();
        listViewFriends = (ListView) findViewById(R.id.orderF_friends);

        /* Add adapter to friendView */
        PickFriendAdapter friendAdapter = new PickFriendAdapter(this, R.layout.item_friend, getAvailableFriends());
        listViewFriends.setAdapter(friendAdapter);
        listViewFriends.setOnItemClickListener(this);
        friendAdapter.notifyDataSetChanged();
    }

    private List<Friend> getAvailableFriends() {
        List<Friend> allFriends = LoadConfiguration.getFriends();
        List<Friend> availableFriends = new ArrayList<Friend>();
        for(int i = 0; allFriends.size() > i; i++) {
            Calendar startfriendCalendar = schedule.getCalendar_start();

            allFriends
        }
        return availableFriends;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Friend friend = (Friend) parent.getItemAtPosition(position);

        schedule.setId_friend(friend.getId());
        Intent intent = new Intent(this, OrderUserInfoActivity.class);
        startActivity(intent);
    }
}
