package com.example.gl.response;

import com.example.gl.model.PlatformModel;

import java.io.Serializable;

public class ResponsePlatformsForGameSingleModel implements Serializable
{

    private PlatformModel platform;

    public PlatformModel getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformModel platform) {
        this.platform = platform;
    }

}
