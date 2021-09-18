package com.example.gl.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gl.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutUsFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters


    public AboutUsFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment AboutUsFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUsFragments newInstance() {
        AboutUsFragments fragment = new AboutUsFragments();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_about_us_fragments, container, false);
        TextView textView = (TextView)root.findViewById(R.id.text_about_us);

        textView.setOnClickListener(v -> GoToURL(textView.getText().toString()));// open web rawg.io

        return root;

    }

    //Open the Chrome and enter to web address
    void GoToURL(String url)
    {

        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }

}