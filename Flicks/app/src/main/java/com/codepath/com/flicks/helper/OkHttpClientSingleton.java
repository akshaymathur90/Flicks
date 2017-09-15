package com.codepath.com.flicks.helper;

import okhttp3.OkHttpClient;

/**
 * Created by akshaymathur on 9/13/17.
 */

public class OkHttpClientSingleton {

    private static OkHttpClient mSingleton;
    private OkHttpClientSingleton(){

    }

    public static OkHttpClient getInstance(){
        if(mSingleton==null){
            return new OkHttpClient();
        }
        return mSingleton;
    }
}
