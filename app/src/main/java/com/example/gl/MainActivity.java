package com.example.gl;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gl.mainActivity.ManagerApp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private final ManagerApp mManagerApp = new ManagerApp();
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.Navigator_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        if(savedInstanceState == null)
        {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mManagerApp.GetMainMenu()).commit();

        }

    }

    @Override
    public void onBackPressed()
    {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {

            mDrawerLayout.closeDrawer(GravityCompat.START);

        }
        else
        {

            super.onBackPressed();

        }

    }

    //Open the fragment item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {

        switch (item.getItemId())
        {

            case R.id.navigator_main_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  mManagerApp.GetMainMenu()).addToBackStack(null).commit();
                break;
            case R.id.navigator_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  mManagerApp.GetSearch()).addToBackStack(null).commit();
                break;
            case R.id.navigator_all_games:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,   mManagerApp.GetGames()).addToBackStack(null).commit();
                break;
            case R.id.navigator_top_games:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mManagerApp.GetTopGames()).addToBackStack(null).commit();
                break;
            case R.id.navigator_cooming_soon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  mManagerApp.GetCoomingSoon()).addToBackStack(null).commit();
                break;
            case R.id.navigator_about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mManagerApp.GetAboutUs()).addToBackStack(null).commit();
                break;
            case R.id.navigator_store:
                if (isServicesOK())
                {

                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                    break;

                }
                else
                {

                    Toast.makeText(this, "Try Later", Toast.LENGTH_LONG).show();

                }

                    break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    /** Get access to google maps api*/
    public boolean isServicesOK()
    {

        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        boolean isNotWorking = false;

        if (available == ConnectionResult.SUCCESS)
        {

            Log.d(TAG, "isServicesOK: Google play services is working");
            return !isNotWorking;

        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {

            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability
                    .getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);

            dialog.show();

        }
        else
        {

            Toast.makeText(this, "You cant make map requests", Toast.LENGTH_SHORT).show();

        }

        return isNotWorking;

    }

}