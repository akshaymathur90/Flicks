package com.codepath.com.flicks.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.com.flicks.helper.OkHttpClientSingleton;
import com.codepath.com.flicks.R;
import com.codepath.com.flicks.models.Movie;
import com.codepath.com.flicks.models.Trailer;
import com.codepath.com.flicks.activities.MovieDetailActivity;
import com.codepath.com.flicks.activities.MoviePlayerActivity;
import com.google.gson.Gson;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.OkHttpClient;

/**
 * Created by akshaymathur on 9/13/17.
 */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Movie> mMovieList;
    private final int LOWRATEDMOVIE = 0;
    private final int HIGHRATEDMOVIE = 1;
    private Picasso picasso;
    private OkHttpClient client;

    public MoviesRecyclerViewAdapter(Context context,List<Movie> movies){
        mContext = context;
        mMovieList = movies;
        client = OkHttpClientSingleton.getInstance();
        picasso = new Picasso.Builder(mContext).downloader(new OkHttp3Downloader(client)).build();
    }

    public void setDataSet(List<Movie> movies){
        mMovieList.addAll(movies);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType){
            case LOWRATEDMOVIE:
                View lowItemView = inflater.inflate(R.layout.basic_movie_item,parent,false);
                return new BasicMovieViewHolder(lowItemView);
            case HIGHRATEDMOVIE:
                View highItemView = inflater.inflate(R.layout.high_rated_movie_item,parent,false);
                return new HighRatedMovieViewHolder(highItemView);
            default:
                View defItemView = inflater.inflate(R.layout.basic_movie_item,parent,false);
                return new BasicMovieViewHolder(defItemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        switch (holder.getItemViewType()){
            case LOWRATEDMOVIE:
                BasicMovieViewHolder basicMovieViewHolder = (BasicMovieViewHolder) holder;
                bindLowRatedViewHolder(basicMovieViewHolder,movie);
                break;
            case HIGHRATEDMOVIE:
                HighRatedMovieViewHolder highRatedMovieViewHolder = (HighRatedMovieViewHolder) holder;
                bindHighRatedMovieHolder(highRatedMovieViewHolder,movie);
        }



    }

    private void bindHighRatedMovieHolder(HighRatedMovieViewHolder highRatedMovieViewHolder, final Movie movie) {

        picasso.load(movie.getBackdropPath())
                .placeholder(R.drawable.landscape)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(highRatedMovieViewHolder.mImageViewMovieBackDrop);




        highRatedMovieViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,MoviePlayerActivity.class);
                intent.putExtra(mContext.getString(R.string.key_movie_id),movie.getId());
                mContext.startActivity(intent);

            }
        });
    }

    public void bindLowRatedViewHolder(BasicMovieViewHolder basicMovieViewHolder, final Movie movie){

        basicMovieViewHolder.mTextViewMovieName.setText(movie.getTitle());
        basicMovieViewHolder.mTextViewMovieDesc.setText(movie.getOverview());
        Log.d("DEBUG",movie.getPosterPath());

        String imageURL;
        int placeholder;
        int orientation = mContext.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            imageURL = movie.getPosterPath();
            placeholder = R.drawable.portrait;
        }else{
            imageURL = movie.getBackdropPath();
            placeholder = R.drawable.landscape;
            //holder.mImageViewMoviePoster
        }
        picasso.load(imageURL)
                .placeholder(placeholder)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(basicMovieViewHolder.mImageViewMoviePoster);

        basicMovieViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,MovieDetailActivity.class);
                intent.putExtra(mContext.getString(R.string.key_movie),movie);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = mMovieList.get(position);
        if(movie.getVoteAverage()>5){
            return HIGHRATEDMOVIE;
        }else if(movie.getVoteAverage()<=5){
            return LOWRATEDMOVIE;
        }
        return -1;

    }

    class BasicMovieViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_movie_poster) ImageView mImageViewMoviePoster;
        @BindView(R.id.tv_movie_name) TextView mTextViewMovieName;
        @BindView(R.id.tv_movie_desc) TextView mTextViewMovieDesc;
        View mView;
        public BasicMovieViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this,itemView);
        }

    }

    public class HighRatedMovieViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_back_drop) ImageView mImageViewMovieBackDrop;
        View mView;

        public HighRatedMovieViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this,itemView);
        }
    }
}
