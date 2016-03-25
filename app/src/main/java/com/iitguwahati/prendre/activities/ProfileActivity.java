package com.iitguwahati.prendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iitguwahati.prendre.R;
import com.iitguwahati.prendre.storage.SQLiteHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> timings, details;
    SQLiteHandler db;
    TextView name,branch, roll, hostel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        name= (TextView) findViewById(R.id.name);
        branch= (TextView) findViewById(R.id.branch);
        roll= (TextView) findViewById(R.id.roll);
        hostel= (TextView) findViewById(R.id.hostel);
        db = new SQLiteHandler(getApplicationContext());
        details = db.getUserDetails();
        name.setText(details.get(0).get("name"));
        roll.setText(details.get(0).get("roll_number"));
        branch.setText(details.get(0).get("branch") + "," + details.get(0).get("year"));
        hostel.setText(details.get(0).get("hostel"));

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Profile");
    }

    

}
