package com.example.gl.mainActivity;

import android.os.Bundle;

import com.example.gl.fragment.AboutUsFragments;
import com.example.gl.fragment.AllGamesFragments;
import com.example.gl.fragment.CoomingSoonFragments;
import com.example.gl.fragment.MainMenuFragments;
import com.example.gl.fragment.SearchFragments;
import com.example.gl.fragment.TopGamesFragments;

import java.io.Serializable;

public class ManagerApp implements Serializable
{

    private final ManagerCallsFromAPI f_ManagerCallFromAPI = new ManagerCallsFromAPI();
    private final String KEY_MANAGER_CALL_API = "MANAGER_API";
    private final String KEY_MANAGER_APP = "MANAGER_APP";

    public ManagerApp()
    {

    }

    //Get fragment main menu
    public MainMenuFragments GetMainMenu()
    {

        Bundle bundel = new Bundle();
        MainMenuFragments mainMenuFragments = new MainMenuFragments();
        bundel.putSerializable(KEY_MANAGER_CALL_API, f_ManagerCallFromAPI);
        bundel.putSerializable(KEY_MANAGER_APP, this);
        mainMenuFragments.setArguments(bundel);

        return mainMenuFragments;

    }

    //Get fragment all game
    public AllGamesFragments GetGames()
    {

        Bundle bundel = new Bundle();
        AllGamesFragments allGamesFragments = new AllGamesFragments();
        bundel.putSerializable(KEY_MANAGER_CALL_API, f_ManagerCallFromAPI);
        allGamesFragments.setArguments(bundel);

        return allGamesFragments;

    }

    //Get fragment search
    public SearchFragments GetSearch()
    {

        Bundle bundel = new Bundle();
        SearchFragments searchFragments = new SearchFragments();
        bundel.putSerializable(KEY_MANAGER_CALL_API, f_ManagerCallFromAPI);
        searchFragments.setArguments(bundel);

        return searchFragments;

    }

    //Get fragment top game
    public TopGamesFragments GetTopGames()
    {

        Bundle bundel = new Bundle();
        TopGamesFragments topGamesFragments = new TopGamesFragments();
        bundel.putSerializable(KEY_MANAGER_CALL_API, f_ManagerCallFromAPI);
        topGamesFragments.setArguments(bundel);

        return topGamesFragments;

    }

    //Get fragment cooming soon
    public CoomingSoonFragments GetCoomingSoon()
    {

        Bundle bundel = new Bundle();
        CoomingSoonFragments coomingSoonFragments = new CoomingSoonFragments();
        bundel.putSerializable(KEY_MANAGER_CALL_API, f_ManagerCallFromAPI);
        coomingSoonFragments.setArguments(bundel);

        return coomingSoonFragments;

    }

    //Get fragment about us
    public AboutUsFragments GetAboutUs()
    {

        return  AboutUsFragments.newInstance();

    }

}
