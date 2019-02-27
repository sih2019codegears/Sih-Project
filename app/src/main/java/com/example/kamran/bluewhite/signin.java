package com.example.kamran.bluewhite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamran.bluewhite.Employee.EmployeeHomeScreen;
import com.example.kamran.bluewhite.customer.CustomerHomeScreen;
import com.example.kamran.bluewhite.farmers.FarmerHomeScreen;
import com.example.kamran.bluewhite.industry.IndustryHomeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signin extends AppCompatActivity {

    ImageView sback;
    TextView signInTextView;
    EditText eMailEditText,passwdEditText;
    private String password="",email="";
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    ProgressBar mProgressBar;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        sback = (ImageView)findViewById(R.id.sinb);
        signInTextView=findViewById(R.id.signInTextView);
        eMailEditText=findViewById(R.id.eMailEditText);
        passwdEditText=findViewById(R.id.passwdEditText);
        mProgressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference();

        mRef.keepSynced(true);
        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(signin.this,main.class);
                startActivity(it);
            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                verify();
            }
        });
    }
    public void verify()
    {

        if(!TextUtils.isEmpty(eMailEditText.getText())&&!TextUtils.isEmpty(passwdEditText.getText()))
        {
            signIn();
        }
        else {
            if(TextUtils.isEmpty(passwdEditText.getText())){
                mProgressBar.setVisibility(View.INVISIBLE);
                passwdEditText.setError("Password is required!");
            }
            if(TextUtils.isEmpty(eMailEditText.getText())){
                mProgressBar.setVisibility(View.INVISIBLE);
                eMailEditText.setError("Email is required!");
            }
        }
}

    private void signIn() {

        email=String.valueOf(eMailEditText.getText());
        password=String.valueOf(passwdEditText.getText());

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            FirebaseUser user = mAuth.getCurrentUser();
                           final String uid=user.getUid();

                            mRef.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists())
                                    {
                                        String category=String.valueOf(dataSnapshot.child("category").getValue());
                                        String email=String.valueOf(dataSnapshot.child("email").getValue());
                                        String fullname=String.valueOf(dataSnapshot.child("fullname").getValue());
                                        String username=String.valueOf(dataSnapshot.child("username").getValue());
                                        prefs=getSharedPreferences("com.example.kamran.bluewhite", Context.MODE_PRIVATE);
                                        editor=prefs.edit();

                                        editor.putString("category",category);
                                        Log.i("category",category);
                                        editor.putString("email",email);
                                        editor.putString("fullname",fullname);
                                        editor.putString("username",username);
                                        editor.putString("uid",uid);
                                        editor.commit();


                                         mProgressBar.setVisibility(View.INVISIBLE);

                                        if(category.equals("industry"))
                                        {

                                            Intent intent;
                                            intent= new Intent(signin.this, IndustryHomeScreen.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                                            startActivity(intent);


                                        }
                                        if(category.equals("farmer"))
                                        {

                                            Intent intent;
                                            intent= new Intent(signin.this, FarmerHomeScreen.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                                            startActivity(intent);

                                        }
                                        if(category.equals("vendors"))
                                        {

                                            Intent intent;
                                            intent= new Intent(signin.this, EmployeeHomeScreen.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                                            startActivity(intent);

                                        }

                                        if(category.equals("customer"))
                                        {

                                            Intent intent;
                                            intent= new Intent(signin.this, CustomerHomeScreen.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                                            startActivity(intent);


                                        }



                                        Log.i( "signInWithEmail","success");

                                    }



                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Log.i( "signInWithEmail:failure", task.getException().toString());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });


    }
    }
