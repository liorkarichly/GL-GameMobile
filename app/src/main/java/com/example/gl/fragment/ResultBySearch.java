package com.example.gl.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.gl.R;
import com.example.gl.mainActivity.ManagerCallsFromAPI;
import com.example.gl.mainActivity.eExtantionType;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultBySearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultBySearch extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String KEY_STRING = "SEARCH_QUERY";
    private final String KEY_HASH = "HASHMAP";
    // TODO: Rename and change types of parameters

    private HashMap<eExtantionType,String> mHashMap = null;
    private String mSearch = null;
    private NestedScrollView mNestedScrollView;
    private ManagerCallsFromAPI mManagerCallsFromAPI = new ManagerCallsFromAPI();
    private int mNextPage = 1;

    public ResultBySearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ResultBySearch.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultBySearch newInstance() {
        ResultBySearch fragment = new ResultBySearch();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_result_by_search, container, false);
        mNestedScrollView = root.findViewById(R.id.nested_scrool_view);
        mManagerCallsFromAPI.getM_ListGameModel().clear();

        savedInstanceState = this.getArguments();

        // Checking the key from search fragment and main menu for result list of game
        if (savedInstanceState != null)
        {

            mSearch = savedInstanceState.getString(KEY_STRING);
            mHashMap = (HashMap<eExtantionType, String>) savedInstanceState.getSerializable(KEY_HASH);

        }

        mManagerCallsFromAPI.CallToFilterSearchGame(mNextPage, root, container, mHashMap, mSearch);

        return root;

    }

}