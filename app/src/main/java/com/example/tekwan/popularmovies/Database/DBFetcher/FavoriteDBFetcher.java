package com.example.tekwan.popularmovies.Database.DBFetcher;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.Database.Contract.MovieFavoriteContract;
import com.example.tekwan.popularmovies.Database.Helper.FavoriteDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tekwan on 7/30/2017.
 */

public class FavoriteDBFetcher {

    public static List<Movie> convertCursorToListMovie(Cursor movieCursor){

        List<Movie> movies = new ArrayList<>();
        if (movieCursor.moveToFirst()){
            do{

                String overview = movieCursor.getString(movieCursor.getColumnIndex(MovieFavoriteContract.FavoriteEntry.COLUMN_OVERVIEW));
                String poster = movieCursor.getString(movieCursor.getColumnIndex(MovieFavoriteContract.FavoriteEntry.COLUMN_POSTER));
                double rating = movieCursor.getDouble(movieCursor.getColumnIndex(MovieFavoriteContract.FavoriteEntry.COLUMN_RATING));
                String releasedDate = movieCursor.getString(movieCursor.getColumnIndex(MovieFavoriteContract.FavoriteEntry.COLUMN_RELEASED_DATE));
                String title = movieCursor.getString(movieCursor.getColumnIndex(MovieFavoriteContract.FavoriteEntry.COLUMN_TITLE));
                String id = movieCursor.getString(movieCursor.getColumnIndex(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_DB_ID));
                // do what ever you want here
                Movie movie = new Movie(poster,title,overview,rating,releasedDate,id);
                movie.setFavorite(true);
                movies.add(movie);
            }while(movieCursor.moveToNext());
        }
        return movies;
    }
    public static Cursor fetchAll(Context context){
        FavoriteDBHelper favoriteDBHelper = new FavoriteDBHelper(context);
        SQLiteDatabase db = favoriteDBHelper.getReadableDatabase();
        String[] projection = {
                MovieFavoriteContract.FavoriteEntry._ID,
                MovieFavoriteContract.FavoriteEntry.COLUMN_POSTER,
                MovieFavoriteContract.FavoriteEntry.COLUMN_RATING,
                MovieFavoriteContract.FavoriteEntry.COLUMN_RELEASED_DATE,
                MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_DB_ID,
                MovieFavoriteContract.FavoriteEntry.COLUMN_OVERVIEW,
                MovieFavoriteContract.FavoriteEntry.COLUMN_TITLE,

        };

        Cursor movieCursor = db.query(
                MovieFavoriteContract.FavoriteEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        return movieCursor;
    }
}
