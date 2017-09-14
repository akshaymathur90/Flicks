package com.codepath.com.flicks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.OkHttpClient;

/**
 * Created by akshaymathur on 9/13/17.
 */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.BasicMovieViewHolder> {

    private Context mContext;
    private List<Movie> mMovieList;

    public MoviesRecyclerViewAdapter(Context context,List<Movie> movies){
        mContext = context;
        mMovieList = movies;
    }

    public void setDataSet(List<Movie> movies){
        mMovieList.addAll(movies);
        notifyDataSetChanged();
    }
    @Override
    public BasicMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.basic_movie_item,parent,false);
        BasicMovieViewHolder viewHolder = new BasicMovieViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BasicMovieViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        holder.mTextViewMovieName.setText(movie.getTitle());
        holder.mTextViewMovieDesc.setText(movie.getOverview());
        Log.d("DEBUG",movie.getPosterPath());
        OkHttpClient client = OkHttpClientSingleton.getInstance();
        Picasso picasso = new Picasso.Builder(mContext).downloader(new OkHttp3Downloader(client)).build();
        picasso.load(movie.getPosterPath())
                .placeholder(R.drawable.default_placeholder_portrait).transform(new RoundedCornersTransformation(10, 10))
                .into(holder.mImageViewMoviePoster);

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }


    class BasicMovieViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_movie_poster) ImageView mImageViewMoviePoster;
        @BindView(R.id.tv_movie_name) TextView mTextViewMovieName;
        @BindView(R.id.tv_movie_desc) TextView mTextViewMovieDesc;

        public BasicMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
