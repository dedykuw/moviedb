package com.example.tekwan.popularmovies.Database.Contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tekwan on 7/29/2017.
 */

public class MovieFavoriteContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    public static final String AUTHORITY = "com.example.tekwan.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";
    private MovieFavoriteContract() {}

    /* Inner class that defines the table contents */
    public static class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_DB_ID = "moviedb_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASED_DATE = "released_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER = "poster";


    }
}
