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

    private final String API_KEY = "9dff30a883ca4236af73ad04b0ca088f";

    private List<GameModel> mListGameModel;
    private List<GameModel> mListTopGameModel;
    private List<GameModel> mListCoomingSoonGameModel;

    private List<PlatformModel> mListPlatformModel;
    private List<PublisherModel> mListPublishersModel;
    private List<DevelopersModel> mListDevelopersModel;
    private List<GameModel> mGameList;
    private List<GenresModel> mGenresList;
    private List<TrailersModel> mListTrailersModel;

    private String mToday;
    private String mNextMonth;
    private String mPreviousMonth;
    private String mTime;

    private volatile static ResponseFromAPI vsInstanceResponseAPI = null;

    private String mGenres;
    private int mPage;
    private int mPageSize;
    private String mPlatforms;
    private String mDevelopers;
    private String mPublishers;
    private String mDates;
    private String mOrdering;
    private String mSearch;

    private static ReentrantLock sLock = new ReentrantLock();//Lock and unlock

    private ResponseFromAPI()
    {

        mListGameModel = new ArrayList<>();
        mListDevelopersModel = new ArrayList<>();
        mListTopGameModel = new ArrayList<>();
        mListCoomingSoonGameModel = new ArrayList<>();
        mListPlatformModel = new ArrayList<>();
        mListPublishersModel = new ArrayList<>();
        mGameList = new ArrayList<>();
        mGenresList = new ArrayList<>();
        mListTrailersModel = new ArrayList<>();

        getTime();
        clearMembers();

    }

    //Get instance
    public static ResponseFromAPI getInstance()
    {

        /**
         * Double check lock - We have 2 checkes if the instance is null and we put lock synchronized
         * and its one of the soluation for this but is not the best practice because the Java memory
         * model allows the publication of partially initialized objects and this may lead in turn to
         * subtle bugs.
         * ReentrantLock - We use this when we want to perform lock with synchronized operation
         * s a mutual exclusion lock with the same basic behavior as the implicit
         * monitors accessed via the synchronized keyword but with extended capabilities.
         * As the name suggests this lock implements reentrant characteristics just as implicit monitors.
         * We useing in lock and unlock and its better for operation in thread-seaf because if another
         * thread has already acquired the lock subsequent calls to lock() pause the current thread until
         * the lock has been unlocked.
         * Only one thread can hold the lock at any given time.
         * Is our choose to use :)
         * Value volatile - keyword, changed value of diffrent threads, its mean when we hace a multiple threads
         * we can use a methods instance of the classes at  the same time without any problem.
         * */
        //Lock
        sLock.lock();

        //try and finally for to ensure unlocking in case of exceptions.
        try
        {

            if (vsInstanceResponseAPI == null)
            {

                vsInstanceResponseAPI = new ResponseFromAPI();

            }

            return vsInstanceResponseAPI;

        }
        finally
        {

            sLock.unlock();

        }

    }

    //Search the game by id or slug
    public void GetGameById(String i_GameIdOrSulg, @Nullable IMyCallback i_MyCallback)
    {

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

                if (response.isSuccessful()  && response.body() != null)
                {

                    try
                    {

                        mGenresList.clear();
                        mGenresList = response.body().getResults();

                        if (i_MyCallbackSpinner != null)
                        {

                            i_MyCallbackSpinner.onSuccess(mGenresList);

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
    public void GetListPlatformsName(@Nullable IMyCallback i_MyCallbackSpinner)
    {

        Call<ResponsePlatformModel> call = ApiClient.getInstance().getApi().getListPlatforms(mPage, API_KEY);

        call.enqueue(new Callback<ResponsePlatformModel>()
        {

            @Override
            public void onResponse(Call<ResponsePlatformModel> call, Response<ResponsePlatformModel> response)
            {

                if (response.body() != null && response.isSuccessful())
                {

                    mListPlatformModel.clear();
                    mListPlatformModel = response.body().getResults();

                    if (i_MyCallbackSpinner != null)
                    {

                        i_MyCallbackSpinner.onSuccess(mListPlatformModel);

                    }

                }

            }

            @Override
            public void onFailure(Call<ResponsePlatformModel> call, Throwable t)
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

    //Get list of name publishers
    public void GetListPublishersName(@NonNull IMyCallback i_MyCallbackSpinner)
    {

        Call<ResponsePublisherModel> call = ApiClient.getInstance().getApi().getListPublishers(mPage, API_KEY);

        call.enqueue(new Callback<ResponsePublisherModel>()
        {

            @Override
            public void onResponse(Call<ResponsePublisherModel> call, Response<ResponsePublisherModel> response)
            {

                try
                {

                    if (response.body() != null && response.isSuccessful())
                    {

                        mListPublishersModel.clear();
                        mListPublishersModel = response.body().getResults();

                        if (i_MyCallbackSpinner != null)
                        {

                            i_MyCallbackSpinner.onSuccess(mListPublishersModel);

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

        Call<ResponseDevelopersModel> call = ApiClient.getInstance().getApi().getListDevelopers( API_KEY, mPage);

        call.enqueue(new Callback<ResponseDevelopersModel>() {
            @Override
            public void onResponse(Call<ResponseDevelopersModel> call, Response<ResponseDevelopersModel> response) {

                if (response.isSuccessful() && response.body() != null){

                    try{

                        mListDevelopersModel.clear();
                        mListDevelopersModel = response.body().getResults();

                        if (i_MyCallbackSpinner != null){

                            i_MyCallbackSpinner.onSuccess(mListDevelopersModel);

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
                String.valueOf(mPageSize), mGenres, mPlatforms, mDevelopers, mPublishers, mDates, mOrdering, mSearch);


        call.enqueue(new Callback<ResponseGameModel>() {
            @Override
            public void onResponse(Call<ResponseGameModel> call, Response<ResponseGameModel> response) {


                    if (response.body() != null && response.isSuccessful()) {

                        mListGameModel.clear();
                        mListGameModel = response.body().getResults();

                        if (i_MyCallback != null) {//send the list top game for fragment

                            i_MyCallback.onSuccess(mListGameModel);
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

        mTime = String.format(mPreviousMonth + "," + mToday);

        // call from rawg api
        Call<ResponseGameModel> call = ApiClient.getInstance().getApi().getGames(API_KEY, String.valueOf(i_Page),
                String.valueOf(mPageSize), mGenres, mPlatforms, mDevelopers, mPublishers, mTime, "-added", mSearch);


        call.enqueue(new Callback<ResponseGameModel>()
        {

            @Override
            public void onResponse(Call<ResponseGameModel> call, Response<ResponseGameModel> response) {

                if (response.body() != null && response.isSuccessful()) {

                    mListTopGameModel.clear();
                    mListTopGameModel = response.body().getResults();

                    if (i_MyCallback != null)
                    {
                        //send the list top game for fragment
                        i_MyCallback.onSuccess(mListTopGameModel);

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
    public void GetComingSoonGames(@Nullable IMyCallback i_MyCallback, int i_Page)
    {

        mTime = String.format(mToday + "," + mNextMonth);

        // call from rawg api
        Call<ResponseGameModel> call = ApiClient.getInstance().getApi().getGames(API_KEY, String.valueOf(i_Page),
                String.valueOf(mPageSize), mGenres, mPlatforms, mDevelopers, mPublishers, mTime, "-added", mSearch);


        call.enqueue(new Callback<ResponseGameModel>() {

            @Override
            public void onResponse(@NotNull Call<ResponseGameModel> call, @NotNull Response<ResponseGameModel> response) {

                if (response.body() != null && response.isSuccessful())
                {

                         mListCoomingSoonGameModel.clear();
                         mListCoomingSoonGameModel = response.body().getResults();

                           if (i_MyCallback != null)
                           {

                               //send the list cooming soon game for fragment
                               i_MyCallback.onSuccess(mListCoomingSoonGameModel);

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
                        mGenres = type.getValue();

                        break;

                    case Platform:
                        mPlatforms = type.getValue();

                        break;

                    case Publisher:
                        mPublishers = type.getValue();

                        break;

                    case ReleaseDate:
                        mDates = type.getValue();

                        break;
                    case Developrs:
                        mDevelopers = type.getValue();

                        break;

                    default:
                        break;

                }

            }

        }
        else if (i_Search != null)
        {

             i_Search = i_Search.replace(' ', '-').toLowerCase();
             mSearch = i_Search;

        }

            Call<ResponseGameModel> call = ApiClient.getInstance().getApi().getGames(API_KEY, String.valueOf(mPage),
                    String.valueOf(mPageSize), mGenres, mPlatforms, mDevelopers, mPublishers, mDates, mOrdering, mSearch);

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

                            mGameList.clear();// clear the list game
                            mGameList = response.body().getResults();//result of games

                            if (i_MyCallback != null) {

                                i_MyCallback.onSuccess(mGameList);

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

                        mListTrailersModel =  response.body().getResults();

                        if (i_MyCallback != null)
                        {

                            try
                             {

                                 i_MyCallback.onSuccessTrailers(mListTrailersModel);

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

            mPlatforms = null;
            mDevelopers = null;
            mPublishers = null;
            mDates = null;
            mOrdering = null;
            mSearch = null;
            mSearch = null;
            mGenres = null;
            mPage = 1;
            mPageSize = 30;

        }

    //Get times for call in api
    private void getTime()
    {

            // Current time
             Date date = Calendar.getInstance().getTime();
              Calendar calendar = Calendar.getInstance();

              // Set your date format
             SimpleDateFormat foramtDate = new SimpleDateFormat("yyyy-MM-dd");
             mToday = foramtDate.format(date);

            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 30);
            mNextMonth = foramtDate.format(calendar.getTime());

            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            mPreviousMonth = foramtDate.format(calendar.getTime());

        }

}
