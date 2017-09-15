package com.codepath.com.flicks.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.com.flicks.helper.OkHttpClientSingleton;
import com.codepath.com.flicks.R;
import com.codepath.com.flicks.models.Movie;
import com.codepath.com.flicks.models.Trailer;
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
    @BindView(R.id.tv_movie_title)
    TextView mTextViewMovieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        final Movie movie = getIntent().getParcelableExtra(getString(R.string.key_movie));

        mTextViewMovieTitle.setText(movie.getTitle());
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
                Intent intent = new Intent(MovieDetailActivity.this,MoviePlayerActivity.class);
                intent.putExtra(getString(R.string.key_movie_id),movie.getId());
                startActivity(intent);
            }
        });
    }

}
