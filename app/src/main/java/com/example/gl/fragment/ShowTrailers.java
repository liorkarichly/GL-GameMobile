package com.example.gl.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    private List<TrailersModel> showTrailers;
    private TextView m_TextFromWeb;
    private VideoView m_VideoView;
    private ListView m_ListViewForVideo;
    private List<String> m_ListViewNames = new ArrayList<>();
    private ArrayAdapter m_AdapterStringsName;



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

            m_ListViewForVideo = (ListView)root.findViewById(R.id.list_view_movie);
            m_VideoView = (VideoView)root.findViewById(R.id.trailer_movie);
            m_TextFromWeb = (TextView)root.findViewById(R.id.rawg_api_web);
            MediaController mediaController = new MediaController(root.getContext());

            showTrailers = (List<TrailersModel>) getArguments().getSerializable(KEY_SHOW_TRAILER);
            m_ListViewNames.clear();

            for (TrailersModel trailersModel : showTrailers) {

                Log.d("Name", trailersModel.getName());
                Log.d("ID", String.valueOf(trailersModel.getId()));
                Log.d("Data", String.valueOf(trailersModel.getData().getMax()));
                m_ListViewNames.add(trailersModel.getName());


            }

            m_AdapterStringsName = new ArrayAdapter(root.getContext(), R.layout.trailer_text_view, m_ListViewNames);
            m_ListViewForVideo.setAdapter(m_AdapterStringsName);

            m_ListViewForVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              m_VideoView.setVideoURI(Uri.parse(showTrailers.get(i).getData().getMax()));
              m_VideoView.setMediaController(mediaController);
              mediaController.setAnchorView(m_VideoView);
              m_VideoView.requestFocus();
              m_VideoView.start();

            }
        });

        }

        m_TextFromWeb.setOnClickListener(view -> GoToURL(m_TextFromWeb.getText().toString()));

        return root;
    }

    void GoToURL(String url)
    {

        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }
}