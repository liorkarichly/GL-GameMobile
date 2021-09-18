package com.example.gl.response;

import androidx.annotation.NonNull;

import com.example.gl.model.TrailersModel;

import org.json.JSONException;

import java.util.List;

public interface IMyCallbackTrailers
{

    void onSuccessTrailers(@NonNull List<TrailersModel> trailer);

    void onError(@NonNull Throwable throwable);

}
