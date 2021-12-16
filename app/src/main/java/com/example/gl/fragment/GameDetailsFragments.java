package com.example.gl.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.gl.R;
import com.example.gl.model.GameSingleModel;
import com.example.gl.model.TrailersModel;
import com.example.gl.response.IMyCallbackTrailers;
import com.example.gl.response.ResponseFromAPI;
import com.example.gl.response.ResponsePlatformsForGameSingleModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameDetailsFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameDetailsFragments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String KEY_GAME_BY_ID = "GAME_BY_ID";
    private final String KEY_SHOW_TRAILER = "SHOW_TRAILER";

    private GameSingleModel mGameSingleDetails;
    private ImageView mProfileGame;
    private TextView mNameGame;
    private TextView mRatingGame;
    private TextView mAboutGame;
    private TextView mReleasedGame;
    private TextView mWebGame;
    private TextView mPlaytimeGame;
    private TextView mPlatformGame;
    private TextView mSeriesCountGame;
    private List<TrailersModel> mTrailers = new ArrayList<>();
    private Button mLinkToTrailer;


    // TODO: Rename and change types of parameters

    public GameDetailsFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment GameDetailsFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static GameDetailsFragments newInstance()
    {

        GameDetailsFragments fragment = new GameDetailsFragments();

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_game_details_fragments, container, false);

        mLinkToTrailer = (Button)root.findViewById(R.id.link_to_show_trailers);
        mNameGame = (TextView)root.findViewById(R.id.name_game_details);
        mPlatformGame = (TextView)root.findViewById(R.id.platform_list_game_details);
        mRatingGame = (TextView)root.findViewById(R.id.rating_game_details);
        mReleasedGame = (TextView)root.findViewById(R.id.released_game_details);
        mPlatformGame = (TextView)root.findViewById(R.id.platform_list_game_details);
        mWebGame = (TextView)root.findViewById(R.id.web_game_details);
        mAboutGame = (TextView)root.findViewById(R.id.about_game_details);
        mPlaytimeGame = (TextView)root.findViewById(R.id.playtime_game_details);
        mSeriesCountGame = (TextView)root.findViewById(R.id.game_series_count_details);
        mProfileGame = (ImageView)root.findViewById(R.id.image_profile_details);
        mTrailers.clear();

        //Get details from user and get the key
        savedInstanceState = this.getArguments();

        if (savedInstanceState != null)
        {

            try
            {

                mGameSingleDetails = (GameSingleModel) savedInstanceState.getSerializable(KEY_GAME_BY_ID);

                String profileImageURL = mGameSingleDetails.getBackground_image();

                //Source image
                if (profileImageURL != null) {

                    Picasso.with(container.getContext()).load(profileImageURL).resize(512,512).into(mProfileGame);

                }
                else// Image Null
                {

                    //Null image
                    Picasso.with(container.getContext()).load(R.mipmap.ic_null_image).into(mProfileGame);

                }

                //Name Game
                mNameGame.setText(mGameSingleDetails.getName());

                //Ratings
                mRatingGame.setText(mGameSingleDetails.getRating().toString());

                //Release Data
                mReleasedGame.setText(mGameSingleDetails.getReleased());

                //Web Link
                mWebGame.setText(mGameSingleDetails.getWebsite());

                //Play Time Game In Hours
                mPlaytimeGame.setText(mGameSingleDetails.getPlaytime().toString());

                //Number Count Of Games From Same Series
                mSeriesCountGame.setText(mGameSingleDetails.getGame_series_count().toString());

                //Descriptions About The Game
                mAboutGame.setText(Html.fromHtml(mGameSingleDetails.getDescription()));

                //Checking if platform is empty
                if (mGameSingleDetails.getPlatforms() != null) {

                    StringBuilder platformBuilder = new StringBuilder();

                    for (ResponsePlatformsForGameSingleModel platformModel : mGameSingleDetails.getPlatforms()) {

                        if (platformModel.getPlatform().getName() != null) {

                            platformBuilder.append(" " + platformModel.getPlatform().getName() + ",");

                        }

                    }

                    platformBuilder.setLength(platformBuilder.length() - 1);
                    mPlatformGame.setText(platformBuilder);
                }

                //When the user click on button of Trailer so we request get from api for list of trailers of specific game
                ResponseFromAPI.getInstance().GetTrailersOfGame(new IMyCallbackTrailers() {

                    @Override
                    public void onSuccessTrailers(@NonNull List<TrailersModel> trailer)  {

                        mTrailers.addAll(trailer);

                        //Checking if the list of trailer is empty or not for present thebutton for access trailer
                        if (mTrailers.size() != 0)
                        {


                            mLinkToTrailer.setVisibility(root.VISIBLE);
                            Log.d("Trailer", mLinkToTrailer.getText().toString());

                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable throwable)
                    {

                        Toast.makeText(container.getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG);

                    }

                }, String.valueOf(mGameSingleDetails.getId()));



            }
            catch (Exception e)
            {

                Toast.makeText(container.getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG);

            }

        }

        //Listener for button trailer (if its not empty) and moves to fragment
mLinkToTrailer.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        Bundle bundel = new Bundle();
        ShowTrailers showTrailers = new ShowTrailers();
        bundel.putSerializable(KEY_SHOW_TRAILER, (Serializable) mTrailers);
        showTrailers.setArguments(bundel);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, showTrailers).addToBackStack(null).commit();

    }
});

        //Link website click
        mWebGame.setOnClickListener(v -> {

            GoToURL(mWebGame.getText().toString());

        });

        return root;

    }

    void GoToURL(String url)
    {

        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }

}