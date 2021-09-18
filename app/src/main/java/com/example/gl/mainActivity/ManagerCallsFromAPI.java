package com.example.gl.mainActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gl.R;
import com.example.gl.adapter.AdapterGameModel;
import com.example.gl.adapter.AdapterMainMenu;
import com.example.gl.model.GameModel;
import com.example.gl.model.GameSingleModel;
import com.example.gl.response.IMyCallback;
import com.example.gl.response.ResponseFromAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ManagerCallsFromAPI implements Serializable
{

    private List<GameModel> m_ListGameModel = new ArrayList<>();

    public List<GameModel> getM_ListGameModel() {
        return m_ListGameModel;
    }

    //Call from Respond api to all game
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CallToAllGame(int i_PageNumber, View i_Root, ViewGroup i_ViewGroup, boolean i_FlagMainMenuOrFragment){

        ResponseFromAPI.getInstance().GetAllGames(new IMyCallback()
        {

            @Override
            public <T> void onSuccess(@NonNull List<T> games)
            {

                if (i_FlagMainMenuOrFragment)
                {

                    featchDataToRecycleViewOfGames(R.id.recycle_view_all_games, (List<GameModel>)games, i_Root, i_ViewGroup);

                }
                else
                {

                    featchDataToRecycleViewOfGamesMainMenu(R.id.recycle_view_all_games, (List<GameModel>)games, i_Root, i_ViewGroup);

                }

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle) {

            }

            @Override
            public void onError(@NonNull Throwable throwable)
            {

                Toast.makeText(i_Root.getContext(), throwable.toString(), Toast.LENGTH_SHORT).show();

            }

        }, i_PageNumber);

    }

    //Call from Respond api to top game
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CallToTopGame(int i_PageNumber, View i_Root, ViewGroup i_ViewGroup, boolean i_FlagMainMenuOrFragment){

        ResponseFromAPI.getInstance().GetTopGames(new IMyCallback()
        {

            @Override
            public <T> void onSuccess(@NonNull List<T> games)
            {

                if (i_FlagMainMenuOrFragment)
                {

                    featchDataToRecycleViewOfGames(R.id.recycle_view_top_game, (List<GameModel>)games, i_Root, i_ViewGroup);

                }
                else
                 {

                    featchDataToRecycleViewOfGamesMainMenu(R.id.recycle_view_top_game, (List<GameModel>)games, i_Root, i_ViewGroup);

                }

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle)
            {

            }

            @Override
            public void onError(@NonNull Throwable throwable)
            {

                Toast.makeText(i_Root.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }, i_PageNumber);

    }

    //Call from Respond api to cooming soon game
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CallToCoomingSoonGame(int i_PageNumber, View i_Root, ViewGroup i_ViewGroup, boolean i_FlagMainMenuOrFragment)
    {

        ResponseFromAPI.getInstance().GetCoomingSoonGames(new IMyCallback()
        {

            @Override
            public <T> void onSuccess(@NonNull List<T> games)
            {

                if (i_FlagMainMenuOrFragment)
                {

                    featchDataToRecycleViewOfGames(R.id.recycler_view_cooming_soon_game, (List<GameModel>)games, i_Root, i_ViewGroup);

                }
                else
                {

                    featchDataToRecycleViewOfGamesMainMenu(R.id.recycler_view_cooming_soon_game, (List<GameModel>)games, i_Root, i_ViewGroup);

                }

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle)
            {

            }

            @Override
            public void onError(@NonNull Throwable throwable)
            {

                Toast.makeText(i_Root.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }, i_PageNumber);

    }

    //Call from Respond api to result search game
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CallToFilterSearchGame(int i_PageNumber, View i_Root, ViewGroup i_ViewGroup, HashMap<eExtantionType, String> i_HashMap, String i_Search)
    {

        ResponseFromAPI.getInstance().GetFilteredGames(new IMyCallback()
        {

            @SuppressLint("ResourceType")
            @Override
            public <T> void onSuccess(@NonNull List<T> games)
            {

                if (games.size() != 0)
                {

                    featchDataToRecycleViewOfGames(R.id.recycle_view_filtered, (List<GameModel>)games, i_Root, i_ViewGroup);

                 }
                else
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(i_Root.getContext());
                    builder.setMessage("Not Found");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK", (dialog, which) -> {

                                // If user click ok
                                // then dialog box is canceled.
                                dialog.cancel();

                            });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();

                }

            }

            @Override
            public void onSuccess(@NonNull GameSingleModel gamesSingle)
            {

            }

            @Override
            public void onError(@NonNull Throwable throwable)
            {

                Toast.makeText(i_Root.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }

        },i_HashMap, i_Search);

    }

    //print to recycleviews of main menu and get list of game model
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void featchDataToRecycleViewOfGamesMainMenu(int i_TheRecycleView, List<GameModel> i_ListGame, View i_Root, ViewGroup i_ViewGroup)
    {

        RecyclerView m_RecycleViewGames;
        m_RecycleViewGames = i_Root.findViewById(i_TheRecycleView);
        m_RecycleViewGames.setLayoutManager(new LinearLayoutManager(i_Root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        m_RecycleViewGames.setItemAnimator(new DefaultItemAnimator());
        AdapterMainMenu adapterGameModel = new AdapterMainMenu(i_ListGame, i_ViewGroup.getContext());
        m_RecycleViewGames.setAdapter(adapterGameModel);

    }

    ///print to recycleviews of fragments and get list of game model
    private void featchDataToRecycleViewOfGames(int i_TheRecycleView, List<GameModel> i_ListGame, View i_Root, ViewGroup i_ViewGroup)
    {

            RecyclerView m_RecycleViewGames;
            m_ListGameModel.addAll(i_ListGame);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(i_Root.getContext(), LinearLayoutManager.VERTICAL, false);
            m_RecycleViewGames = i_Root.findViewById(i_TheRecycleView);
            AdapterGameModel adapterGameModel = new AdapterGameModel(m_ListGameModel, i_ViewGroup.getContext());
            m_RecycleViewGames.setLayoutManager(linearLayoutManager);
            m_RecycleViewGames.setAdapter(adapterGameModel);

    }

}