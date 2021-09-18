package com.example.gl.response;

import androidx.annotation.NonNull;

import com.example.gl.model.GameModel;
import com.example.gl.model.GameSingleModel;

import java.util.List;

public interface IMyCallback
{

   <T> void onSuccess(@NonNull List<T> games);

    void onSuccess(@NonNull GameSingleModel gamesSingle);

    void onError(@NonNull Throwable throwable);

}
