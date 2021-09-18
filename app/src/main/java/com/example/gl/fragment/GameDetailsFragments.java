package com.example.gl.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.gl.R;
import com.example.gl.model.DataModel;
import com.example.gl.model.GameSingleModel;
import com.example.gl.model.TrailersModel;
import com.example.gl.response.IMyCallback;
import com.example.gl.response.IMyCallbackTrailers;
import com.example.gl.response.ResponseFromAPI;
import com.example.gl.response.ResponsePlatformsForGameSingleModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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

    private GameSingleModel m_GameSingleDetails;
    private ImageView m_ProfileGame;
    private TextView m_NameGame;
    private TextView m_RatingGame;
    private TextView m_AboutGame;
    private TextView m_ReleasedGame;
    private TextView m_WebGame;
    private TextView m_PlaytimeGame;
    private TextView m_PlatformGame;
    private TextView m_SeriesCountGame;
    private List<TrailersModel> trailers = new ArrayList<>();

    private Button m_LinkToTrailer;


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

        m_LinkToTrailer = (Button)root.findViewById(R.id.link_to_show_trailers);
        m_NameGame = (TextView)root.findViewById(R.id.name_game_details);
        m_PlatformGame = (TextView)root.findViewById(R.id.platform_list_game_details);
        m_RatingGame = (TextView)root.findViewById(R.id.rating_game_details);
        m_ReleasedGame = (TextView)root.findViewById(R.id.released_game_details);
        m_PlatformGame = (TextView)root.findViewById(R.id.platform_list_game_details);
        m_WebGame = (TextView)root.findViewById(R.id.web_game_details);
        m_AboutGame = (TextView)root.findViewById(R.id.about_game_details);
        m_PlaytimeGame = (TextView)root.findViewById(R.id.playtime_game_details);
        m_SeriesCountGame = (TextView)root.findViewById(R.id.game_series_count_details);
        m_ProfileGame = (ImageView)root.findViewById(R.id.image_profile_details);
        trailers.clear();

        savedInstanceState = this.getArguments();

        if (savedInstanceState != null)
        {

            try
            {

                m_GameSingleDetails = (GameSingleModel) savedInstanceState.getSerializable(KEY_GAME_BY_ID);

                String profileImageURL = m_GameSingleDetails.getBackground_image();

                //Image
                if (profileImageURL != null) {

                    Picasso.with(container.getContext()).load(profileImageURL).resize(512,512).into(m_ProfileGame);

                }
                else// Image Null
                {

                    //Null image
                    Picasso.with(container.getContext()).load(R.mipmap.ic_null_image).into(m_ProfileGame);

                }

                //Name Game
                m_NameGame.setText(m_GameSingleDetails.getName());

                //Ratings
                m_RatingGame.setText(m_GameSingleDetails.getRating().toString());

                //Release Data
                m_ReleasedGame.setText(m_GameSingleDetails.getReleased());

                //Web Link
                m_WebGame.setText(m_GameSingleDetails.getWebsite());

                //Play Time Game In Hours
                m_PlaytimeGame.setText(m_GameSingleDetails.getPlaytime().toString());

                //Number Count Of Games From Same Series
                m_SeriesCountGame.setText(m_GameSingleDetails.getGame_series_count().toString());

                //Descriptions About The Game
                m_AboutGame.setText(Html.fromHtml(m_GameSingleDetails.getDescription()));

                //Checking if platform is empty
                if (m_GameSingleDetails.getPlatforms() != null) {

                    StringBuilder platformBuilder = new StringBuilder();

                    for (ResponsePlatformsForGameSingleModel platformModel : m_GameSingleDetails.getPlatforms()) {

                        if (platformModel.getPlatform().getName() != null) {

                            platformBuilder.append(" " + platformModel.getPlatform().getName() + ",");

                        }

                    }

                    platformBuilder.setLength(platformBuilder.length() - 1);
                    m_PlatformGame.setText(platformBuilder);
                }


                ResponseFromAPI.getInstance().GetTrailersOfGame(new IMyCallbackTrailers() {

                    @Override
                    public void onSuccessTrailers(@NonNull List<TrailersModel> trailer)  {

                        trailers.addAll(trailer);

                        if (trailers.size() != 0)
                        {

                            m_LinkToTrailer.setVisibility(root.VISIBLE);
                            Log.d("Trailer", m_LinkToTrailer.getText().toString());

                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable throwable)
                    {

                        Toast.makeText(container.getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG);

                    }

                }, String.valueOf(m_GameSingleDetails.getId()));



            }
            catch (Exception e)
            {

                Toast.makeText(container.getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG);

            }

        }

m_LinkToTrailer.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        Bundle bundel = new Bundle();
        ShowTrailers showTrailers = new ShowTrailers();
        bundel.putSerializable(KEY_SHOW_TRAILER, (Serializable) trailers);
        showTrailers.setArguments(bundel);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, showTrailers).addToBackStack(null).commit();

    }
});

//        m_ListViewForVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//              m_VideoView.setVideoURI(Uri.parse(trailers.get(i).getData().getMax()));
//              m_VideoView.setMediaController(new MediaController(root.getContext()));
//              m_VideoView.requestFocus();
//              m_VideoView.start();
//
//            }
//        });

        m_WebGame.setOnClickListener(v -> {

            GoToURL(m_WebGame.getText().toString());

        });

        return root;

    }

    void GoToURL(String url)
    {

        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }

}