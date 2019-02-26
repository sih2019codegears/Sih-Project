package com.example.kamran.bluewhite.farmers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.kamran.bluewhite.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmerAddProductFragment extends Fragment {


    private static final int IMAGE_SIZE = 200;
    private SurfaceView cameraPreview;
    private RelativeLayout overlay;
    static Camera camera = null;
    SurfaceHolder surfaceHolder;

    public FarmerAddProductFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View v = inflater.inflate(R.layout.fragment_farmer_add_product, container, false);


        return v;

    }
}

