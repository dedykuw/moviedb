package com.example.tekwan.popularmovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.Database.Contract.MovieFavoriteContract;
import com.example.tekwan.popularmovies.Database.DBAction.FavoriteDBAction;
import com.example.tekwan.popularmovies.Database.Helper.FavoriteDBHelper;
import com.example.tekwan.popularmovies.Layout.MovieAdapter;
import com.example.tekwan.popularmovies.Persistent.AsyncTaskListenerInterface;
import com.example.tekwan.popularmovies.Persistent.MovieAsyncTaskLoader;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        AsyncTaskListenerInterface,
        LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final int SEARCH_LOADER = 33;
    private static final int FAVORITE_LOADER = 34;
    private static final String LIST_STATE_KEY = "scrollstate" ;
    private final LoaderManager loaderManager = getSupportLoaderManager();

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.movie_recycler) RecyclerView movieRecyclerView;

    private MovieAdapter adapter;
    private GridLayoutManager layoutManager;
    private boolean isLoading = true;

    private static final String POPULAR = "popular";
    private static final String RATING = "rating";
    private String searhType = POPULAR;
    private Boolean showFavorite = true;
    private static Boolean isShowFavorite = true;
    private LoaderManager.LoaderCallbacks<Cursor> dbFavoriteLoader;

    private Bundle savedState;
    Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);
        FavoriteDBHelper dbHelper = new FavoriteDBHelper(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List moviesList = new ArrayList<Movie>();
        layoutManager = new GridLayoutManager(this,2);
        movieRecyclerView.setHasFixedSize(true);
        movieRecyclerView.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(this,moviesList,getApplicationContext());

        movieRecyclerView.setAdapter(adapter);
        movieRecyclerView.addOnScrollListener(new ScrollListener());

        if (savedInstanceState == null) {
            savedState =  new Bundle();
            savedState.putString(SEARCH_QUERY_URL_EXTRA,POPULAR);
        }else {
            savedState = savedInstanceState;
        }


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_show_favorite))) setIsShowFavorite(sharedPreferences.getBoolean(key,getResources().getBoolean(R.bool.pref_show_favorite)));
    }

    private class ScrollListener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isLoading) return;
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
            if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                Log.d("finish", "finish loading");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPreferences();
        loadData();
    }
    private void restoreScrollState(){
        if (mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        }
    }

    private void setPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setIsShowFavorite(sharedPreferences.getBoolean(getString(R.string.pref_show_favorite),
                getResources().getBoolean(R.bool.pref_show_favorite)));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    private void loadData(){
        adapter.setMovieList(new ArrayList<Movie>());

        Loader<String> dataLoader = loaderManager.getLoader(SEARCH_LOADER);
        if (dataLoader == null){
            loaderManager.initLoader(SEARCH_LOADER, savedState, this).forceLoad();
        }else {
            loaderManager.restartLoader(SEARCH_LOADER, savedState, this).forceLoad();
        }
        if (isShowFavorite()) loadFromProvider();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular){
            savedState.putString(SEARCH_QUERY_URL_EXTRA,POPULAR);
            loadData();
            return true;
        }
        if(id == R.id.top_rated){
            savedState.putString(SEARCH_QUERY_URL_EXTRA,RATING);
            loadData();
            return true;
        }
        if (id == R.id.refresh_button){
            loadData();
            return true;
        }
        if (id == R.id.action_setting){
            Intent settingIntent = new Intent(this,SettingActivity.class);
            startActivity(settingIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(Movie posterClick) {
        Intent intended = new Intent(MainActivity.this,DetailActivity.class);
        intended.putExtra("movie",posterClick);
        startActivity(intended);
    }

    @Override
    public void preExecute() {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        movieRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Boolean isShowFavorite() {
        return isShowFavorite;
    }
    private void setIsShowFavorite(Boolean set){
        isShowFavorite = set;
    }

    @Override
    public void returnData(List<Movie> moviesLists) {
        if (moviesLists!= null) {
            finishFetchExection();
            adapter.setMovieList(moviesLists);
            restoreScrollState();
        }else {
            Toast.makeText(MainActivity.this, R.string.internet_problem,Toast.LENGTH_LONG).show();
            loadFromProvider();
            finishFetchExection();
        }
    }

    @Override
    public void addData(List<Movie> movies) {
        if (movies!= null) {
            finishFetchExection();
            adapter.addMovieList(movies);
        }
    }

    private void finishFetchExection(){
        isLoading = false;
        progressBar.setVisibility(View.INVISIBLE);
        movieRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int id,final Bundle args) {
        return new MovieAsyncTaskLoader(this,this,args.getString(SEARCH_QUERY_URL_EXTRA));
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        returnData(data);
    }


    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY_URL_EXTRA,savedState.getString(SEARCH_QUERY_URL_EXTRA));
        mListState = layoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    private void loadFromProvider(){
        LoaderManager loaderManager = getSupportLoaderManager();
        dbFavoriteLoader = new LoaderManager.LoaderCallbacks<Cursor>(){
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                final Uri uri = MovieFavoriteContract.FavoriteEntry.CONTENT_URI;
                    return new AsyncTaskLoader<Cursor>(MainActivity.this) {
                        @Override
                        public Cursor loadInBackground() {
                            try {
                                return getContentResolver().query(uri,
                                        null,
                                        null,
                                        null,
                                        null);

                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    };
                }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                addData(FavoriteDBAction.convertCursorToListMovie(data));
            }
            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };

        loaderManager.initLoader(FAVORITE_LOADER, null, dbFavoriteLoader).forceLoad();


    }
}
