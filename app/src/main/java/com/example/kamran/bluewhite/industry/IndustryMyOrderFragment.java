package com.example.kamran.bluewhite.industry;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamran.bluewhite.R;
import com.example.kamran.bluewhite.farmers.ImageFile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndustryMyOrderFragment extends Fragment {

    RecyclerView recyclerView;
    Query query;
    FirebaseRecyclerOptions<Farmers> options;
    FirebaseRecyclerAdapter<Farmers,IndustryMyOrderFragment.ViewHolder> recyclerAdapter;

    DatabaseReference mRef;
    public IndustryMyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_industry_my_order_fragment, container, false);

        recyclerView=v.findViewById(R.id.recycler_view);

        mRef=FirebaseDatabase.getInstance().getReference();



        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        query = FirebaseDatabase.getInstance()
                .getReference().child("farmer_quantity");

        query.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Farmers>()
                .setQuery(query, Farmers.class).setLifecycleOwner(this)
                .build();

        recyclerAdapter=new FirebaseRecyclerAdapter<Farmers, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Farmers model) {


                holder.qty.setText("Qty: "+model.getQuantity());
                holder.price.setText("Price: "+model.getPrice());
                holder.username.setText(model.getFullname());


               final String key = getRef(position).getKey();


                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        mRef.child("farmer_files").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(key))
                                {

                                    Intent i = new Intent(getActivity(),ViewPhotosActivity.class);
                                    i.putExtra("uid",key);
                                    startActivity(i);



                                }
                                else{

                                    Toast.makeText(getContext(), "Sorry no photos exists for this user!", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });












                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_user_layout, viewGroup, false);
                IndustryMyOrderFragment.ViewHolder holder = new ViewHolder(view);
                return holder;



            }
        };

        recyclerView.setAdapter(recyclerAdapter);

      return v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView username,email,price,qty;
        LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            linearLayout=mView.findViewById(R.id.linear_layout);
            username=mView.findViewById(R.id.user_name);
            email=mView.findViewById(R.id.email);
            price=mView.findViewById(R.id.price);
            qty=mView.findViewById(R.id.quantity);

        }
    }
}
