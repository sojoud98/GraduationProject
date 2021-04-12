package com.example.grduationproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    FirebaseAuth firebaseAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CardView cookingCard, cleaningCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        cleaningCard = findViewById(R.id.cleaning);
        cookingCard = findViewById(R.id.cooking);
        cleaningCard.setOnClickListener(this);
        cookingCard.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(ActivityHome.this, ActivityHome.class));

                break;
            case R.id.nav_profile:

                startActivity(new Intent(ActivityHome.this, Activity_profile.class));
                break;
                case R.id.nav_join:

                startActivity(new Intent(ActivityHome.this, Activity_apply.class));
                break;
            case R.id.nav_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(ActivityHome.this, MainActivity.class));

                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cooking:

                startActivity(new Intent(ActivityHome.this, ActivityCooking.class));
                break;

            case R.id.cleaning:
                startActivity(new Intent(ActivityHome.this, ActivityCleaning.class));
                break;

                default:
                break;
        }
    }
}