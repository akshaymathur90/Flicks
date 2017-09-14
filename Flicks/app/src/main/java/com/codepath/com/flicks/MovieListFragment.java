package com.codepath.com.flicks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieListFragment extends Fragment {

    @BindView(R.id.rv_movies_list) RecyclerView mRecyclerView;
    private Unbinder unbinder;
    private List<Movie> mMovieList;
    MoviesRecyclerViewAdapter mRecyclerViewAdapter;

    OkHttpClient client = OkHttpClientSingleton.getInstance();
    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);
        unbinder = ButterKnife.bind(this, v);

        mMovieList = new ArrayList<>();
        mRecyclerViewAdapter = new MoviesRecyclerViewAdapter(getActivity(), mMovieList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        fetchMoviesFromAPI();
        return v;
    }

    public void fetchMoviesFromAPI(){

        Request request = new Request.Builder()
                .url(getAPIURL())
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
                }
                else{
                    String responseString = response.body().string();
                    Log.d("DEBUG", responseString);
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        final JSONArray jsonArray = jsonObject.getJSONArray("results");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mRecyclerViewAdapter.setDataSet(Movie.fromArray(jsonArray));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public String getAPIURL(){

        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(getString(R.string.movies_data_url)).newBuilder();
        urlBuilder.addQueryParameter("api_key", getString(R.string.api_key));
        return urlBuilder.build().toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
