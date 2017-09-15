package com.codepath.com.flicks.activities;

import android.os.Bundle;
import android.util.Log;

import com.codepath.com.flicks.R;
import com.codepath.com.flicks.helper.OkHttpClientSingleton;
import com.codepath.com.flicks.models.Trailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoviePlayerActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);
        youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        int movieId = getIntent().getIntExtra(getString(R.string.key_movie_id),0);
        OkHttpClient client = OkHttpClientSingleton.getInstance();

        Request request = new Request.Builder()
                .url(getTrailersAPIURL(String.valueOf(movieId)))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }else{
                    String stringResponse = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        Gson gson = new Gson();
                        Trailer trailer = gson.fromJson(jsonArray.get(0).toString(),Trailer.class);
                        final String trailerKey = trailer.getKey();
                        Log.d("DEBUG","The trailer key is--> "+trailerKey);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                youTubePlayerView.initialize("AIzaSyC_POChw-2PQb2G0BT7x-jNZt24gVef_rg",
                                        new YouTubePlayer.OnInitializedListener() {
                                            @Override
                                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                                YouTubePlayer youTubePlayer, boolean b) {

                                                // do any work here to cue video, play video, etc.
                                                youTubePlayer.loadVideo(trailerKey);
                                            }
                                            @Override
                                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                                YouTubeInitializationResult youTubeInitializationResult) {

                                            }
                                        });
                            }
                        });



                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });



    }

    public String getTrailersAPIURL(String movieId){

        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(String.format(getString(R.string.trailer_data_url),movieId)).newBuilder();
        urlBuilder.addQueryParameter("api_key", getString(R.string.api_key));
        return urlBuilder.build().toString();
    }
}
