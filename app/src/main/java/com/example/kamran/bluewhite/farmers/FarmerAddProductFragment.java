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

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamran.bluewhite.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
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

    RecyclerView recyclerView;

    private FirebaseRecyclerAdapter<ImageFile, FarmerAddProductFragment.ViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<ImageFile> options;
    private Query query;



    public FarmerAddProductFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_farmer_add_product, container, false);





        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView=v.findViewById(R.id.recycler_view);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Uploading Image");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        chooseFileImageView = v.findViewById(R.id.choose_file_imageview);

        uploadChosenFileImageView = v.findViewById(R.id.upload_chosen_file_imageview);

        uploadChosenFileTextView = v.findViewById(R.id.upload_chosen_file_textview);

        chosenFileTextView = v.findViewById(R.id.chosen_file_textview);

        permission_check();




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


    private void permission_check() {
        if(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

            }
        }

        initialize();
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


    public void initialize() {

        recyclerView.hasFixedSize();
           recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        query = FirebaseDatabase.getInstance()
                .getReference().child("farmer_files").child(uid);

        query.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<ImageFile>()
                .setQuery(query, ImageFile.class).setLifecycleOwner(this)
                .build();


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ImageFile, FarmerAddProductFragment.ViewHolder>(options) {


                        @Override
                        protected void onBindViewHolder(@NonNull FarmerAddProductFragment.ViewHolder holder, int position, @NonNull ImageFile model) {

                            String url=model.getUrl();

                            Picasso.get()
                                    .load(url)
                                    //.resize(getActivity().getWindowManager().getDefaultDisplay().getWidth(), 400)
                                    //.centerCrop()
                                    .into(holder.imageView);



                        }
                        @NonNull
                        @Override
                        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_image_layout, parent, false);
                            ViewHolder holder = new ViewHolder(view);
                            return holder;

                        }
                    };


                    recyclerView.setAdapter(firebaseRecyclerAdapter);


                } else {
                    Toast.makeText(getContext(), "No files for this unit.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

        public static  class ViewHolder  extends RecyclerView.ViewHolder{

            View itemView;
            ImageView imageView;
            public ViewHolder(View mview) {
                super(mview);
                itemView = mview;
                imageView=itemView.findViewById(R.id.individual_image);

            }
        }


    }