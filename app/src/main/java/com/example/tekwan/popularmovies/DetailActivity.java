package com.example.tekwan.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private ImageView poster;
    private TextView title;
    private TextView desc;
    private TextView vote;
    private TextView releaseDateView;
    private RatingBar rate;

    private String originalTitle;
    private String movieOverview;
    private Double voteAverage;
    private String releaseDate;
    private String posterUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie= intent.getParcelableExtra("movie");



        poster = (ImageView) findViewById(R.id.poster);
        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);
        vote = (TextView) findViewById(R.id.vote);
        releaseDateView = (TextView) findViewById(R.id.release_date);
        rate = (RatingBar) findViewById(R.id.ratingBar);

        rate.setNumStars(5);
        rate.setMax(5);

        posterUrl = movie.getPosterUrl();
        originalTitle = movie.getOriginalTitle();
        movieOverview = movie.getMovieOverview();
        voteAverage = movie.getVoteAverage();
        releaseDate = movie.getReleaseDate();

        init();
    }
    protected void init(){

        title.setText(originalTitle);
        desc.setText(movieOverview);
        vote.setText(getString(R.string.rating) +voteAverage.toString() +" from 10");
        releaseDateView.setText(getString(R.string.released_at)+ releaseDate);
        rate.setRating((float) (voteAverage / 2));

        Picasso.with(DetailActivity.this)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder)
                .into(poster);

    }
    public void navigateHome(View view){
        Intent home = new Intent(DetailActivity.this,MainActivity.class);
        startActivity(home);
    }
}
