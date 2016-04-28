package com.tripfriend.front.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tripfriend.R;
import com.tripfriend.configuration.Configuration;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.Friend;
import com.tripfriend.front.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderPickFriendActivity extends Activity implements AdapterView.OnItemClickListener {

    List<Friend> friends;
    ListView listViewFriends;
    Schedule schedule;
    Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pick_friend);

        config = Configuration.getInstance();
        schedule = Schedule.getInstance();
        friends = config.getFriends();
        listViewFriends = (ListView) findViewById(R.id.orderF_friends);

        /* Add adapter to friendView */
        PickFriendAdapter friendAdapter = new PickFriendAdapter(this, R.layout.item_friend, getAvailableFriends());
        listViewFriends.setAdapter(friendAdapter);
        listViewFriends.setOnItemClickListener(this);
        friendAdapter.notifyDataSetChanged();
    }

    private List<Friend> getAvailableFriends() {
        // This will be pointed to the internet api

        return config.getFriends();
        //return new ArrayList<Friend>();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Friend friend = (Friend) parent.getItemAtPosition(position);

        schedule.setId_friend(friend.getId());
        Intent intent = new Intent(this, OrderUserInfoActivity.class);
        startActivity(intent);
    }
}
