package com.example.gl.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.example.gl.R;
import com.example.gl.model.TrailersModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowTrailers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowTrailers extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_SHOW_TRAILER = "SHOW_TRAILER";

    // TODO: Rename and change types of parameters
    private List<TrailersModel> mShowTrailers;
    private TextView mTextFromWeb;
    private VideoView mVideoView;
    private ListView mListViewForVideo;
    private List<String> mListViewNames = new ArrayList<>();
    private ArrayAdapter mAdapterStringsName;

    public ShowTrailers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ShowTrailers.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowTrailers newInstance(String param1) {
        ShowTrailers fragment = new ShowTrailers();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_show_trailers, container, false);

        savedInstanceState = this.getArguments();
        if (savedInstanceState != null)
        {

            mListViewForVideo = (ListView)root.findViewById(R.id.list_view_movie);
            mVideoView = (VideoView)root.findViewById(R.id.trailer_movie);
            mTextFromWeb = (TextView)root.findViewById(R.id.rawg_api_web);
            MediaController mediaController = new MediaController(root.getContext());//Show the video

            mShowTrailers = (List<TrailersModel>) getArguments().getSerializable(KEY_SHOW_TRAILER);
            mListViewNames.clear();

            //Print the trailer in listView
            for (TrailersModel trailersModel : mShowTrailers) {

                Log.d("Name", trailersModel.getName());
                Log.d("ID", String.valueOf(trailersModel.getId()));
                Log.d("Data", String.valueOf(trailersModel.getData().getMax()));
                mListViewNames.add(trailersModel.getName());

            }

            mAdapterStringsName = new ArrayAdapter(root.getContext(), R.layout.trailer_text_view, mListViewNames);
            mListViewForVideo.setAdapter(mAdapterStringsName);

            mListViewForVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              mVideoView.setVideoURI(Uri.parse(mShowTrailers.get(i).getData().getMax()));
              mVideoView.setMediaController(mediaController);
              mediaController.setAnchorView(mVideoView);
              mVideoView.requestFocus();
              mVideoView.start();

            }
        });

        }

        mTextFromWeb.setOnClickListener(view -> GoToURL(mTextFromWeb.getText().toString()));

        return root;
    }

    void GoToURL(String url)
    {

        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }

}