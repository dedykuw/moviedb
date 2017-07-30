package com.example.tekwan.popularmovies.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.tekwan.popularmovies.Database.Contract.MovieFavoriteContract;
import com.example.tekwan.popularmovies.Database.DBFetcher.FavoriteDBFetcher;
import com.example.tekwan.popularmovies.Database.Helper.FavoriteDBHelper;

/**
 * Created by tekwan on 7/30/2017.
 */

public class FavoriteContentProvider extends ContentProvider{

    private FavoriteDBHelper favoriteDBHelper;

    private static final int FAVORITES= 300;
    private static final int FAVORITES_WITH_ID = 301;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private Context context;

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieFavoriteContract.AUTHORITY,MovieFavoriteContract.PATH_FAVORITES,FAVORITES);
        uriMatcher.addURI(MovieFavoriteContract.AUTHORITY,MovieFavoriteContract.PATH_FAVORITES + "/#",FAVORITES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        favoriteDBHelper = new FavoriteDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int matched = sUriMatcher.match(uri);
        Cursor retCursor = null;
        switch (matched){
            case FAVORITES :
                retCursor = FavoriteDBFetcher.fetchAll(context);
                break;
            case FAVORITES_WITH_ID :
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri: "+ uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
