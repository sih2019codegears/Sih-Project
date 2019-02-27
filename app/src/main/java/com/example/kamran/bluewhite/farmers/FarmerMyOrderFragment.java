
package com.example.kamran.bluewhite.farmers;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamran.bluewhite.R;
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
public class FarmerMyOrderFragment extends Fragment {


    RecyclerView recyclerView;
    Query query;
    FirebaseRecyclerOptions<OrderReceived> options;
    FirebaseRecyclerAdapter<OrderReceived, FarmerMyOrderFragment.ViewHolder> recyclerAdapter;

    DatabaseReference mRef,mRef1,mRef2,declineRef;

    String key;

    FirebaseAuth mAuth;
    String currentUid,industryuid;


    public FarmerMyOrderFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_farmer_my_order, container, false);

        currentUid = FirebaseAuth.getInstance().getUid();
        recyclerView = v.findViewById(R.id.recycler_view_order);

        mRef=FirebaseDatabase.getInstance().getReference();
        declineRef=FirebaseDatabase.getInstance().getReference().child("order_industry_farmer").child("farmer").child(currentUid);
        mRef1 = FirebaseDatabase.getInstance().getReference();
        mRef2 = FirebaseDatabase.getInstance().getReference();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        query = FirebaseDatabase.getInstance()
                .getReference().child("order_industry_farmer").child("farmer").child(currentUid);

        query.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<OrderReceived>()
                .setQuery(query, OrderReceived.class).setLifecycleOwner(this)
                .build();


        recyclerAdapter = new FirebaseRecyclerAdapter<OrderReceived, FarmerMyOrderFragment.ViewHolder>(options) {


            @NonNull
            @Override
            public FarmerMyOrderFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_received, viewGroup, false);
                FarmerMyOrderFragment.ViewHolder holder = new FarmerMyOrderFragment.ViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final FarmerMyOrderFragment.ViewHolder holder, int position, @NonNull final OrderReceived model) {

                final String uid=model.getFrom();

                //holder.username.setText(model.getFrom());
                mRef.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String name=String.valueOf(dataSnapshot.child("fullname").getValue());

                        holder.username.setText(name);
                        holder.qty.setText(model.getQuantity());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        mRef1.child("order_industry_farmer").child("farmer").child(currentUid).child(uid).child("status").setValue("accepted", new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                              if(databaseError==null) {
                                  mRef2.child("order_industry_farmer").child("industry").child(uid).child(currentUid).child("status").setValue("accepted", new DatabaseReference.CompletionListener() {
                                      @Override
                                      public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                          if (databaseError == null)
                                              Toast.makeText(getActivity(), "Order Accepted !!", Toast.LENGTH_SHORT).show();
                                          else {
                                              Toast.makeText(getActivity(), "Failed to Accept Order !!", Toast.LENGTH_SHORT).show();
                                              Log.i("Error to accept order", databaseError.getDetails());

                                          }
                                      }
                                  });

                              }
                              else{
                                  Toast.makeText(getActivity(), "Failed to Accept Order !!", Toast.LENGTH_SHORT).show();

                              }
                            }
                        });


                    }
                });

                holder.decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        declineRef.child(uid).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                if(databaseError==null)
                                {
                                    mRef2.child("order_industry_farmer").child("industry").child(uid).child(currentUid).child("status").setValue("declined", new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                            Toast.makeText(getActivity(), "Order Declined !", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                                else{
                                    Toast.makeText(getActivity(), "Sorry Could not decline order", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });

            }

        };
        recyclerView.setAdapter(recyclerAdapter);
        return v;
    }

    private class ViewHolder extends  RecyclerView.ViewHolder{
        TextView username,qty;
        Button accept,decline;
        View mView;
        public ViewHolder(View view) {
            super(view);
            mView=view;
            accept=mView.findViewById(R.id.accept_request_button);
            decline=mView.findViewById(R.id.decline_request_button);
            username=mView.findViewById(R.id.received_user_name);
            qty=mView.findViewById(R.id.quantity_recieved);
        }
    }
}
