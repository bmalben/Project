package com.example.aimallapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimallapp.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


        });


        binding.appBarHome.fab.setVisibility(View.INVISIBLE);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.nav_home){
            Intent i = new Intent(getApplicationContext(),view_shop.class);
            startActivity(i);
        }
        if(id==R.id.nav_gallery){
            Intent i = new Intent(getApplicationContext(),view_notifications.class);
            startActivity(i);
        }
        if(id==R.id.nav_product){
            Intent i = new Intent(getApplicationContext(),view_product.class);
            startActivity(i);
        }
        if(id==R.id.nav_slideshow){
            Intent i = new Intent(getApplicationContext(),view_offer.class);
            startActivity(i);
        }
        if(id==R.id.nav_message){
            Intent i = new Intent(getApplicationContext(),help_message.class);
            startActivity(i);
        }
        if(id==R.id.nav_service){
            Intent i = new Intent(getApplicationContext(),view_service.class);
            startActivity(i);
        }
        if(id==R.id.nav_shop){
            Intent i = new Intent(getApplicationContext(),view_nearest_shop.class);
            startActivity(i);
        }
        if(id==R.id.nav_feedback){
            Intent i = new Intent(getApplicationContext(),feedback.class);
            startActivity(i);
        }
        if(id==R.id.scan){
            Intent i = new Intent(getApplicationContext(),Scanqr.class);
            startActivity(i);
        }
        if(id==R.id.logout){
            sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor ed= sh.edit();
            ed.commit();
            ed.clear();
            Intent i = new Intent(getApplicationContext(),ip_page.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            Intent ij=new Intent(getApplicationContext(), macservice.class);
            stopService(ij);
            Intent ik=new Intent(getApplicationContext(), Locationservice.class);
            stopService(ik);

        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),Home.class);
        startActivity(i);
    }
}