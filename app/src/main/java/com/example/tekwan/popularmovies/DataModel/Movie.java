package com.example.tekwan.popularmovies.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tekwan on 7/4/2017.
 */

public class Movie implements Parcelable{
    private String posterUrl;
    private String originalTitle;
    private String movieOverview;
    private Boolean isFavorite;
    private double voteAverage;
    private String releaseDate;

    private String movieId;

    public Movie(String posterUrl, String originalTitle, String movieOverview, Double voteAverage, String releaseDate,String id) {
        this.posterUrl = posterUrl;
        this.originalTitle = originalTitle;
        this.movieOverview = movieOverview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.movieId = id;
    }

    public String getPosterUrl() {
        String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";
        return BASE_POSTER_URL + posterUrl;
    }
    public String getPosterName(){
        return posterUrl;
    }
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setPosterUrl(String poster) {
        this.posterUrl = poster;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    private Movie(Parcel in) {
        posterUrl = in.readString();
        originalTitle = in.readString();
        movieOverview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
        movieId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(originalTitle);
        dest.writeString(movieOverview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(movieId);
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
