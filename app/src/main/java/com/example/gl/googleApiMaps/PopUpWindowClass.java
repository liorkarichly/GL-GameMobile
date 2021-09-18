package com.example.gl.googleApiMaps;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.gl.R;
import com.google.android.gms.maps.model.LatLng;

public class PopUpWindowClass
{

    @SuppressLint("ResourceAsColor")
    public static void ShowPopUpWindow(final View i_View, PlaceByIDModel m_PlaceModelByID, LatLng i_LocationPlace) {

        LayoutInflater inflater = (LayoutInflater)i_View.getContext().getSystemService(i_View.getContext().LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.info_window_place,null);


        boolean focuseAble = true;

        final PopupWindow popupWindow = new PopupWindow(popUpView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, focuseAble);
        popupWindow.showAtLocation(i_View, Gravity.CENTER, 0, 0);

        TextView textName = popUpView.findViewById(R.id.name_of_place);
        TextView textAddress = popUpView.findViewById(R.id.address_of_name);
        TextView textPhone = popUpView.findViewById(R.id.phone_number_of_place);
        TextView textWeb = popUpView.findViewById(R.id.web_site_of_place);
        TextView textRating = popUpView.findViewById(R.id.rating_of_place);
        TextView textRatingTotal = popUpView.findViewById(R.id.total_rating_of_place);
        TextView textStatus= popUpView.findViewById(R.id.status_opening_of_place);
        Button buttonWaze = popUpView.findViewById(R.id.click_waze);

            textName.setText( m_PlaceModelByID.getName());
            textAddress.setText(m_PlaceModelByID.getFormatted_address());
            textPhone.setText(m_PlaceModelByID.getFormatted_phone_number());
            textWeb.setText(m_PlaceModelByID.getWebsite());
            textRating.setText(String.valueOf(m_PlaceModelByID.getRating()));
            textRatingTotal.setText(String.valueOf(m_PlaceModelByID.getUser_ratings_total()));


        try{

            boolean openNow = m_PlaceModelByID.getOpening_hours().isOpen_now();
            String solutionOpenning = "Open";

            if (!openNow)
            {

                solutionOpenning = "Close";
                textStatus.setTextColor(Color.RED);

            }
            else
            {

                textStatus.setTextColor(Color.GREEN);

            }

            textStatus.setText(solutionOpenning);

        }
        catch (NullPointerException ex){

            textStatus.setText("null");
            Log.i("TAG", "ShowPopUpWindow: " + ex.getMessage());

        }




        buttonWaze.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Log.d("TAG", "onClick: WAZE");

                try{

                    String url = "https://waze.com/ul?ll= " + i_LocationPlace.latitude + "," + i_LocationPlace.longitude + "&navigate=yes";
                    Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                   view.getContext().startActivity(intent);

                }catch (ActivityNotFoundException ex)
                {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                    view.getContext().startActivity(intent);

                }

            }

        });

    popUpView.setOnTouchListener((view, motionEvent) -> {
         popupWindow.dismiss();
         return true;
    });
    }


}
