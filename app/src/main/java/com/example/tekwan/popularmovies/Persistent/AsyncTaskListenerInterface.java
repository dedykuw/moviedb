package com.example.tekwan.popularmovies.Persistent;

import com.example.tekwan.popularmovies.DataModel.Movie;

import java.util.List;

/**
 * Created by Dedy on 7/6/2017.
 */

public interface AsyncTaskListenerInterface {
    void returnData(List<Movie> mMovieList);
    void preExecute();

}
