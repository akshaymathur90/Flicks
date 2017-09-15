package com.codepath.com.flicks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_back_drop_image)
    ImageView mImageViewBackDrop;
    @BindView(R.id.tv_release_date)
    TextView mTextViewReleaseDate;
    @BindView(R.id.tv_movie_synopsis)
    TextView mTextViewSynopsis;
    @BindView(R.id.rb_movie_rating)
    RatingBar mRatingBarMovieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        final Movie movie = getIntent().getParcelableExtra(getString(R.string.key_movie));

        mTextViewReleaseDate.setText(movie.getReleaseDate());
        mTextViewSynopsis.setText(movie.getOverview());
        mRatingBarMovieRating.setRating(movie.getVoteAverage().floatValue());

        final OkHttpClient client = OkHttpClientSingleton.getInstance();
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client)).build();

        picasso.load(movie.getBackdropPath())
                .placeholder(R.drawable.landscape)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(mImageViewBackDrop);

        mImageViewBackDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request request = new Request.Builder()
                        .url(getTrailersAPIURL(String.valueOf(movie.getId())))
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
                                Log.d("DEBUG",jsonArray.toString());
                                Gson gson = new Gson();
                                Trailer trailer = gson.fromJson(jsonArray.get(0).toString(),Trailer.class);
                                String trailerKey = trailer.getKey();
                                Log.d("DEBUG",trailerKey);
                                Intent intent = new Intent(MovieDetailActivity.this,MoviePlayerActivity.class);
                                intent.putExtra(getString(R.string.key_trailer),trailerKey);
                                startActivity(intent);


                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });






            }
        });
    }

    public String getTrailersAPIURL(String movieId){

        Log.d("DEBUG","Movie id is-->"+movieId);
        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(String.format(getString(R.string.trailer_data_url),movieId)).newBuilder();
        urlBuilder.addQueryParameter("api_key",getString(R.string.api_key));
        Log.d("DEBUG","Trailer API url is-->"+urlBuilder.build().toString());
        return urlBuilder.build().toString();
    }
}
