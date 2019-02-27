package com.example.kamran.bluewhite.Employee;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kamran.bluewhite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeReqProductFragment extends Fragment {


    public EmployeeReqProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_req_product, container, false);
    }

}
