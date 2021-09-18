package com.example.gl.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.gl.mainActivity.ManagerApp;
import com.example.gl.mainActivity.ManagerCallsFromAPI;
import com.example.gl.R;
import com.example.gl.adapter.AdapterGameModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragments extends Fragment implements View.OnClickListener
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String KEY_MANAGER_CALL_API = "MANAGER_API";
    private final String KEY_MANAGER_APP = "MANAGER_APP";
    private final String KEY_STRING = "SEARCH_QUERY";

    private ManagerCallsFromAPI m_ManagerCallsFromAPI;
    private ManagerApp m_ManagerApp;
    private View m_Root;
    private ViewGroup m_ViewGroup;
    private Button m_Search;
    private final boolean m_IsFragmentMainMenu = false;
    private EditText m_EditTextOfSearch;

    // TODO: Rename and change types of parameters

    public MainMenuFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainMenuFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragments newInstance() {

        MainMenuFragments fragment = new MainMenuFragments();

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        m_Root = inflater.inflate(R.layout.fragment_main_menu_fragments, container, false);
        m_ViewGroup = container;

        Button buttonSeeAllGame = (Button) m_Root.findViewById(R.id.click_see_all_game);
        Button buttonSeeAllTopGame = (Button)m_Root.findViewById(R.id.click_see_top_game);
        Button buttonSeeAllCoomingSoonGame = (Button)m_Root.findViewById(R.id.click_see_cooming_soon_game);

        m_Search = (Button)m_Root.findViewById(R.id.click_search_main_menu);
        m_EditTextOfSearch = m_Root.findViewById(R.id.search_input_text);

        savedInstanceState = this.getArguments();

        if (savedInstanceState != null)
        {

            m_ManagerCallsFromAPI = (ManagerCallsFromAPI)savedInstanceState.getSerializable(KEY_MANAGER_CALL_API);
            m_ManagerApp = (ManagerApp)savedInstanceState.getSerializable(KEY_MANAGER_APP);

        }

        //Call to data from api to recycleview on main menu
        m_ManagerCallsFromAPI.CallToTopGame(1, m_Root, m_ViewGroup, m_IsFragmentMainMenu);
        m_ManagerCallsFromAPI.CallToAllGame(1, m_Root, m_ViewGroup,m_IsFragmentMainMenu);
        m_ManagerCallsFromAPI.CallToCoomingSoonGame(1, m_Root, m_ViewGroup, m_IsFragmentMainMenu);

        //click listener of buttons see all and search
        buttonSeeAllGame.setOnClickListener(this);
        buttonSeeAllTopGame.setOnClickListener(this);
        buttonSeeAllCoomingSoonGame.setOnClickListener(this);
        m_Search.setOnClickListener(this);

        return m_Root;

    }

    //set listener to buttons see all and click on search button
    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {

            case R.id.click_see_all_game:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, m_ManagerApp.GetGames()).addToBackStack(null).commit();
                break;

            case R.id.click_see_top_game:
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  m_ManagerApp.GetTopGames()).addToBackStack(null).commit();
                 break;

            case R.id.click_see_cooming_soon_game:
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  m_ManagerApp.GetCoomingSoon()).addToBackStack(null).commit();
                 break;



            case R.id.click_search_main_menu:

                Bundle bundle = new Bundle();

                if(!m_EditTextOfSearch.getText().toString().equals("What is on your mind?") && !m_EditTextOfSearch.getText().toString().equals(""))
                {

                    String input = m_EditTextOfSearch.getText().toString();
                    m_EditTextOfSearch.setText("What is on your mind?");

                    input = input.replace(' ', '-').toLowerCase();
                    bundle.putString(KEY_STRING, input);

                }

                 ResultBySearch filteredGamesFragment  = new ResultBySearch();
                 filteredGamesFragment.setArguments(bundle);
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filteredGamesFragment).addToBackStack(null).commit();
                 break;

            default:
                 break;

        }

    }

}