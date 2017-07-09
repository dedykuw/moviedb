package com.example.tekwan.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.Layout.MovieAdapter;
import com.example.tekwan.popularmovies.Persistent.AsyncTaskListenerInterface;
import com.example.tekwan.popularmovies.Persistent.DataAsyncTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,AsyncTaskListenerInterface{

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.movie_recycler) RecyclerView movieRecyclerView;

    private ArrayList<Movie> moviesList;
    private MovieAdapter adapter;

    private static final String POPULAR = "popular";
    private static final String RATING = "rating";
    private String searhType = POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        moviesList = new ArrayList<Movie>();

        movieRecyclerView.setHasFixedSize(true);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new MovieAdapter(this,moviesList,getApplicationContext());

        movieRecyclerView.setAdapter(adapter);

        loadData(searhType);
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
            setSearchType(POPULAR);
            loadData(searhType);
            return true;
        }
        if(id == R.id.top_rated){
            setSearchType(RATING);
            loadData(searhType);
            return true;
        }
        if (id == R.id.refresh_button){
            loadData(searhType);
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
        if (moviesList!= null) {
            finishFetchExection();
            adapter.setMovieList(moviesList);
        }else {
            Toast.makeText(MainActivity.this, R.string.internet_problem,Toast.LENGTH_LONG).show();
            finishFetchExection();
        }
    }
    private void finishFetchExection(){
        progressBar.setVisibility(View.INVISIBLE);
        movieRecyclerView.setVisibility(View.VISIBLE);
    }
    private void setSearchType(String type){
        this.searhType = type;
    }
}
