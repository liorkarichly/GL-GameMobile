package com.example.gl.googleApiMaps;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseFromJson
{

    private static final String TAG = "ParseFromJson";

    public static PlaceByIDModel parserJsonObjectsToPlaceModel(JSONObject i_JsonObject){

        //gets a full json object response

        PlaceByIDModel placeIDModel = new PlaceByIDModel();
        PlaceOpeningHours openingHours = new PlaceOpeningHours();
        JSONObject jsonObjectResult = null;

        try{

            //extracts the result field from the full response
            jsonObjectResult = i_JsonObject.getJSONObject("result");

            //assign parameters to object model
            placeIDModel.setName(jsonObjectResult.getString("name"));
            placeIDModel.setFormatted_address(jsonObjectResult.getString("formatted_address"));
            placeIDModel.setFormatted_phone_number(jsonObjectResult.getString("formatted_phone_number"));
            placeIDModel.setUser_ratings_total(jsonObjectResult.getInt("user_ratings_total"));
            placeIDModel.setRating(jsonObjectResult.getDouble("rating"));
            openingHours.setOpen_now(jsonObjectResult.getJSONObject("opening_hours").getBoolean("open_now"));
            placeIDModel.setOpening_hours(openingHours);
            placeIDModel.setWebsite(jsonObjectResult.getString("website"));

        }
        catch (JSONException e)
        {

            e.printStackTrace();

        }

        return placeIDModel;

    }

    public List<StoresPlaces> ParseResultsStorePlace(JSONObject i_JsonObject)
    {

        //Initialize json array
        JSONArray jsonArray = null;

        try
        {

            //Get results
            jsonArray = i_JsonObject.getJSONArray("results");

        }
        catch (JSONException e)
        {

            e.printStackTrace();

        }

        List<StoresPlaces> storesPlacesList =   parserJsonArrayStorePlace(jsonArray);

        return storesPlacesList;

    }

    private List<StoresPlaces> parserJsonArrayStorePlace(JSONArray i_JsonArray){

        //Initialze list store places
        List<StoresPlaces> dataList = new ArrayList<>();

        for (int i = 0; i < i_JsonArray.length(); i++)
        {

            try
            {

                //Initialize store place
                StoresPlaces data = parserJsonObjectsStorePlace((JSONObject)i_JsonArray.get(i));
                Log.d(TAG, "parserJsonArray: " + data.toString());

                //Add data i hash map list
                dataList.add(data);

            }
            catch (JSONException e)
            {

                e.printStackTrace();

            }

        }

        //Return list Store Places
        return dataList;

    }

    private StoresPlaces parserJsonObjectsStorePlace(JSONObject i_JsonObject){

        StoresPlaces storesPlaces = new StoresPlaces();
        Geometry geometry = new Geometry();
        Location location = new Location();

        try{

            String name = i_JsonObject.getString("name");
            String place_id = i_JsonObject.getString("place_id");
            String vicinity = i_JsonObject.getString("vicinity");
            String latitude = i_JsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String longitude = i_JsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng");

            storesPlaces.setName(name);
            storesPlaces.setPlace_id(place_id);
            storesPlaces.setVicinity(vicinity);

            location.latitude = latitude;
            location.longitude = longitude;
            geometry.setLocation(location);// location into to geometry
            storesPlaces.setGeometry(geometry);// geometry into to store place

        }
        catch (JSONException e)
        {

            e.printStackTrace();

        }

        return storesPlaces;

    }

}
