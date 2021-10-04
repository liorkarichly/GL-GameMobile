package com.example.gl.response;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.gl.apiClient.ApiClient;
import com.example.gl.mainActivity.eExtantionType;
import com.example.gl.model.DevelopersModel;
import com.example.gl.model.GameModel;
import com.example.gl.model.GameSingleModel;
import com.example.gl.model.GenresModel;
import com.example.gl.model.PlatformModel;
import com.example.gl.model.PublisherModel;
import com.example.gl.model.TrailersModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@RequiresApi(api = Build.VERSION_CODES.O)
public class ResponseFromAPI
{

    private final String API_KEY = "";

    private List<GameModel> m_ListGameModel;
    private List<GameModel> m_ListTopGameModel;
    private List<GameModel> m_ListCoomingSoonGameModel;
    private List<PlatformModel> m_ListPlatformModel;
    private List<PublisherModel> m_ListPublishersModel;
    private List<DevelopersModel> m_ListDevelopersModel;
    private List<GameModel> m_GameList;
    private List<GenresModel> m_GenresList;
    private List<TrailersModel> m_ListTrailersModel;


    private String m_Today;
    private String m_NextMonth;
    private String m_PrevMonth;

    private static ResponseFromAPI s_instance = null;

    private String m_Genres;
    private Integer m_Page;
    private Integer m_PageSize;
    private String m_Platforms;
    private String m_Developers;
    private String m_Publishers;
    private String m_Dates;
    private String m_Ordering;
    private String m_Search;

    private static ReentrantLock s_Lock = new ReentrantLock();//lock and unlock

    private ResponseFromAPI()
    {

        m_ListGameModel = new ArrayList<>();
        m_ListDevelopersModel = new ArrayList<>();
        m_ListTopGameModel = new ArrayList<>();
        m_ListCoomingSoonGameModel = new ArrayList<>();
        m_ListPlatformModel = new ArrayList<>();
        m_ListPublishersModel = new ArrayList<>();
        m_GameList = new ArrayList<>();
        m_GenresList = new ArrayList<>();
        m_ListTrailersModel = new ArrayList<>();

        getTime();
        clearMembers();

    }

    //Get instance
    public static ResponseFromAPI getInstance(){

        s_Lock.lock();

        try
        {

            if (s_instance == null)
            {

                s_instance = new ResponseFromAPI();

            }

            return s_instance;

        }
        finally
        {

            s_Lock.unlock();

        }

    }

    //Search the game by id or slug
    public void GetGameById(String i_GameIdOrSulg, @Nullable IMyCallback i_MyCallback){

        Call<GameSingleModel> call = ApiClient.getInstance().getApi().getGameById(i_GameIdOrSulg, API_KEY);

        call.enqueue(new Callback<GameSingleModel>()
        {

            @Override
            public void onResponse(Call<GameSingleModel> call, Response<GameSingleModel> response)
            {

                if (response.body() != null && response.isSuccessful())
                {

                    GameSingleModel gameSingleModel = response.body();

                    if (i_MyCallback != null)
                    {

                        i_MyCallback.onSuccess(gameSingleModel);

                    }

                }

            }

            @Override
            public void onFailure(Call<GameSingleModel> call, Throwable t)
            {

                if (t instanceof IOException)
                {

                    i_MyCallback.onError(t);

                }
                else
                 {

                    i_MyCallback.onError(t);

                }

            }

        });

    }

