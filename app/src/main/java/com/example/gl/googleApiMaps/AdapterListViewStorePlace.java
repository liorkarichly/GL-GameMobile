package com.example.gl.googleApiMaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gl.R;

import java.util.List;

public class AdapterListViewStorePlace extends ArrayAdapter<StoresPlaces>
{
    public AdapterListViewStorePlace(@NonNull Context context, @NonNull List<StoresPlaces> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null)
        {

            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.card_view_for_list_view_store_place, parent, false);

        }

        // get the position of the view from the ArrayAdapter
        StoresPlaces storesPlaces = getItem(position);

        // then according to the position of the view assign the desired image for the same

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textViewName = currentItemView.findViewById(R.id.name_store);
        textViewName.setText(storesPlaces.getName());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textViewDistance = currentItemView.findViewById(R.id.distance_store_from_me);
        textViewDistance.setText(String.format("%.2f km",storesPlaces.getDistance()));

        // then return the recyclable view
        return currentItemView;

    }

}
