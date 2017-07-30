package com.example.tekwan.popularmovies.Database.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tekwan.popularmovies.Database.Contract.MovieFavoriteContract;

/**
 * Created by tekwan on 7/29/2017.
 */

public class FavoriteDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Favorites.db";

    public FavoriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        final String createTable =  "CREATE TABLE " +
                MovieFavoriteContract.FavoriteEntry.TABLE_NAME+ " (" +
                MovieFavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                MovieFavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_DB_ID + " TEXT NOT NULL, " +
                MovieFavoriteContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieFavoriteContract.FavoriteEntry.COLUMN_RELEASED_DATE + " TEXT , " +
                MovieFavoriteContract.FavoriteEntry.COLUMN_RATING + " DOUBLE , " +
                MovieFavoriteContract.FavoriteEntry.COLUMN_POSTER + " BLOB ) ";

        db.execSQL(createTable);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + MovieFavoriteContract.FavoriteEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
