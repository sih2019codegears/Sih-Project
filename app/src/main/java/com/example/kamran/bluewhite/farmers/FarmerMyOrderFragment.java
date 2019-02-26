package com.example.kamran.bluewhite.farmers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kamran.bluewhite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmerMyOrderFragment extends Fragment {


    public FarmerMyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_farmer_my_order, container, false);

    return v;
    }

}
