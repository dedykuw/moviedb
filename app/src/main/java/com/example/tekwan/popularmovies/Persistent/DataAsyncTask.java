package com.example.tekwan.popularmovies.Persistent;

import android.os.AsyncTask;

import java.net.URL;
import java.util.List;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.Layout.MovieAdapter;
import com.example.tekwan.popularmovies.Utilities.NetworkUtility;

/**
 * Created by tekwan on 7/4/2017.
 */

public class DataAsyncTask extends AsyncTask<Object,Void,List<Movie>>{

    private static final String TAG = DataAsyncTask.class.getSimpleName();
    private MovieAdapter adapter;
    private AsyncTaskListenerInterface listener;

    public DataAsyncTask(AsyncTaskListenerInterface listener) {
            this.listener = listener;
    }

    @Override
    protected List<Movie> doInBackground(Object... strings) {

        if (strings.length == 0) {
            return null;
        }
        String sort = strings[0].toString();
        URL url = NetworkUtility.buildUrl(sort);

        try {
            String jsonMovieResponse = NetworkUtility
                    .makeHttpRequest(url);

            List simpleJsonMovieData = NetworkUtility
                    .extractFeatureFromJson(jsonMovieResponse);

            return simpleJsonMovieData;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        listener.preExecute();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies!= null) {
            listener.returnData(movies);
        }
    }
}


