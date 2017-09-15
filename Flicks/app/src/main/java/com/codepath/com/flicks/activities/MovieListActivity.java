package com.codepath.com.flicks.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.com.flicks.fragments.MovieListFragment;
import com.codepath.com.flicks.R;

public class MovieListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment movieListFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(movieListFragment == null){
            movieListFragment = MovieListFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.fragment_container,movieListFragment).commit();
        }else{
            fragmentManager.beginTransaction().replace(R.id.fragment_container,movieListFragment).commit();
        }
    }
}
