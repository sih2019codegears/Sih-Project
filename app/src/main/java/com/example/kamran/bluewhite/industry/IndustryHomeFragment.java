package com.example.kamran.bluewhite.industry;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kamran.bluewhite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndustryHomeFragment extends Fragment {


    public IndustryHomeFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_industry_home, container, false);
    }

}
