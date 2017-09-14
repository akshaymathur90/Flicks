package com.codepath.com.flicks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.OkHttpClient;

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

        Movie movie = getIntent().getParcelableExtra(getString(R.string.key_movie));

        mTextViewReleaseDate.setText(movie.getReleaseDate());
        mTextViewSynopsis.setText(movie.getOverview());
        mRatingBarMovieRating.setRating(movie.getVoteAverage().floatValue());

        OkHttpClient client = OkHttpClientSingleton.getInstance();
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client)).build();

        picasso.load(movie.getBackdropPath())
                .placeholder(R.drawable.landscape)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(mImageViewBackDrop);
    }
}
