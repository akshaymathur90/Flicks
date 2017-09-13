package com.codepath.com.flicks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by akshaymathur on 9/13/17.
 */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.BasicMovieViewHolder> {

    @Override
    public BasicMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BasicMovieViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class BasicMovieViewHolder extends RecyclerView.ViewHolder{

        public BasicMovieViewHolder(View itemView) {
            super(itemView);
        }
    }
}
