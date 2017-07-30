package com.example.tekwan.popularmovies.Persistent;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.Utilities.NetworkUtility;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tekwan on 7/26/2017.
 */

public class MovieAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

    private final AsyncTaskListenerInterface listener;
    private String searchquery;
    List<Movie> movies;
    public MovieAsyncTaskLoader(Context context,AsyncTaskListenerInterface listener,String query) {
        super(context);
        this.listener = listener;
        searchquery = query;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        listener.preExecute();
    }


    @Override
    public List<Movie> loadInBackground() {
        if (searchquery == null || TextUtils.isEmpty(searchquery)) {
            return null;
        }
        URL url = NetworkUtility.buildUrl(searchquery);

        try {
            String jsonMovieResponse = NetworkUtility
                    .makeHttpRequest(url);
            return NetworkUtility.extractFeatureFromJson(jsonMovieResponse);


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
