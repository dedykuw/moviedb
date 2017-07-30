package com.example.tekwan.popularmovies.Layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.R;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by tekwan on 7/4/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private List<Movie> moviesList;
    private final Context context;
    private final MovieAdapterOnClickHandler mClickHandler;


    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, List<Movie> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
        mClickHandler = clickHandler;
    }


    public void setMovieList(List<Movie> mMovieList) {
        if (this.moviesList.size() > 0) this.moviesList.clear();
        this.moviesList.addAll(mMovieList);
        notifyDataSetChanged();
    }
    public void addMovieList(List<Movie> movies){
        this.moviesList.addAll(movies);
        notifyDataSetChanged();
    }
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie posterClick);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;
        private final ImageView favorite;
        public MovieAdapterViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.movieImage);
            favorite = (ImageView) view.findViewById(R.id.image_favorite);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie posterClick = moviesList.get(adapterPosition);
            mClickHandler.onClick(posterClick);
        }
        public void setData(Movie movie){
            Picasso.with(context)
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
            if (movie.getFavorite()){
                favorite.setVisibility(View.VISIBLE);
            }else {
                favorite.setVisibility(View.GONE);
            }
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
        holder.setData(movieView);
    }

    @Override
    public int getItemCount() {
        if (null == moviesList) return 0;
        return moviesList.size();
    }
}
