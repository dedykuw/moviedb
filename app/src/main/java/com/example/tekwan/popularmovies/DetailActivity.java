package com.example.tekwan.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.DataModel.Review;
import com.example.tekwan.popularmovies.DataModel.Trailer;
import com.example.tekwan.popularmovies.Database.Contract.MovieFavoriteContract;
import com.example.tekwan.popularmovies.Database.Helper.FavoriteDBHelper;
import com.example.tekwan.popularmovies.Layout.ReviewAdapter;
import com.example.tekwan.popularmovies.Layout.TrailerAdapter;
import com.example.tekwan.popularmovies.Persistent.ReviewAsyncTackLoader;
import com.example.tekwan.popularmovies.Persistent.TrailerAsyncTaskLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerOnClick {
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.desc) TextView desc;
    @BindView(R.id.vote) TextView vote;
    @BindView(R.id.release_date) TextView releaseDateView;
    @BindView(R.id.ratingBar) RatingBar rate;
    @BindView(R.id.unfavorite) ImageButton unfavorite;
    @BindView(R.id.favorited) ImageButton favorited;

    private String originalTitle;
    private String movieOverview;
    private Double voteAverage;
    private String releaseDate;
    private String posterUrl;
    private String movieId;
    private String posterName;

    private LoaderManager loaderManager;

    private static final int TRAILER_LOADER = 23;
    private static final int REVIEW_LOADER = 24;

    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private FavoriteDBHelper favoriteDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        rate.setNumStars(5);
        rate.setMax(5);

        posterUrl = movie.getPosterUrl();
        originalTitle = movie.getOriginalTitle();
        movieOverview = movie.getMovieOverview();
        voteAverage = movie.getVoteAverage();
        releaseDate = movie.getReleaseDate();
        movieId = movie.getMovieId();
        posterName = movie.getPosterName();

        favoriteDBHelper = new FavoriteDBHelper(this);
        loaderManager = getSupportLoaderManager();

        init();
        initLoader();
    }

    private void initLoader() {
        initTrailerLoader();
        initReviewLoader();
    }

    private void initReviewLoader() {

        List<Review> reviewList = new ArrayList<>();

        RecyclerView reviewRecyclerView = (RecyclerView) findViewById(R.id.review_recycler);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewAdapter = new ReviewAdapter(reviewList,DetailActivity.this);

        reviewRecyclerView.setAdapter(reviewAdapter);
        LoaderManager.LoaderCallbacks reviewsLoader = new LoaderManager.LoaderCallbacks<List<Review>>(){

            @Override
            public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
                return  new ReviewAsyncTackLoader(DetailActivity.this,movieId);
            }

            @Override
            public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
                reviewAdapter.setReviewsData(data);
            }

            @Override
            public void onLoaderReset(Loader<List<Review>> loader) {

            }
        };

        loaderManager.initLoader(REVIEW_LOADER, null, reviewsLoader).forceLoad();

    }

    private void initTrailerLoader() {

        List<Trailer> trailerList = new ArrayList<>();

        RecyclerView trailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recycler);
        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        trailerAdapter = new TrailerAdapter(trailerList,DetailActivity.this);
        trailerRecyclerView.setAdapter(trailerAdapter);

        LoaderManager.LoaderCallbacks trailerLoader = new LoaderManager.LoaderCallbacks<List<Trailer>>() {

            @Override
            public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
                return new TrailerAsyncTaskLoader(DetailActivity.this,movieId);
            }

            @Override
            public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
                trailerAdapter.setTrailerList(data);
            }

            @Override
            public void onLoaderReset(Loader<List<Trailer>> loader) {

            }
        };
        loaderManager.initLoader(TRAILER_LOADER, null, trailerLoader).forceLoad();

    }
    private boolean getFavorite(){
        SQLiteDatabase db = favoriteDBHelper.getReadableDatabase();
        String[] projection = {
                MovieFavoriteContract.FavoriteEntry._ID
        };

        String selection = MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_DB_ID + " = ?";
        String[] selectionArgs = { movieId };


        Cursor c = db.query(
                MovieFavoriteContract.FavoriteEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        Boolean returnData = c.getCount() > 0;
        c.close();
        return returnData;
    }
    private void init(){

        title.setText(originalTitle);
        desc.setText(movieOverview);
        vote.setText(getString(R.string.rating)+" "+voteAverage.toString() +" from 10");
        releaseDateView.setText(getString(R.string.released_at)+" "+releaseDate);
        rate.setRating((float) (voteAverage / 2));

        if (getFavorite()){
            favorited.setVisibility(View.VISIBLE);
        }else {
            unfavorite.setVisibility(View.VISIBLE);
        }

        Picasso.with(DetailActivity.this)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        poster.setImageBitmap(bitmap);

                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {}

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                }
                );

    }
    public void navigateHome(View view){
        Intent home = new Intent(DetailActivity.this,MainActivity.class);
        startActivity(home);
    }

    public void favoriteThis(View view){

        poster.buildDrawingCache();
        final Bitmap bitmap = poster.getDrawingCache();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final File myImageFile = new File("image", posterName); // Create image file
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(myImageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        SQLiteDatabase db = favoriteDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_DB_ID, movieId);
        values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_TITLE, originalTitle);
        values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, movieOverview);
        values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_RATING, voteAverage);
        values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_RELEASED_DATE, releaseDate);
        values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, movieOverview);
        values.put(MovieFavoriteContract.FavoriteEntry.COLUMN_POSTER, posterName);
        long newRowId = db.insert(MovieFavoriteContract.FavoriteEntry.TABLE_NAME, null, values);
        if (newRowId > 0){
            unfavorite.setVisibility(View.GONE);
            favorited.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(Trailer trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public void unFavoriteThis(View view) {

        SQLiteDatabase db = favoriteDBHelper.getWritableDatabase();
        String selection = MovieFavoriteContract.FavoriteEntry.COLUMN_MOVIE_DB_ID + " LIKE ?";
        String[] selectionArgs = { movieId };
        int result = db.delete(MovieFavoriteContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);

        if (result > 0){
            favorited.setVisibility(View.GONE);
            unfavorite.setVisibility(View.VISIBLE);
        }
    }
}
