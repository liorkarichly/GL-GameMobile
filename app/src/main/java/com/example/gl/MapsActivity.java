package com.example.gl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gl.databinding.ActivityMapsBinding;
import com.example.gl.googleApiMaps.AdapterListViewStorePlace;
import com.example.gl.googleApiMaps.IOnFinishedListenerCallback;
import com.example.gl.googleApiMaps.ParseFromJson;
import com.example.gl.googleApiMaps.PlaceByIDModel;
import com.example.gl.googleApiMaps.PopUpWindowClass;
import com.example.gl.googleApiMaps.SortByDistance;
import com.example.gl.googleApiMaps.StoresPlaces;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
        , View.OnClickListener
{

    public static final String TAG = "MapsActivity";
    public static final String PLACE_TYPE = "store";
    public static final String KEYWORD_TYPE = "gaming";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private ListView m_ListViewPlaces;
    private List<StoresPlaces> m_StoresPlaceByMyArea = new ArrayList<>();
    private FusedLocationProviderClient m_FusedLocationProviderClient;
    private boolean m_LocationPermissionsGranted = false;
    private double m_CurrentLat = 0;
    private double m_CurrentLong = 0;

    private PlaceByIDModel m_PlaceByID;
    private int m_AmountSelectedRange = 20000;//20*1000 km is default
    private SeekBar m_SeekBar;
    private Button m_ButtonSelectedRangeFromSeekBar;
    private TextView m_TextOfRange;
    private MarkerOptions markerOptions;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location m_LastKnownLocation;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private View m_ViewPopUp;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getLocationPermission();

        }

        // Set up the action toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_maps);
        setSupportActionBar(toolbar);

        // Set up the views
        m_ListViewPlaces = findViewById(R.id.listPlaces);
        m_SeekBar = findViewById(R.id.seekbar_range);
        m_ButtonSelectedRangeFromSeekBar = findViewById(R.id.click_search_by_range);
        m_TextOfRange = findViewById(R.id.text_of_range);

        m_TextOfRange.setText(m_AmountSelectedRange/1000 + "/" + m_SeekBar.getMax() + " km");
        m_SeekBar.setProgress(m_AmountSelectedRange/1000);
        m_ButtonSelectedRangeFromSeekBar.setOnClickListener(this);
        m_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                int forShow = i;
                m_TextOfRange.setText(forShow + "/" + seekBar.getMax() + " km");

                Log.d(TAG, "onProgressChanged: " + i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                m_AmountSelectedRange = seekBar.getProgress();
                m_AmountSelectedRange = m_AmountSelectedRange*1000;
                Log.d(TAG, "onStopTrackingTouch: " + m_AmountSelectedRange);

            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "Map is ready!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (m_LocationPermissionsGranted) {

            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            mMap.setMyLocationEnabled(true);//enable icon of my location
            mMap.getUiSettings().setMyLocationButtonEnabled(false);// disable to button that move me to my location

        }

    }

    private void initMap() {


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.drawer_menu_map, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
/** that pickCurrentPlace is defined, find the line
 * in onOptionsItemSelected() that calls
 * pickCurrentPlace and uncomment it.
 * */
            case R.id.action_geolocate:

                // COMMENTED OUT UNTIL WE DEFINE THE METHOD
                // Present the current place picker
                pickCurrentPlace();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

        Log.d(TAG, "getLocationPermission: getting location permissions");

        String[] permission = {FINE_LOCATION, COURSE_LOCATION};
        m_LocationPermissionsGranted = false;

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                m_LocationPermissionsGranted = true;
                Log.d(TAG, "getLocationPermission: init");
                initMap();

            } else {

                ActivityCompat.requestPermissions(this
                        , permission
                        , PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }

        } else {

            ActivityCompat.requestPermissions(this
                    , permission
                    , PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }

    }

    /**
     * Handles the result of the request for location permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: Enter");
        m_LocationPermissionsGranted = false;

        switch (requestCode) {

            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {

                    Log.d(TAG, "onRequestPermissionsResult: PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION");

                    for (int i = 0; i < grantResults.length; i++) {

                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                            m_LocationPermissionsGranted = false;
                            return;

                        }

                    }

                    m_LocationPermissionsGranted = true;
                    initMap();

                }

            }

        }

    }

    /**
     * If the user has granted permission, then the method calls getDeviceLocation
     * to initiate the process of getting the current likely places.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        m_FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d(TAG, "getDevicesLocation: getting the devices current location");


        try {

            Log.d(TAG, "getDevicesLocation: getting the devices current location 2");

            if (m_LocationPermissionsGranted) {

                if (ActivityCompat.checkSelfPermission(this
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this
                        , Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.d(TAG, "getDevicesLocation: getting the devices current location 2");
                    return;

                }


                Task<Location> locationResult = m_FusedLocationProviderClient.getLastLocation();

                locationResult.addOnSuccessListener(location -> {

                    if (location != null) {

                        m_CurrentLat = location.getLatitude();
                        m_CurrentLong = location.getLongitude();

                        moveCamera(new LatLng(m_CurrentLat
                                        , m_CurrentLong)
                                , DEFAULT_ZOOM, "My location");

                        Log.d(TAG, "onSuccess: Init marker on the maps and find near by place");

                        FindNearByPlaces();

                    }

                });

            }

        } catch (SecurityException e) {

            Log.d(TAG, "getDeviceLocation: ");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Exception: " + e.getMessage());

        }

    }

    /**
     * When the user taps Pick Place, this method checks for location
     * permissions and prompts the user for permission if it hasn't been granted.
     **/
    private void pickCurrentPlace() {

        if (mMap == null) {

            return;

        }

        if (m_LocationPermissionsGranted) {

            getDeviceLocation();

        } else {

            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();

        }

    }

    /**
     * move to my location the camera
     */
    private void moveCamera(LatLng i_LatLng, float i_Zoom, String i_Title)
    {

        Log.d(TAG, "moveCamera: moving the camera to lat: " + i_LatLng.latitude + ", lng:" + i_LatLng.longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(i_LatLng, i_Zoom));//move to my location with zoom in


        if (!i_Title.equals("My location"))
        {

            markerOptions.title(i_Title).position(i_LatLng);

        }

        hideSoftKeyboard();

    }

    private void hideSoftKeyboard()
    {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public void FindNearByPlaces()
    {

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + m_CurrentLat + "," + m_CurrentLong +
                "&radius=" + m_AmountSelectedRange +
                "&keyword=" + KEYWORD_TYPE +
                "&type=" + PLACE_TYPE +
                "&sensor=true" +
                "&key=" + getString(R.string.api_key_google);

        Log.d(TAG, "FindNearByPlaces: " + m_AmountSelectedRange);
        new PlaceTask(false, null).execute(url);

    }

    private String getStoreDetails(String i_placeID) throws IOException, JSONException
    {

        String url = "https://maps.googleapis.com/maps/api/place/details/json" +
                "?place_id=" + i_placeID +
                "&fields=name,rating,formatted_phone_number,formatted_address,website,opening_hours,user_ratings_total" +
                "&key=" + getString(R.string.api_key_google);

        return url;

    }

    /**
     * Let's think about what we want to happen when the user clicks an item in the ListView.
     * To confirm the user's choice of which place they are currently,
     * you can add a marker to the map at that place.
     * If the user clicks that marker,
     * an info window pops up displaying the place name and address.
     */
    private AdapterView.OnItemClickListener listClickedHandler =
            new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView parent, View v, int position, long id)
                {

                    m_ViewPopUp = v;
                    // position will give us the index of which place was selected in the array

                    LatLng markerLatLng = new LatLng(Double.parseDouble(m_StoresPlaceByMyArea.get(position).getGeometry().getLocation().getLatitude()),
                            Double.parseDouble( m_StoresPlaceByMyArea.get(position).getGeometry().getLocation().getLongitude()));

                   String markerName = m_StoresPlaceByMyArea.get(position).getName();
//
//                    // Position the map's camera at the location of the marker.
                    moveCamera(markerLatLng, DEFAULT_ZOOM, markerName);

                    try
                    {

                        String url = getStoreDetails(m_StoresPlaceByMyArea.get(position).getPlace_id());

                        new PlaceTask(true, new IOnFinishedListenerCallback()
                        {

                            @Override
                            public void onFinished(PlaceByIDModel results)
                            {

                                try
                                {

                                    m_PlaceByID = results;
                                    Log.d(TAG, "onFinished: Popup");
                                    PopUpWindowClass.ShowPopUpWindow(binding.getRoot(), results, markerLatLng);

                                }
                                catch (Exception ex)
                                {

                                    Log.d(TAG, "onFinished: ");
                                    Toast.makeText(MapsActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();

                                }

                            }

                        }).execute(url);

                    }
                    catch (JSONException e)
                    {

                        e.printStackTrace();

                    }
                    catch (IOException ioException)
                    {

                        ioException.printStackTrace();

                    }

                }

            };

    /**
     * Now that you have your list of most-likely places that the user is currently visiting,
     * you can present those options to the user in the ListView.
     * you can also set the ListView click listener to use the click handler you just defined.
     */
    private void getCurrentPlaceLikelihoods()
    {

        if (ActivityCompat.checkSelfPermission(MapsActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

        }

        AdapterListViewStorePlace adapterListViewStorePlace = new AdapterListViewStorePlace(this, m_StoresPlaceByMyArea);

        m_ListViewPlaces.setAdapter(adapterListViewStorePlace);
        m_ListViewPlaces.setOnItemClickListener(listClickedHandler);

    }

    /**
     * Click listener on button range and call back the place in radius
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view)
    {

        if (view.getId() == R.id.click_search_by_range)
        {

            getDeviceLocation();
            m_TextOfRange.setText(m_AmountSelectedRange/1000 + "/" + m_SeekBar.getMax() + " km");

        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class PlaceTask extends AsyncTask<String, Integer, String>
    {

        private boolean m_PlaceByIdOrNot;
        private IOnFinishedListenerCallback m_FinishedListenre;

        public PlaceTask(boolean i_PlaceByIdOrNot, @NonNull IOnFinishedListenerCallback i_FinishedListener)
        {

            m_FinishedListenre = i_FinishedListener;
            m_PlaceByIdOrNot = i_PlaceByIdOrNot;

        }

        @Override
        protected String doInBackground(String... strings)
        {

            String data = null;

            try
            {

                data = downloadUrl(strings[0]);

            }
            catch (IOException ioException)
            {

                ioException.printStackTrace();

            }

            return data;

        }

        @Override
        protected void onPostExecute(String s)
        {

            doingExecute(s, m_PlaceByIdOrNot, m_FinishedListenre);

        }

    }

    /**
     * Call to function that return List Place or Details By Place ID
     */
    private void doingExecute(String response, boolean paserPlaceOrNot, @NonNull IOnFinishedListenerCallback i_Finished) {

        if (paserPlaceOrNot)
        {

            if (i_Finished != null)
            {

                i_Finished.onFinished(GetPlaceDetailsByID(response));

            }

        }
        else
        {

            new ParserTask().execute(response);

        }

    }

    /**
     * Return Place Details By ID
     */
    private PlaceByIDModel GetPlaceDetailsByID(String response) {

        JSONObject jsonObject;

        PlaceByIDModel placeByIDModel = new PlaceByIDModel();

        try {

            jsonObject = new JSONObject(response);

            placeByIDModel = ParseFromJson.parserJsonObjectsToPlaceModel(jsonObject);

        } catch (JSONException jsonException) {

            Log.d(TAG, "GetPlaceDetailsByID: ");
            Toast.makeText(this, jsonException.getMessage(), Toast.LENGTH_LONG).show();

        }

        return placeByIDModel;

    }

    private String downloadUrl(String i_Url) throws IOException {

        URL urlString = new URL(i_Url);
        HttpURLConnection connection = (HttpURLConnection) urlString.openConnection();
        connection.connect();

        InputStream inputStream = connection.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder stringBuilder = new StringBuilder();

        String line = "";

        while ((line = bufferedReader.readLine()) != null) {

            stringBuilder.append(line);

        }

        String data = stringBuilder.toString();
        bufferedReader.close();

        return data;

    }

    private class ParserTask extends AsyncTask<String, Integer, List<StoresPlaces>> {

        @Override
        protected List<StoresPlaces> doInBackground(String... strings) {

            //Create json parser
            ParseFromJson parseFromJson = new ParseFromJson();

            //Initialize hash map
            List<StoresPlaces> mapList = null;
            JSONObject jsonObject = null;

            try {

                //Initialize json object
                jsonObject = new JSONObject(strings[0]);

                //Parse json object
                mapList = parseFromJson.ParseResultsStorePlace(jsonObject);


            } catch (JSONException e) {

                e.printStackTrace();

            }

            return mapList;

        }

        @SuppressLint("DefaultLocale")
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(List<StoresPlaces> storesPlaces)
        {

            //Clear map
            mMap.clear();

            m_StoresPlaceByMyArea = storesPlaces;

            for (int i = 0; i < m_StoresPlaceByMyArea.size() ; i++)
            {

                float results[] = new float[10];
                Location.distanceBetween(m_CurrentLat, m_CurrentLong
                        , Double.parseDouble(m_StoresPlaceByMyArea.get(i).getGeometry().getLocation().getLatitude())
                        , Double.parseDouble(m_StoresPlaceByMyArea.get(i).getGeometry().getLocation().getLongitude())
                        , results);//Return the distance in km

                m_StoresPlaceByMyArea.get(i).setDistance(results[0]/1000);

                //Initialize marker opthions
                markerOptions = new MarkerOptions();

                //Set options and title
                markerOptions
                        .position(new LatLng( Double.parseDouble(m_StoresPlaceByMyArea.get(i).getGeometry().getLocation().getLatitude()),
                                Double.parseDouble(m_StoresPlaceByMyArea.get(i).getGeometry().getLocation().getLongitude())))
                        .title(m_StoresPlaceByMyArea.get(i).getName())
                        .snippet(String.format("Distance: %.2f km" , m_StoresPlaceByMyArea.get(i).getDistance()));

                mMap.addMarker(markerOptions);

            }


            Collections.sort(m_StoresPlaceByMyArea, new SortByDistance());//sort by distance
            getCurrentPlaceLikelihoods();

        }

    }

}