    //Get list of name genres
    public void GetGenres(@Nullable IMyCallback i_MyCallbackSpinner)
    {

        Call<ResponseGenresModel> call = ApiClient.getInstance().getApi().getGenres(API_KEY);

        call.enqueue(new Callback<ResponseGenresModel>()
        {

            @Override
            public void onResponse(Call<ResponseGenresModel> call, Response<ResponseGenresModel> response)
            {

                if (response.isSuccessful()  && response.body() != null) {

                    try
                    {

                        m_GenresList.clear();
                        m_GenresList = response.body().getResults();

                        if (i_MyCallbackSpinner != null)
                        {

                            i_MyCallbackSpinner.onSuccess(m_GenresList);

                        }

                    }
                    catch (Exception e)
                    {

                        i_MyCallbackSpinner.onError(e);

                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseGenresModel> call, Throwable t)
            {

                if (t instanceof IOException)
                {


                    i_MyCallbackSpinner.onError(t);

                }
                else
                {

                    i_MyCallbackSpinner.onError(t);

                }

            }

        });

    }

    //Get list of name platforms
    public void GetListPlatformsName(@Nullable IMyCallback i_MyCallbackSpinner) {

        Call<ResponsePlatformModel> call = ApiClient.getInstance().getApi().getListPlatforms(m_Page, API_KEY);

        call.enqueue(new Callback<ResponsePlatformModel>() {
            @Override
            public void onResponse(Call<ResponsePlatformModel> call, Response<ResponsePlatformModel> response) {

                if (response.body() != null && response.isSuccessful())
                {

                    m_ListPlatformModel.clear();
                    m_ListPlatformModel = response.body().getResults();

                    if (i_MyCallbackSpinner != null)
                    {

                        i_MyCallbackSpinner.onSuccess(m_ListPlatformModel);

                    }

                }

            }

            @Override
            public void onFailure(Call<ResponsePlatformModel> call, Throwable t) {

                if (t instanceof IOException)
                {


                    i_MyCallbackSpinner.onError(t);

                }
                else
                {

                    i_MyCallbackSpinner.onError(t);

                }


            }

        });

    }

    //Get list of name publishers
    public void GetListPublishersName(@NonNull IMyCallback i_MyCallbackSpinner){

        Call<ResponsePublisherModel> call = ApiClient.getInstance().getApi().getListPublishers(m_Page, API_KEY);

        call.enqueue(new Callback<ResponsePublisherModel>() {
            @Override
            public void onResponse(Call<ResponsePublisherModel> call, Response<ResponsePublisherModel> response) {


                try{

                    if (response.body() != null && response.isSuccessful())
                    {

                        m_ListPublishersModel.clear();
                        m_ListPublishersModel = response.body().getResults();

                        if (i_MyCallbackSpinner != null)
                        {

                            i_MyCallbackSpinner.onSuccess(m_ListPublishersModel);

                        }

                    }

                }catch (Exception e){

                    i_MyCallbackSpinner.onError(e);

                }

            }

            @Override
            public void onFailure(Call<ResponsePublisherModel> call, Throwable t) {

                if (t instanceof IOException)
                {


                    i_MyCallbackSpinner.onError(t);

                }
                else
                {

                    i_MyCallbackSpinner.onError(t);

                }


            }
        });

    }

    //Get list of name publishers
    public void GetListDeveloprsName(@NonNull IMyCallback i_MyCallbackSpinner){

        Call<ResponseDevelopersModel> call = ApiClient.getInstance().getApi().getListDevelopers( API_KEY, m_Page);

        call.enqueue(new Callback<ResponseDevelopersModel>() {
            @Override
            public void onResponse(Call<ResponseDevelopersModel> call, Response<ResponseDevelopersModel> response) {

                if (response.isSuccessful() && response.body() != null){

                    try{

                        m_ListDevelopersModel.clear();
                        m_ListDevelopersModel = response.body().getResults();

                        if (i_MyCallbackSpinner != null){

                            i_MyCallbackSpinner.onSuccess(m_ListDevelopersModel);

                        }

                    }catch (Exception ex){


                        i_MyCallbackSpinner.onError(ex);

                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseDevelopersModel> call, Throwable t) {

                if (t instanceof IOException)
                {


                    i_MyCallbackSpinner.onError(t);

                }
                else
                {

                    i_MyCallbackSpinner.onError(t);

                }

            }

        });

    }

    //Get all games
    public void GetAllGames(@Nullable IMyCallback i_MyCallback, int i_Page)
    {

        // call from rawg api
        Call<ResponseGameModel> call = ApiClient.getInstance().getApi().getGames(API_KEY, String.valueOf(i_Page),
                String.valueOf(m_PageSize), m_Genres, m_Platforms, m_Developers, m_Publishers, m_Dates, m_Ordering, m_Search);


        call.enqueue(new Callback<ResponseGameModel>() {
            @Override
            public void onResponse(Call<ResponseGameModel> call, Response<ResponseGameModel> response) {


                    if (response.body() != null && response.isSuccessful()) {

                        m_ListGameModel.clear();
                        m_ListGameModel = response.body().getResults();

                        if (i_MyCallback != null) {//send the list top game for fragment

                            i_MyCallback.onSuccess(m_ListGameModel);
                        }

                    }

            }

            @Override
            public void onFailure(Call<ResponseGameModel> call, Throwable t) {

                if (t instanceof IOException) {


                    i_MyCallback.onError(t);

                } else {

                    i_MyCallback.onError(t);

                }

            }

        });

    }

    //Get top games
    public void GetTopGames(@Nullable IMyCallback i_MyCallback, int i_Page)
    {

        // call from rawg api
        Call<ResponseGameModel> call = ApiClient.getInstance().getApi().getGames(API_KEY, String.valueOf(i_Page),
                String.valueOf(m_PageSize), m_Genres, m_Platforms, m_Developers, m_Publishers, String.format(m_PrevMonth + "," + m_Today), "-added", m_Search);


        call.enqueue(new Callback<ResponseGameModel>()
        {

            @Override
            public void onResponse(Call<ResponseGameModel> call, Response<ResponseGameModel> response) {

                if (response.body() != null && response.isSuccessful()) {

                    m_ListTopGameModel.clear();
                    m_ListTopGameModel = response.body().getResults();

                    if (i_MyCallback != null)
                    {
                        //send the list top game for fragment
                        i_MyCallback.onSuccess(m_ListTopGameModel);

                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseGameModel> call, Throwable t)
            {

                if (t instanceof IOException)
                {


                    i_MyCallback.onError(t);

                }
                else
                {

                    i_MyCallback.onError(t);

                }

            }

        });

    }

    //Get cooming soon games
    public void GetCoomingSoonGames(@Nullable IMyCallback i_MyCallback, int i_Page)
    {

        String time = String.format(m_Today + "," +m_NextMonth );

        // call from rawg api
        Call<ResponseGameModel> call = ApiClient.getInstance().getApi().getGames(API_KEY, String.valueOf(i_Page),
                String.valueOf(m_PageSize), m_Genres, m_Platforms, m_Developers, m_Publishers, time, "-added",  m_Search);


        call.enqueue(new Callback<ResponseGameModel>() {

            @Override
            public void onResponse(@NotNull Call<ResponseGameModel> call, @NotNull Response<ResponseGameModel> response) {

                if (response.body() != null && response.isSuccessful())
                {

                         m_ListCoomingSoonGameModel.clear();
                         m_ListCoomingSoonGameModel = response.body().getResults();

                           if (i_MyCallback != null)
                           {

                               //send the list cooming soon game for fragment
                               i_MyCallback.onSuccess(m_ListCoomingSoonGameModel);

                           }
                }

            }

            @Override
            public void onFailure(Call<ResponseGameModel> call, Throwable t)
            {

                if (t instanceof IOException)
                {


                    i_MyCallback.onError(t);

                }
                else
                {

                    i_MyCallback.onError(t);

                }

            }

        });

    }

    //Get game filter
    public void GetFilteredGames(@Nullable IMyCallback i_MyCallback, @Nullable HashMap<eExtantionType, String> i_HashMap, @Nullable String i_Search) {

        if (i_HashMap != null)
        {

            clearMembers();
            for (Map.Entry<eExtantionType, String> type : i_HashMap.entrySet())
            {

                if (type.getValue() != null)
                {

                    String value = type.getValue().replace(' ', '-').toLowerCase();
                    type.setValue(value);

                }

                switch (type.getKey())
                {

                    case Genre:
                        m_Genres = type.getValue();

                        break;

                    case Platform:
                        m_Platforms = type.getValue();

                        break;

                    case Publisher:
                        m_Publishers = type.getValue();

                        break;

                    case ReleaseDate:
                        m_Dates = type.getValue();

                        break;
                    case Developrs:
                        m_Developers = type.getValue();

                        break;

                    default:
                        break;

                }

            }

        }
        else if (i_Search != null)
        {

             i_Search = i_Search.replace(' ', '-').toLowerCase();
             m_Search = i_Search;

        }

            Call<ResponseGameModel> call = ApiClient.getInstance().getApi().getGames(API_KEY, String.valueOf(m_Page),
                    String.valueOf(m_PageSize), m_Genres, m_Platforms, m_Developers, m_Publishers,  m_Dates, m_Ordering, m_Search);

            i_HashMap = null;
            clearMembers();
            call.enqueue(new Callback<ResponseGameModel>()
            {

                @Override
                public void onResponse(Call<ResponseGameModel> call, Response<ResponseGameModel> response)
                {

                    try
                    {

                        if (response.body() != null && response.isSuccessful())
                        {

                            m_GameList.clear();// clear the list game
                            m_GameList = response.body().getResults();//result of games

                            if (i_MyCallback != null) {

                                i_MyCallback.onSuccess(m_GameList);

                            }

                        }

                    }
                    catch (Exception e)
                    {

                         i_MyCallback.onError(e);

                    }

                }

                @Override
                public void onFailure(Call<ResponseGameModel> call, Throwable t)
                {

                    if (t instanceof IOException)
                    {


                        i_MyCallback.onError(t);

                    }
                    else
                    {

                        i_MyCallback.onError(t);

                    }

                }

            });

        }

    //Get trialer by id of game
    public void GetTrailersOfGame(@Nullable IMyCallbackTrailers i_MyCallback, @Nullable String i_NameOrIdOfTheGame)
    {

        Call<ResponseTrailersModel> call = ApiClient.getInstance().getApi().getListTrialers(i_NameOrIdOfTheGame, API_KEY);

        call.enqueue(new Callback<ResponseTrailersModel>()
        {

            @Override
            public void onResponse(Call<ResponseTrailersModel> call, Response<ResponseTrailersModel> response)
            {

                if (response.body() != null && response.isSuccessful())
                {

                        m_ListTrailersModel =  response.body().getResults();

                        if (i_MyCallback != null)
                        {

                            try
                             {

                                 i_MyCallback.onSuccessTrailers(m_ListTrailersModel);

                            }
                             catch (Exception e)
                              {

                              i_MyCallback.onError(e);

                             }

                        }

                }

            }

            @Override
            public void onFailure(Call<ResponseTrailersModel> call, Throwable t)
            {

                i_MyCallback.onError(t);

            }

        });

    }

     //Clear members of serach and parametrs for send to api
    private void clearMembers()
    {

            m_Platforms = null;
            m_Developers = null;
            m_Publishers = null;
            m_Dates = null;
            m_Ordering = null;
            m_Search = null;
            m_Search = null;
            m_Genres = null;
            m_Page = 1;
            m_PageSize = 30;

        }

    //Get times for call in api
    private void getTime()
    {

            // Current time
             Date date = Calendar.getInstance().getTime();
              Calendar calendar = Calendar.getInstance();

              // Set your date format
             SimpleDateFormat foramtDate = new SimpleDateFormat("yyyy-MM-dd");
             m_Today = foramtDate.format(date);

            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 30);
            m_NextMonth = foramtDate.format(calendar.getTime());

            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            m_PrevMonth = foramtDate.format(calendar.getTime());

        }

}
