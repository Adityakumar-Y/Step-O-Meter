package com.example.steptracker.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.steptracker.Fragments.DashboardFragment;
import com.example.steptracker.Fragments.GraphFragment;
import com.example.steptracker.Fragments.ProfileFragment;
import com.example.steptracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.navigation.ui.NavigationUI.onNavDestinationSelected;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private NavController navController;
    private final Fragment dashboardFragment = new DashboardFragment();
    private final Fragment graphFragment = new GraphFragment();
    private final Fragment profileFragment = new ProfileFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = dashboardFragment;
    private boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setupToolbar();
        setupBottomNav();
        /*navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                menuItem -> onNavDestinationSelected(menuItem, navController)
        );*/
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    private void setupBottomNav() {
        fm.beginTransaction().add(R.id.fragmentContainer, profileFragment, "3").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, graphFragment, "2").hide(graphFragment).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, dashboardFragment, "1").commit();
    }

   /* @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (DrawerLayout) null);
    }*/

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.dashboardFragment:
                if(!getSupportActionBar().isShowing()) {
                    getSupportActionBar().show();
                }
                fm.beginTransaction().hide(active).show(dashboardFragment).commit();
                active = dashboardFragment;
                return true;
            case R.id.graphFragment:
                if(!getSupportActionBar().isShowing()) {
                    getSupportActionBar().show();
                }
                fm.beginTransaction().hide(active).show(graphFragment).commit();
                active = graphFragment;
                return true;
            case R.id.profileFragment:
                if(getSupportActionBar().isShowing()) {
                    getSupportActionBar().hide();
                }
                fm.beginTransaction().hide(active).show(profileFragment).commit();
                active = profileFragment;
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(!doubleBackPressed) {
            doubleBackPressed = true;
            if(!getSupportActionBar().isShowing()) {
                getSupportActionBar().show();
            }
            getSupportFragmentManager().beginTransaction().hide(active).show(dashboardFragment).commit();
            active = dashboardFragment;
            new Handler().postDelayed(() -> {
                doubleBackPressed = false;
            }, 2000);
        }else{
            super.onBackPressed();
        }
    }

}

