package com.iitguwahati.prendre.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.iitguwahati.prendre.R;
import com.iitguwahati.prendre.storage.SessionManager;
import com.viewpagerindicator.CirclePageIndicator;

public class GetStartedActivity extends AppCompatActivity {

    SessionManager session;
    Button getStarted;
    ViewPager viewpager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        session = new SessionManager(this);
        if(session.isGetStartedDone()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        viewpager = (ViewPager) findViewById(R.id.pager);
        getStarted = (Button) findViewById(R.id.getStarted);
        viewpager.setClipToPadding(false);
        viewpager.setPadding(80,60,80,60);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setGetStartedDone(true);
                startActivity(new Intent(GetStartedActivity.this, LoginActivity.class));

            }
        });
        com.iitguwahati.prendre.helper.PagerAdapter padapter = new
                com.iitguwahati.prendre.helper.PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);

        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(viewpager);

    }
}
