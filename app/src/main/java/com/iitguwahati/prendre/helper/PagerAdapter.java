package com.iitguwahati.prendre.helper;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iitguwahati.prendre.fragments.GetStarted1;
import com.iitguwahati.prendre.fragments.GetStarted2;
import com.iitguwahati.prendre.fragments.GetStarted3;
import com.iitguwahati.prendre.fragments.GetStarted4;
import com.iitguwahati.prendre.fragments.GetStarted5;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        switch (arg0) {


            case 0:
                return new GetStarted1();

            case 1:
                return new GetStarted2();

            case 2:
                return new GetStarted3();

            case 3:
                return new GetStarted4();

            case 4:
                return new GetStarted5();

            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 5;
    }

}
