package com.example.gl.apiClient;

import androidx.annotation.Nullable;

import com.example.gl.model.GameSingleModel;
import com.example.gl.response.ResponseDevelopersModel;
import com.example.gl.response.ResponseGameModel;
import com.example.gl.response.ResponseGenresModel;
import com.example.gl.response.ResponsePlatformModel;
import com.example.gl.response.ResponsePublisherModel;
import com.example.gl.response.ResponseTrailersModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiServerRequests
{

    @GET("games")
    Call<ResponseGameModel> getGames(
            @Query("key") String apiKey,
            @Query("page") @Nullable String page ,
            @Query("page_size") @Nullable String page_size,
            @Query("genres") @Nullable String genres,
            @Query("platforms") @Nullable String platforms,
            @Query("developers") @Nullable String developers,
            @Query("publishers") @Nullable String publishers,
            @Query("dates") @Nullable String dates,
            @Query("ordering") @Nullable String ordering,
            @Query("search") @Nullable String search
    );

    @GET("genres")
    Call<ResponseGenresModel> getGenres(
            @Query("key") String apiKey

    );

    @GET("games/{id}")
    Call<GameSingleModel> getGameById(
            @Path("id") String id,
            @Query("key") String apiKey

    );

    @GET("platforms")
    Call<ResponsePlatformModel> getListPlatforms(
            @Query("page") @Nullable Integer page,
            @Query("key")  String apiKey

    );

    @GET("developers")
    Call<ResponseDevelopersModel> getListDevelopers(
            @Query("key")  String apiKey,
            @Query("page") @Nullable Integer page

    );

    @GET("games/{id}/movies")
    Call<ResponseTrailersModel> getListTrialers(
            @Path("id") String id,
            @Query("key") String apiKey

    );

    @GET("publishers")
    Call<ResponsePublisherModel> getListPublishers(
            @Query("page") @Nullable Integer page,
            @Query("key")  String apiKey

    );

}
