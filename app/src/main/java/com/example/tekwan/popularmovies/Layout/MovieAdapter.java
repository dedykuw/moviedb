package com.example.tekwan.popularmovies.Layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tekwan on 7/4/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private Context context;
    private MovieAdapterOnClickHandler mClickHandler;

    public void setMovieList(List<Movie> mMovieList) {
        this.moviesList.addAll(mMovieList);
        notifyDataSetChanged();
    }
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie posterClick);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, ArrayList<Movie> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;

        public MovieAdapterViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie posterClick = moviesList.get(adapterPosition);
            mClickHandler.onClick(posterClick);
        }
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (null == moviesList) return 0;
        return moviesList.size();
    }
}
