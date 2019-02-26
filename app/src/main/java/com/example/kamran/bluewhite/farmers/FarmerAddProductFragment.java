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


    private static final int IMAGE_SIZE =200 ;
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

        View v= inflater.inflate(R.layout.fragment_farmer_add_product, container, false);


        // Optional: Hide the status bar at the top of the window

        // Set the content view and get references to our views
        cameraPreview = (SurfaceView)v. findViewById(R.id.camera_preview);
        overlay = (RelativeLayout) v.findViewById(R.id.overlay);

        surfaceHolder = cameraPreview.getHolder();
      //  surfaceHolder.addCallback((SurfaceHolder.Callback)FarmerAddProductFragment.this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

         camera = Camera.open();

        Camera.PreviewCallback cameraPreviewCallback = new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {

            }
        };

        try {
            camera.setPreviewCallback(cameraPreviewCallback);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();


        } catch (IOException e) {
            e.printStackTrace();
        }

//        Camera.Parameters camParams = camera.getParameters();

//// Find a preview size that is at least the size of our IMAGE_SIZE
//        Camera.Size previewSize = camParams.getSupportedPreviewSizes().get(0);
//        for (Camera.Size size : camParams.getSupportedPreviewSizes()) {
//            if (size.width >= IMAGE_SIZE && size.height >= IMAGE_SIZE) {
//                previewSize = size;
//                break;
//            }
//        }
//        camParams.setPreviewSize(previewSize.width, previewSize.height);
//
//// Try to find the closest picture size to match the preview size.
//        Camera.Size pictureSize = camParams.getSupportedPictureSizes().get(0);
//        for (Camera.Size size : camParams.getSupportedPictureSizes()) {
//            if (size.width == previewSize.width && size.height == previewSize.height) {
//                pictureSize = size;
//                break;
//            }
//        }
//        camParams.setPictureSize(pictureSize.width, pictureSize.height);




    return v;
    }


    public void onWindowFocusChanged(boolean hasFocus) {

        // Get the preview size
        int previewWidth = cameraPreview.getMeasuredWidth(),
                previewHeight = cameraPreview.getMeasuredHeight();

        // Set the height of the overlay so that it makes the preview a square
        RelativeLayout.LayoutParams overlayParams = (RelativeLayout.LayoutParams) overlay.getLayoutParams();
        overlayParams.height = previewHeight - previewWidth;
        overlay.setLayoutParams(overlayParams);
    }

    private Bitmap processImage(byte[] data) throws IOException {
        // Determine the width/height of the image
        int width = camera.getParameters().getPictureSize().width;
        int height = camera.getParameters().getPictureSize().height;

        // Load the bitmap from the byte array
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Rotate and crop the image into a square
        int croppedWidth = (width > height) ? height : width;
        int croppedHeight = (width > height) ? height : width;

        Matrix matrix = new Matrix();
       // matrix.postRotate(IMAGE_ORIENTATION);
        Bitmap cropped = Bitmap.createBitmap(bitmap, 0, 0, croppedWidth, croppedHeight, matrix, true);
        bitmap.recycle();

        // Scale down to the output size
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(cropped, IMAGE_SIZE, IMAGE_SIZE, true);
        cropped.recycle();

        return scaledBitmap;
    }

//    public CameraView(Context ctx, AttributeSet attrSet) {
//        super(ctx, attrSet);
////        getHolder().addCallback(cameraPreviewCallback);
////        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//    }


}
