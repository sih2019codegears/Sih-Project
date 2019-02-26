package com.example.kamran.bluewhite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kamran.bluewhite.Employee.EmployeeHomeScreen;
import com.example.kamran.bluewhite.customer.CustomerHomeScreen;
import com.example.kamran.bluewhite.farmers.FarmerHomeScreen;
import com.example.kamran.bluewhite.industry.IndustryHomeScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class main extends AppCompatActivity {
    TextView sin;
    LinearLayout circle;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        circle = findViewById(R.id.circle);
        sin = (TextView)findViewById(R.id.sin);

        prefs=getSharedPreferences("com.example.kamran.bluewhite", Context.MODE_PRIVATE);

        if(mUser!=null)
        {

            String category=prefs.getString("category","");


            if(category.equals("industry"))
            {

                Intent intent;
                intent= new Intent(main.this, IndustryHomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);


            }
            if(category.equals("farmer"))
            {

                Intent intent;
                intent= new Intent(main.this, FarmerHomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
            if(category.equals("vendors"))
            {

                Intent intent;
                intent= new Intent(main.this, EmployeeHomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }

            if(category.equals("customer"))
            {

                Intent intent;
                intent= new Intent(main.this, CustomerHomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);


            }



        }
        else {

            circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(main.this, category.class);
                    startActivity(it);
                }
            });
            sin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(main.this, signin.class);
                    startActivity(it);
                    finish();
                }

            });

        }
    }
}
