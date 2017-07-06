package com.example.tekwan.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.Layout.MovieAdapter;
import com.example.tekwan.popularmovies.Persistent.AsyncTaskListenerInterface;
import com.example.tekwan.popularmovies.Persistent.DataAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,AsyncTaskListenerInterface{

    private RecyclerView movieRecyclerView;
    private ArrayList<Movie> moviesList = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;
    private AsyncTask dataAsyncTask;

    private static final String SORT_BY_POPULAR = "popular";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        dataAsyncTask = new DataAsyncTask(this);

        dataAsyncTask.execute("popular");


        movieRecyclerView = (RecyclerView) findViewById(R.id.movie_recyler);
        movieRecyclerView.setHasFixedSize(true);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new MovieAdapter(this,moviesList,getApplicationContext());
        movieRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.popular){
            return true;
        }

        if(id == R.id.top_rated){
            return true;
        };
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(Movie posterClick) {
        Intent intenteded = new Intent(MainActivity.this,DetailActivity.class);
        intenteded.putExtra("movie",posterClick);
        startActivity(intenteded);
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void returnData(List<Movie> moviesList) {
        progressBar.setVisibility(View.INVISIBLE);
        for (Movie movie : moviesList) {
            Log.i("TITLE: ", movie.getOriginalTitle());
        }
    }
}
