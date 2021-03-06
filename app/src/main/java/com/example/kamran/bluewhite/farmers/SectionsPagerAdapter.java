package com.example.kamran.bluewhite.farmers;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch(i)
        {
            case 0:
                return new FarmerHomeFragment();
            case 1:
                return new FarmerAddProductFragment();

            case 2:
                return new FarmerUpdateFragment();

            case 3:
                return new FarmerMyOrderFragment();

        }


        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){

            case 0:
                return "Home";
            case 1:
                return "Add Product";
            case 2:
                return "Update";
            case 3:
                return "Order Received";
        }
        return null;


    }
}
