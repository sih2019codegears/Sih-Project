package com.example.kamran.bluewhite.farmers;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kamran.bluewhite.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmerUpdateFragment extends Fragment {

    private DatabaseReference mRef,mRef1;
    private FirebaseAuth mAuth;
    EditText qtyEditText,priceEditText;
    Button qtyUpdateButton,priceUpdateButton;
    ProgressBar qtyProgressBar,priceProgressBar;
    String uid;

    public FarmerUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



       View v =inflater.inflate(R.layout.fragment_farmer_update, container, false);

       qtyEditText=v.findViewById(R.id.qtyUpdateEditText);
       qtyUpdateButton=v.findViewById(R.id.qtyUpdateButton);
       priceEditText=v.findViewById(R.id.updatePriceEditText);
       priceUpdateButton=v.findViewById(R.id.updatePriceButton);
       qtyProgressBar=v.findViewById(R.id.qtyProgressBar);

       priceProgressBar=v.findViewById(R.id.priceProgressBar);
       mRef= FirebaseDatabase.getInstance().getReference();
        mRef1= FirebaseDatabase.getInstance().getReference();
       mAuth=FirebaseAuth.getInstance();
       uid=mAuth.getCurrentUser().getUid();

       qtyUpdateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(!TextUtils.isEmpty(qtyEditText.getText())) {

                   qtyProgressBar.setVisibility(View.VISIBLE);
                   String qty=String.valueOf(qtyEditText.getText());
                   mRef.child("farmer_quantity").child(uid).child("quantity").setValue(qty, new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                           qtyProgressBar.setVisibility(View.INVISIBLE);
                           if(databaseError==null) {
                               Toast.makeText(getActivity(), "Quantity updated!!", Toast.LENGTH_SHORT);
                           }
                           else
                               Toast.makeText(getContext(),"Quantity could not be updated!!",Toast.LENGTH_SHORT);



                       }
                   });

               }
           }
       });

       priceUpdateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               priceProgressBar.setVisibility(View.VISIBLE);
               if(!TextUtils.isEmpty(priceEditText.getText())) {
                   String price=String.valueOf(priceEditText.getText());
                   mRef1.child("farmer_quantity").child(uid).child("price").setValue(price, new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                           priceProgressBar.setVisibility(View.INVISIBLE);
                           if(databaseError==null)
                               Toast.makeText(getContext(), "Price updated successfully!!", Toast.LENGTH_SHORT).show();
                           else
                               Toast.makeText(getContext(), "Price could not be updated!!", Toast.LENGTH_SHORT).show();


                       }
                   });


               }
           }
       });




       return v;
    }

}
