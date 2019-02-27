package com.example.kamran.bluewhite.industry;
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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndustryAddProductFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Uri uri;
    ImageView chooseFileImageView,uploadChosenFileImageView;

    TextView chosenFileTextView;
    TextView uploadChosenFileTextView;

    DatabaseReference databaseReference,databaseReference1;
    StorageReference storageReference;

    ProgressDialog progressDialog;
    int progresspercentage;

    String uid;

    RecyclerView recyclerView;

    Spinner mSpinner;

    String category;

    List<String> categories;

    EditText priceEditText;
    String price="0";


    public IndustryAddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_industry_add_product_fragment, container, false);

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView=v.findViewById(R.id.recycler_view);
        priceEditText=v.findViewById(R.id.priceEditText);
        mSpinner=v.findViewById(R.id.mSpinner);
        mSpinner.setOnItemSelectedListener(this);


        categories = new ArrayList<>();
        categories.add("Item 1");
        categories.add("Item 2");
        categories.add("Item 3");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mSpinner.setAdapter(dataAdapter);



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


        uploadChosenFileImageView.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                price=String.valueOf(priceEditText.getText());

                progresspercentage=0;

                if (uri != null) {

                    File file = new File(uri.getPath());

                    final String fileName = file.getName();
                    String format[] = fileName.split("\\.");

                    // Log.i("format", format.toString());
                    final String fileFormat = format[format.length-1];



                    progresspercentage=0;


                    databaseReference = FirebaseDatabase.getInstance().getReference().child("industry_files").child(uid).push();
                    databaseReference.keepSynced(true);
                    final String pushId = databaseReference.getKey();

                    storageReference = FirebaseStorage.getInstance().getReference().child("industry_files").child(uid).child(category).child(pushId);

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
                                        value.put("category",category);
                                        value.put("price",price);

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

        if(category!=null) {

            progresspercentage = 0;

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, 0);

        }
        else{
            Toast.makeText(getContext(), "Kindly choose your category!", Toast.LENGTH_SHORT).show();
        }
    }


    private void permission_check() {
        if(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

            }
        }

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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         category = String.valueOf(adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {



    }
}




