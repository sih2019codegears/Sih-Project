package com.example.kamran.bluewhite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity
{
    ImageView sback;
    String category;
    EditText fullnameEditText,usernameEditText,emailEditText,passwordEditText;
    TextView signupTextView;
    String fullname,username,email,password;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef,mDatabaseRef1;
    Map<String,String> map;
    ProgressBar mProgressBar;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        category=getIntent().getStringExtra("category");
        sback = (ImageView)findViewById(R.id.sback);
        fullnameEditText=findViewById(R.id.fullnameEditText);
        usernameEditText=findViewById(R.id.usernameEditText);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        signupTextView=findViewById(R.id.signUpTextView);
        mProgressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference();
        mDatabaseRef1=FirebaseDatabase.getInstance().getReference();

        mDatabaseRef.keepSynced(true);
        mDatabaseRef1.keepSynced(true);

        prefs=getSharedPreferences("com.example.kamran.bluewhite", Context.MODE_PRIVATE);
        editor=prefs.edit();

        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(signup.this, main.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);

            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
             verify();


            }
        });


    }

    public void verify()
    {

        if(!TextUtils.isEmpty(fullnameEditText.getText())&&!TextUtils.isEmpty(usernameEditText.getText())&&!TextUtils.isEmpty(emailEditText.getText())&&!TextUtils.isEmpty(passwordEditText.getText()))
        {

            register();
        }
        else {

            mProgressBar.setVisibility(View.INVISIBLE);
            if(TextUtils.isEmpty(fullnameEditText.getText())){
                fullnameEditText.setError("Full name is required!");
            }
            if(TextUtils.isEmpty(usernameEditText.getText())){
                usernameEditText.setError("Full name is required!");
            }
            if(TextUtils.isEmpty(passwordEditText.getText())){
                passwordEditText.setError("Full name is required!");
            }
            if(TextUtils.isEmpty(emailEditText.getText())){
                emailEditText.setError("Full name is required!");
            }


        }


    }

    void register()
    {

        fullname=String.valueOf(fullnameEditText.getText());
        email=String.valueOf(emailEditText.getText());
        username=String.valueOf(usernameEditText.getText());
        password=String.valueOf(passwordEditText.getText());
        map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("email",email);
        map.put("username",username);
        map.put("password",password);
        map.put("category",category);

        editor.putString("category",category);
        editor.putString("email",email);
        editor.putString("fullname",fullname);
        editor.putString("username",username);


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Log.i("user created ","successfully");
                    final String uid=mAuth.getCurrentUser().getUid();
                    map.put("uid",uid);


                    mDatabaseRef.child("users").child(uid).setValue(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if(databaseError==null)
                            {
                                editor.putString("uid",uid);
                                editor.commit();
                                mProgressBar.setVisibility(View.INVISIBLE);

                                if(category.equals("industry"))
                                {

                                    Intent intent;
                                    intent= new Intent(signup.this, IndustryHomeScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);


                                }
                                if(category.equals("farmer"))
                                {

                                    Intent intent;
                                    intent= new Intent(signup.this, FarmerHomeScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);

                                }
                                if(category.equals("vendors"))
                                {

                                    Intent intent;
                                    intent= new Intent(signup.this, EmployeeHomeScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);

                                }

                                if(category.equals("customer"))
                                {

                                    Intent intent;
                                    intent= new Intent(signup.this, CustomerHomeScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);


                                }



                                Log.i("sign up","successful");
                            }
                            else{
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Log.i("signup","unsuccessful");
                            }

                        }
                    });

                }
                else{
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Log.i("auth fail","else block");
                    Toast.makeText(signup.this,"Signup UnSuccessful",Toast.LENGTH_SHORT);

                }

            }
        });
    }
}
