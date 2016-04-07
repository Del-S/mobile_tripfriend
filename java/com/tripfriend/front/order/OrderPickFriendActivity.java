package com.tripfriend.front.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.tripfriend.R;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.Friend;

import java.util.List;

public class OrderPickFriendActivity extends AppCompatActivity {

    List<Friend> friends;
    ListView listViewFriends;
    Button buttonOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pick_friend);

        friends = LoadConfiguration.getFriends();
        listViewFriends = (ListView) findViewById(R.id.orderF_friends);
        buttonOrder = (Button) findViewById(R.id.orderF_button_order);

        // Add adapter with image, name, lanfuages (flags) and checkbox
        //listViewFriends.setAdapter();

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkFriend() ) {
                    // intent and move
                } else {
                   // Toast message to order friend
                }
            }
        });
    }

    public boolean checkFriend() {
        return true;
    }
}
