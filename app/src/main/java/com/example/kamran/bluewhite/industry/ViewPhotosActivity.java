package com.example.kamran.bluewhite.industry;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.kamran.bluewhite.R;
import com.example.kamran.bluewhite.farmers.ImageFile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewPhotosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Query query;
    FirebaseRecyclerOptions<ImageFile> options;
    FirebaseRecyclerAdapter<ImageFile, ViewPhotosActivity.ViewHolder> recyclerAdapter;

    DatabaseReference mRef1,mRef2;

    EditText qtyEditText;

    String key;

    FirebaseAuth mAuth;
    String currentUid;
    Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photos);
        currentUid=FirebaseAuth.getInstance().getUid();
        recyclerView = findViewById(R.id.recyclerView);
        placeOrder=findViewById(R.id.placeButton);
        mRef1=FirebaseDatabase.getInstance().getReference();
        mRef2=FirebaseDatabase.getInstance().getReference();

        recyclerView.hasFixedSize();
        qtyEditText=findViewById(R.id.qtyEditText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        key = getIntent().getStringExtra("uid");
        query = FirebaseDatabase.getInstance()
                .getReference().child("farmer_files").child(key);



        query.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<ImageFile>()
                .setQuery(query, ImageFile.class).setLifecycleOwner(this)
                .build();

        recyclerAdapter = new FirebaseRecyclerAdapter<ImageFile, ViewPhotosActivity.ViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final ViewPhotosActivity.ViewHolder holder, int position, @NonNull ImageFile model) {
                String url;
                url=model.getUrl();
                Picasso.get()
                        .load(url)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imageView);

                placeOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!TextUtils.isEmpty(qtyEditText.getText()))
                        {
                            String qty=String.valueOf(qtyEditText.getText());
                            HashMap<String,String> hashMap1= new HashMap<>();
                            final HashMap<String,String> hashMap2=new HashMap<>();
                            hashMap1.put("from",currentUid);
                            hashMap1.put("to",key);
                            hashMap1.put("quantity",qty);
                            hashMap1.put("status","pending");
                            hashMap2.put("from",key);
                            hashMap2.put("to",currentUid);
                            hashMap2.put("quantity",qty);
                            hashMap2.put("status","pending");

                            mRef1.child("order_industry_farmer").child("farmer").child(key).child(currentUid).setValue(hashMap1,new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                                {

                                    if(databaseError==null)
                                    {
                                        mRef2.child("order_industry_farmer").child("industry").child(currentUid).child(key).setValue(hashMap2, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                                            {
                                                if(databaseError==null)
                                                {
                                                    Toast.makeText(ViewPhotosActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Log.i("Exception",databaseError.getDetails());
                                                    Toast.makeText(ViewPhotosActivity.this, "Order could not be placed!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        Log.i("Exception",databaseError.getDetails());
                                        Toast.makeText(ViewPhotosActivity.this, "Order could not be placed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(ViewPhotosActivity.this, "No quantity mentioned!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }


            @NonNull
            @Override
            public ViewPhotosActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_image_layout, viewGroup, false);
                ViewPhotosActivity.ViewHolder holder = new ViewPhotosActivity.ViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(recyclerAdapter);

    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView imageView;
        EditText qtyEditText;
        Button placeOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            imageView=mView.findViewById(R.id.individual_image);


        }
    }
}
