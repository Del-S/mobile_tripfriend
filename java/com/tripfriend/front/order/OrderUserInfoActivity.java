package com.tripfriend.front.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tripfriend.R;
import com.tripfriend.configuration.LoadConfiguration;
import com.tripfriend.front.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class OrderUserInfoActivity extends Activity {

    // TODO: revision and send asyncTask to schedule to create apointment :)
    LoadConfiguration loadConfiguration;
    Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_user_info);

        loadConfiguration = LoadConfiguration.getInstance(OrderUserInfoActivity.this);
        schedule = Schedule.getInstance();

        Button finish_registration = (Button) findViewById(R.id.orderU_finish);
        finish_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()) {
                    try {
                        loadConfiguration.completeOrder();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(OrderUserInfoActivity.this, "Fill all fields (parametrize)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void completeActivity(JSONObject result) {
        try {
            System.out.println(result.toString());
            String status = result.getString("status");

            if(status.equals( "success" )) {
                Intent intent = new Intent(OrderUserInfoActivity.this, OrderCompleteActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(OrderUserInfoActivity.this, "Schedule was not created.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean checkInputs() {
        Boolean ret = true;

        EditText nameEditText = (EditText) findViewById(R.id.orderU_name);
        String name = nameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Please enter your name.");
            ret = false;
        } else {
            if(name.split("\\w+").length>1){
                schedule.setName( name.substring(0, name.lastIndexOf(' ')));
                schedule.setSurname( name.substring(name.lastIndexOf(" ") + 1) );
            } else {
                schedule.setName( name );
                schedule.setSurname( name );
            }
        }

        EditText emailEditText = (EditText) findViewById(R.id.orderU_email);
        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter your email.");
            ret = false;
        } else {
            schedule.setEmail( email );
        }

        EditText phoneEditText = (EditText) findViewById(R.id.orderU_phone);
        String phone = phoneEditText.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Please enter your phone number.");
            ret = false;
        } else {
            schedule.setPhone_number(phone);
        }

        EditText groupEditText = (EditText) findViewById(R.id.orderU_group);
        String group = groupEditText.getText().toString();
        if (TextUtils.isEmpty(group)) {
            groupEditText.setError("Please enter number of people.");
            ret = false;
        } else {
            schedule.setGroup(Integer.valueOf(group));
        }

        EditText pickupEditText = (EditText) findViewById(R.id.orderU_pickup_location);
        String pickup_location = pickupEditText.getText().toString();
        if (TextUtils.isEmpty(pickup_location)) {
            pickupEditText.setError("Please enter pickup location.");
            ret = false;
        } else {
            schedule.setPickup_location( pickup_location );
        }

        EditText notesEditText = (EditText) findViewById(R.id.orderU_notes);
        String notes = notesEditText.getText().toString();
        schedule.setNotes(notes);

        return ret;
    }
}
