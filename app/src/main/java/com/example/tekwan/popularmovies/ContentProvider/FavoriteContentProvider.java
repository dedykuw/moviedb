package com.example.tekwan.popularmovies.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.tekwan.popularmovies.Database.Contract.MovieFavoriteContract;
import com.example.tekwan.popularmovies.Database.DBAction.FavoriteDBAction;
import com.example.tekwan.popularmovies.Database.Helper.FavoriteDBHelper;

/**
 * Created by tekwan on 7/30/2017.
 */

public class FavoriteContentProvider extends ContentProvider{

    private FavoriteDBHelper favoriteDBHelper;

    private static final int FAVORITES= 100;
    private static final int FAVORITES_WITH_ID = 101;
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
                retCursor = FavoriteDBAction.fetchAll(context);
                break;
            case FAVORITES_WITH_ID :
                String id = uri.getPathSegments().get(1);
                retCursor = FavoriteDBAction.getOne(context,id);
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

        int matched = sUriMatcher.match(uri);
        Uri returnedUri;

        switch (matched){
            case FAVORITES :
                long id = FavoriteDBAction.insertRow(values,context);
                if (id > 0){
                    returnedUri = ContentUris.withAppendedId(MovieFavoriteContract.FavoriteEntry.CONTENT_URI,id);
                }else {
                    throw new SQLException("Failed to instert row "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri: "+ uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String id = uri.getPathSegments().get(1);
        return FavoriteDBAction.deleteRow(context,id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
