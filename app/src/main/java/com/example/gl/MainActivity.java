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

    private DrawerLayout m_DrawerLayout;
    private NavigationView m_NavigationView;
    private final ManagerApp m_ManagerApp = new ManagerApp();
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        m_DrawerLayout = findViewById(R.id.drawer_layout);
        m_NavigationView = findViewById(R.id.Navigator_view);
        m_NavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                m_DrawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        m_DrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        if(savedInstanceState == null)
        {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, m_ManagerApp.GetMainMenu()).commit();

        }

    }

    @Override
    public void onBackPressed()
    {

        if (m_DrawerLayout.isDrawerOpen(GravityCompat.START))
        {

            m_DrawerLayout.closeDrawer(GravityCompat.START);

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  m_ManagerApp.GetMainMenu()).addToBackStack(null).commit();
                break;
            case R.id.navigator_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  m_ManagerApp.GetSearch()).addToBackStack(null).commit();
                break;
            case R.id.navigator_all_games:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,   m_ManagerApp.GetGames()).addToBackStack(null).commit();
                break;
            case R.id.navigator_top_games:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, m_ManagerApp.GetTopGames()).addToBackStack(null).commit();
                break;
            case R.id.navigator_cooming_soon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  m_ManagerApp.GetCoomingSoon()).addToBackStack(null).commit();
                break;
            case R.id.navigator_about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, m_ManagerApp.GetAboutUs()).addToBackStack(null).commit();
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

        m_DrawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    public boolean isServicesOK(){

        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        boolean isNotWorking = false;

        if (available == ConnectionResult.SUCCESS){

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