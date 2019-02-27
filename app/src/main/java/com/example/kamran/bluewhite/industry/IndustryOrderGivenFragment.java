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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kamran.bluewhite.MoneyTransferActivity;
import com.example.kamran.bluewhite.R;
import com.example.kamran.bluewhite.farmers.FarmerMyOrderFragment;
import com.example.kamran.bluewhite.farmers.OrderReceived;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndustryOrderGivenFragment extends Fragment {


    RecyclerView recyclerView;
    Query query;
    FirebaseRecyclerOptions<OrderReceived> options;
    FirebaseRecyclerAdapter<OrderReceived, IndustryOrderGivenFragment.ViewHolder> recyclerAdapter;

    DatabaseReference mRef, mRef1, mRef2, declineRef;

    String key;

    FirebaseAuth mAuth;
    String currentUid, industryuid;


    public IndustryOrderGivenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_industry_order_given, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);



        currentUid = FirebaseAuth.getInstance().getUid();

        mRef=FirebaseDatabase.getInstance().getReference().child("users");

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        query = FirebaseDatabase.getInstance()
                .getReference().child("order_industry_farmer").child("industry").child(currentUid);

        query.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<OrderReceived>()
                .setQuery(query, OrderReceived.class).setLifecycleOwner(this)
                .build();


        recyclerAdapter = new FirebaseRecyclerAdapter<OrderReceived, IndustryOrderGivenFragment.ViewHolder>(options) {


            @NonNull
            @Override
            public IndustryOrderGivenFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_order_status, viewGroup, false);
                IndustryOrderGivenFragment.ViewHolder holder = new IndustryOrderGivenFragment.ViewHolder(view);
                return holder;

            }

            @Override
            protected void onBindViewHolder(@NonNull final IndustryOrderGivenFragment.ViewHolder holder, int position, @NonNull OrderReceived model) {

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MoneyTransferActivity.class);
                        startActivity(intent);


                    }
                });

                mRef.child(model.getTo()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            holder.username.setText(String.valueOf(dataSnapshot.child("fullname").getValue()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                holder.qty.setText(model.getQuantity());
                holder.status.setText(model.getStatus());




            }


        };

        recyclerView.setAdapter(recyclerAdapter);
        return v;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView username,qty,status;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

            linearLayout=itemView.findViewById(R.id.linear_layout);
            username=mView.findViewById(R.id.requested_user_name);
            qty=mView.findViewById(R.id.quantity_requested);
            status=mView.findViewById(R.id.order_status);


        }
    }

}
