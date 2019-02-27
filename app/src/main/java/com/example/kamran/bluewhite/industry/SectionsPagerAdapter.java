package com.example.kamran.bluewhite.industry;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kamran.bluewhite.farmers.FarmerMyOrderFragment;
import com.example.kamran.bluewhite.farmers.FarmerUpdateFragment;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch(i)
        {
            case 0:
                return new IndustryHomeFragment();
            case 1:
                return new IndustryAddProductFragment();

            case 2:
                return new IndustryUpdateFragment();

            case 3:
                return new IndustryMyOrderFragment();

            case 4:
                return new IndustryOrderGivenFragment();

        }


        return null;
    }

    @Override
    public int getCount() {
        return 5;
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
                return "Farmers";
            case 4:
                return "Order Status";
        }
        return null;


    }
}
