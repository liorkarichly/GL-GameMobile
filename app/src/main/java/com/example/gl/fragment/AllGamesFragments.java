package com.example.gl.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.gl.R;
import com.example.gl.mainActivity.ManagerCallsFromAPI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllGamesFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllGamesFragments extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    //List of games in the screen and we want to pull nore games to the list
    private NestedScrollView mNestedScrollView;

    //Key for get from api
    private final String KEY_MANAGER_CALL_API = "MANAGER_API";

    //Call to games from the manager
    private static ManagerCallsFromAPI sManagerCallsFromAPI;

    //Fragment view
    private View mRoot;
    private ViewGroup mContainer;

    //Count page of games that we get from api
    private int mPageNext = 1;
    private final boolean fFeatchDataToRecycleView = true;

    public AllGamesFragments() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllGamesFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static AllGamesFragments newInstance(ManagerCallsFromAPI i_ManagerFromAPI) {

        AllGamesFragments fragment = new AllGamesFragments();

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
        mRoot = inflater.inflate(R.layout.fragment_all_games_fragments, container, false);
        mContainer = container;

        //Take the id of the list
        mNestedScrollView = mRoot.findViewById(R.id.nested_scrool_view);

        //Get details from user and get the key
        savedInstanceState = this.getArguments();

        if (savedInstanceState != null)
        {

         sManagerCallsFromAPI = (ManagerCallsFromAPI) savedInstanceState.getSerializable(KEY_MANAGER_CALL_API);

        }

        //clear the list
        sManagerCallsFromAPI.getM_ListGameModel().clear();
        mPageNext = 1;

        //First call
        sManagerCallsFromAPI.CallToAllGame(mPageNext, mRoot, mContainer, fFeatchDataToRecycleView);

        //Scroll down in recycle view
        mNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            try
            {

                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                {

                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    mPageNext++;
                    sManagerCallsFromAPI.CallToAllGame(mPageNext, mRoot, mContainer, fFeatchDataToRecycleView);

                }

            }
            catch (Exception e)
            {

                Toast.makeText(mRoot.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }

        });

        return mRoot;

    }

}