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

public class OrderUserInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_user_info);

        Button finish_registration = (Button) findViewById(R.id.orderU_finish);
        finish_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()) {
                    Intent intent = new Intent(OrderUserInfoActivity.this, OrderCompleteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(OrderUserInfoActivity.this, "Fill all fields (parametrize)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkInputs() {
        Boolean ret = true;

        EditText nameEditText = (EditText) findViewById(R.id.orderU_name);
        String name = nameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Your message");
            ret = false;
        }

        EditText emailEditText = (EditText) findViewById(R.id.orderU_email);
        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("advavadv");
            ret = false;
        }

        EditText phoneEditText = (EditText) findViewById(R.id.orderU_phone);
        String phone = phoneEditText.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("avsdv");
            ret = false;
        }

        EditText groupEditText = (EditText) findViewById(R.id.orderU_group);
        String group = groupEditText.getText().toString();
        if (TextUtils.isEmpty(group)) {
            groupEditText.setError("avssdvsvsvdv");
            ret = false;
        }

        return ret;
    }
}
