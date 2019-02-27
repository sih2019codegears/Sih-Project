package com.example.kamran.bluewhite.industry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.kamran.bluewhite.R;

import com.example.kamran.bluewhite.main;
import com.google.firebase.auth.FirebaseAuth;

public class IndustryHomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
    {
        NavigationView navigationView;
        DrawerLayout drawerLayout;
        Toolbar toolbar;
        private int navigation_header;
        private FirebaseAuth mAuth;

        SharedPreferences prefs;

        ViewPager mViewPager;
        TabLayout mTabLayout;
        private SectionsPagerAdapter mSectionsPagerAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_industry_home_screen);

            mAuth=FirebaseAuth.getInstance();

            prefs=getSharedPreferences("com.example.kamran.bluewhite", Context.MODE_PRIVATE);

            navigationView =findViewById(R.id.navigation_drawer);
            navigationView.setNavigationItemSelectedListener(this);

            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Coir Coconut");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mViewPager = findViewById(R.id.view_pager);
            mTabLayout = findViewById(R.id.tab_layout);


            View headerView = navigationView.getHeaderView(0);

            TextView headerName=headerView.findViewById(R.id.name_header);
            TextView headerEmail = headerView.findViewById(R.id.email_header);
            String headername=prefs.getString("username","");
            headerName.setText(headername);
            Log.i("header name",headername);
            headerEmail.setText(prefs.getString("email",""));
            drawerLayout =findViewById(R.id.drawer_layout);


            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionsPagerAdapter);

            mTabLayout.setupWithViewPager(mViewPager);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);

                }
            });


            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();

            final IndustryAddProductFragment farmerAddProductFragment = new IndustryAddProductFragment();
            final IndustryHomeFragment farmerHomeFragment = new IndustryHomeFragment();
            final IndustryMyOrderFragment farmerMyOrderFragment  =new IndustryMyOrderFragment();
            final IndustryUpdateFragment farmerUpdateFragment = new IndustryUpdateFragment();
        }


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            drawerClose();

            switch(menuItem.getItemId())
            {

                case R.id.menu_logout:
                    mAuth.signOut();
                    Intent intent = new Intent(IndustryHomeScreen.this, main.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return true;


            }
            return false;


        }
        public void drawerClose()

        {

            drawerLayout.closeDrawer(GravityCompat.START);
        }

        @Override
        public void onBackPressed() {
            if(drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerClose();
            else
                super.onBackPressed();


        }
}

