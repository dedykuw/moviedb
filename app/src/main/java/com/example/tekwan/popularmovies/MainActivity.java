package com.example.tekwan.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private ArrayList<Movie> moviesList;
    private MovieAdapter adapter;
    private ProgressBar progressBar;

    private static final String POPULAR = "popular";
    private static final String RATING = "rating";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesList = new ArrayList<Movie>();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        movieRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler);
        movieRecyclerView.setHasFixedSize(true);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(this,moviesList,getApplicationContext());

        movieRecyclerView.setAdapter(adapter);

        loadData(POPULAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    private void loadData(String type){

        adapter.setMovieList(new ArrayList<Movie>());
        new DataAsyncTask(this).execute(type);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.popular){
            loadData(POPULAR);
            return true;
        }

        if(id == R.id.top_rated){
            loadData(RATING);
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
        progressBar.setVisibility(View.VISIBLE);
        movieRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void returnData(List<Movie> moviesList) {
        progressBar.setVisibility(View.INVISIBLE);
        movieRecyclerView.setVisibility(View.VISIBLE);
        adapter.setMovieList(moviesList);

    }
}
