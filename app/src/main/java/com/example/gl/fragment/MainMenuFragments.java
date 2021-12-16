package com.example.gl.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.gl.R;
import com.example.gl.mainActivity.ManagerApp;
import com.example.gl.mainActivity.ManagerCallsFromAPI;

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

    //Manager for calls to api
    private ManagerCallsFromAPI mManagerCallsFromAPI;

    //Manager of application
    private ManagerApp mManagerApp;
    private View mRoot;
    private ViewGroup mViewGroup;
    private Button mSearch;
    private final boolean mIsFragmentMainMenu = false;
    private EditText mEditTextOfSearch;

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
        mRoot = inflater.inflate(R.layout.fragment_main_menu_fragments, container, false);
        mViewGroup = container;

        Button buttonSeeAllGame = (Button) mRoot.findViewById(R.id.click_see_all_game);
        Button buttonSeeAllTopGame = (Button) mRoot.findViewById(R.id.click_see_top_game);
        Button buttonSeeAllCoomingSoonGame = (Button) mRoot.findViewById(R.id.click_see_cooming_soon_game);

        mSearch = (Button) mRoot.findViewById(R.id.click_search_main_menu);
        mEditTextOfSearch = mRoot.findViewById(R.id.search_input_text);

        savedInstanceState = this.getArguments();

        if (savedInstanceState != null)
        {

            mManagerCallsFromAPI = (ManagerCallsFromAPI)savedInstanceState.getSerializable(KEY_MANAGER_CALL_API);
            mManagerApp = (ManagerApp)savedInstanceState.getSerializable(KEY_MANAGER_APP);

        }

        //Call to data from api to recycleview on main menu
        mManagerCallsFromAPI.CallToTopGame(1, mRoot, mViewGroup, mIsFragmentMainMenu);
        mManagerCallsFromAPI.CallToAllGame(1, mRoot, mViewGroup, mIsFragmentMainMenu);
        mManagerCallsFromAPI.CallToCoomingSoonGame(1, mRoot, mViewGroup, mIsFragmentMainMenu);

        //click listener of buttons see all and search
        buttonSeeAllGame.setOnClickListener(this);
        buttonSeeAllTopGame.setOnClickListener(this);
        buttonSeeAllCoomingSoonGame.setOnClickListener(this);
        mSearch.setOnClickListener(this);//Click on the button search

        return mRoot;

    }

    //set listener to buttons see all and click on search button
    @Override
    public void onClick(View v)
    {

        //Click on the side menu and select one of the options
        switch (v.getId())
        {

            case R.id.click_see_all_game:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mManagerApp.GetGames()).addToBackStack(null).commit();
                break;

            case R.id.click_see_top_game:
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  mManagerApp.GetTopGames()).addToBackStack(null).commit();
                 break;

            case R.id.click_see_cooming_soon_game:
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  mManagerApp.GetCoomingSoon()).addToBackStack(null).commit();
                 break;

            case R.id.click_search_main_menu:

                Bundle bundle = new Bundle();
                //validation of EditText search
                if(!mEditTextOfSearch.getText().toString().equals("What is on your mind?") && !mEditTextOfSearch.getText().toString().equals(""))
                {

                    String input = mEditTextOfSearch.getText().toString();
                    mEditTextOfSearch.setText("What is on your mind?");

                    //The names of games sending with bottomline e.g: a_b_c
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