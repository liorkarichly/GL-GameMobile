package com.example.gl.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gl.mainActivity.ManagerCallsFromAPI;
import com.example.gl.R;

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

    private NestedScrollView m_NestedScrollView;
    private final String KEY_MANAGER_CALL_API = "MANAGER_API";
    private static ManagerCallsFromAPI m_ManagerCallsFromAPI;
    private View m_Root;

    private ViewGroup m_Container;
    private int m_PageNext = 1;
    private final boolean m_FeatchDataToRecycleView = true;

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
        m_Root = inflater.inflate(R.layout.fragment_all_games_fragments, container, false);
        m_Container = container;
        m_NestedScrollView = m_Root.findViewById(R.id.nested_scrool_view);

        savedInstanceState = this.getArguments();

        if (savedInstanceState != null)
        {

         m_ManagerCallsFromAPI = (ManagerCallsFromAPI) savedInstanceState.getSerializable(KEY_MANAGER_CALL_API);

        }

        m_ManagerCallsFromAPI.getM_ListGameModel().clear();
        m_PageNext = 1;

        //First call
        m_ManagerCallsFromAPI.CallToAllGame(m_PageNext, m_Root, m_Container, m_FeatchDataToRecycleView);

        //Scroll down in recycle view
        m_NestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            try
            {

                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                {

                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    m_PageNext++;
                    m_ManagerCallsFromAPI.CallToAllGame(m_PageNext, m_Root, m_Container, m_FeatchDataToRecycleView);

                }

            }
            catch (Exception e)
            {

                Toast.makeText(m_Root.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }

        });

        return m_Root;

    }

}