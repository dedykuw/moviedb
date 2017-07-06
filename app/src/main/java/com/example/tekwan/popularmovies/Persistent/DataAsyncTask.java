package com.example.tekwan.popularmovies.Persistent;

import android.graphics.Movie;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.List;

import com.example.tekwan.popularmovies.Layout.MovieAdapter;
import com.example.tekwan.popularmovies.Utilities.NetworkUtility;

/**
 * Created by tekwan on 7/4/2017.
 */

public class DataAsyncTask extends AsyncTask<String, Void, List<Movie>>{
    private static final String TAG = DataAsyncTask.class.getSimpleName();
    private ProgressBar mLoadingIndicator;
    private MovieAdapter adapter;
    public DataAsyncTask(MovieAdapter adapter) {
        this.adapter = adapter;
    }

    //   public MovieAsyncTask(MovieAdapter adapter){ movieAdapter = adapter; }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    @Override
    protected List<Movie> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        String sortMode = params[0];
        URL movieRequestUrl = NetworkUtility.buildUrl(sortMode);

        try {
            String jsonMovieResponse = NetworkUtility
                    .makeHttpRequest(movieRequestUrl);

            List simpleJsonMovieData = NetworkUtility
                    .extractFeatureFromJson(jsonMovieResponse);

            return simpleJsonMovieData;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(List<Movie> mMovieList) {
        if (mMovieList != null) {
            //adapter.setMovieList(mMovieList);
        }
    }
}

}
