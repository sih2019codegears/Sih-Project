package com.example.kamran.bluewhite.farmers;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamran.bluewhite.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmerAddProductFragment extends Fragment {



    Uri uri;
    String serverUri;
    ImageView chooseFileImageView,uploadChosenFileImageView;

    TextView chosenFileTextView;
    TextView uploadChosenFileTextView;

    DatabaseReference databaseReference,databaseReference1;
    StorageReference storageReference;

    ProgressDialog progressDialog;
    int progresspercentage;

    String uid;


    public FarmerAddProductFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View v = inflater.inflate(R.layout.fragment_farmer_add_product, container, false);

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Uploading Image");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        chooseFileImageView = v.findViewById(R.id.choose_file_imageview);

        uploadChosenFileImageView = v.findViewById(R.id.upload_chosen_file_imageview);

        uploadChosenFileTextView = v.findViewById(R.id.upload_chosen_file_textview);

        chosenFileTextView = v.findViewById(R.id.chosen_file_textview);




        chooseFileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //For marshmellow the permission is already granted..
                if (Build.VERSION.SDK_INT < 23) {
                    chooseFile();

                } else {
                    //if sdk int>23 check for self permission

                    if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //if permission is not granted ask for permission

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        chooseFile();
                    }


                }

            }
        });


        // uploading process begins here ------------->>>>>>>>>


        uploadChosenFileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progresspercentage=0;

                if (uri != null) {

                    File file = new File(uri.getPath());

                    final String fileName = file.getName();
                    String format[] = fileName.split("\\.");

                    // Log.i("format", format.toString());
                    final String fileFormat = format[format.length-1];



                    progresspercentage=0;


                    databaseReference = FirebaseDatabase.getInstance().getReference().child("farmer_files").child(uid).push();
                    databaseReference.keepSynced(true);
                    final String pushId = databaseReference.getKey();

                    storageReference = FirebaseStorage.getInstance().getReference().child("farmer_files").child(uid).child(pushId);

                    storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {


                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri1) {

                                        HashMap<String, String> value = new HashMap<>();
                                        value.put("pushId", pushId);
                                        value.put("url", uri1.toString());
                                        value.put("type", fileFormat);
                                        value.put("name", fileName);


                                        databaseReference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();

                                                    Toast.makeText(getActivity(), "File Uploaded !", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), "File could not be uploaded !", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });


                                    }
                                });

                            } else {

                                Toast.makeText(getActivity(), "File could not be uploaded !", Toast.LENGTH_SHORT).show();


                            }
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progresspercentage=0;
                            progressDialog.show();
                            progresspercentage = (int) ((int)(100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                            progressDialog.incrementProgressBy(progresspercentage);
                        }
                    });


                }
            }
        });
        return v;
    }

    private void chooseFile() {

        progresspercentage=0;

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent,0);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (requestCode == 0 && data != null) {

            uri = data.getData();

            Log.i("URI",String.valueOf(uri));
            File file= new File(uri.getPath());
            chosenFileTextView.setVisibility(View.VISIBLE);
            chosenFileTextView.setText(file.getName());

            uploadChosenFileTextView.setVisibility(View.VISIBLE);
            uploadChosenFileImageView.setVisibility(View.VISIBLE);

        }
    }


}
