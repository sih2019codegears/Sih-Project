package com.example.kamran.bluewhite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class category extends AppCompatActivity {
    Button oval4,oval3,oval2,oval1,oval;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        oval4=(Button)findViewById(R.id.oval4);
        oval3=(Button)findViewById(R.id.oval3);
        oval2=(Button)findViewById(R.id.oval2);
        oval1=(Button)findViewById(R.id.oval1);
        oval=(Button)findViewById(R.id.oval);

        oval4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(category.this,signup.class);
                it.putExtra("category","farmer");
                startActivity(it);
                finish();
            }
        });

        oval3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(category.this,signup.class);
                it.putExtra("category","industry");
                startActivity(it);
                finish();
            }
        });

        oval2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(category.this,signup.class);
                it.putExtra("category","customer");
                startActivity(it);
                finish();
            }
        });

        oval1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(category.this,signup.class);
                it.putExtra("category","transportation");
                startActivity(it);
                finish();
            }
        });

        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(category.this,signup.class);
                it.putExtra("category","employee");
                startActivity(it);
                finish();
            }
        });

    }
}
