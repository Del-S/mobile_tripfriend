package com.tripfriend.front.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tripfriend.MainActivity;
import com.tripfriend.R;
import com.tripfriend.configuration.Configuration;
import com.tripfriend.front.Schedule;

public class OrderCompleteActivity extends Activity {

    Schedule schedule;
    Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        setTitle(getResources().getText(R.string.orderC_heading));

        schedule = Schedule.getInstance();
        config = Configuration.getInstance();
        int locationId = schedule.getLocation();
        String location = config.getLocations().get(locationId);

        TextView textViewLove = (TextView) findViewById(R.id.orderC_textView_love);
        String textLove = getString(R.string.orderC_love);
        textViewLove.setText( textLove + " " + location );

        Button buttonFinish = (Button) findViewById(R.id.orderC_button_finish_order);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderCompleteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
