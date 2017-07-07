package com.example.tekwan.popularmovies.Layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tekwan on 7/4/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private ArrayList<Movie> moviesList;
    private Context context;
    private MovieAdapterOnClickHandler mClickHandler;


    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, ArrayList<Movie> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
        mClickHandler = clickHandler;
    }


    public void setMovieList(List<Movie> mMovieList) {
        this.moviesList.addAll(mMovieList);
        notifyDataSetChanged();
    }
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie posterClick);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;

        public MovieAdapterViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.movieImage);
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

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        final Movie movieView = moviesList.get(position);

        Picasso.with(context)
                .load(movieView.getPosterUrl())
                .resize(300, 450)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == moviesList) return 0;
        return moviesList.size();
    };
}
