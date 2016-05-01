package com.tripfriend.front.about;

import android.app.Activity;
import android.os.Bundle;

import com.tripfriend.R;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(getResources().getText(R.string.about_heading));
    }
}
