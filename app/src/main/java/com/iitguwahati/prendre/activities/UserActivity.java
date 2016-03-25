package com.iitguwahati.prendre.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.iitguwahati.prendre.R;
import com.iitguwahati.prendre.fragments.TasksFragment;
import com.iitguwahati.prendre.fragments.EventsFragment;
import com.iitguwahati.prendre.fragments.SchedulerFragment;
import com.iitguwahati.prendre.storage.SessionManager;

public class UserActivity extends AppCompatActivity {

    SessionManager session;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        final TabLayout tabLayout;
        final ViewPager viewPager;
        session = new SessionManager(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Eventify");
        if (!session.isLoggedIn()) {
            startActivity(new Intent(UserActivity.this, LoginActivity.class));
            finish();
        }
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);


        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
        viewPager.setCurrentItem(1);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });


    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TasksFragment();
                case 1:
                    return new EventsFragment();
                case 2:
                    return new SchedulerFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return 3;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Tasks";
                case 1:
                    return "Events";
                case 2:
                    return "Scheduler";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.profile:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                startActivity(new Intent(UserActivity.this, ProfileActivity.class));

                return true;

            case R.id.logout:
                logoutUser();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void logoutUser(){
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        session.setLogin(false);

        finish();
    }

}
