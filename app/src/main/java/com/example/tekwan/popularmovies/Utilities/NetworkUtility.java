package com.example.tekwan.popularmovies.Utilities;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.example.tekwan.popularmovies.BuildConfig;
import com.example.tekwan.popularmovies.DataModel.Movie;
import com.example.tekwan.popularmovies.DataModel.Review;
import com.example.tekwan.popularmovies.DataModel.Trailer;

/**
 * Created by tekwan on 7/4/2017.
 */

public class NetworkUtility {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = NetworkUtility.class.getSimpleName();

    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String MOVIE_ID_PATH = "id";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String SORT_BY_POPULAR = "popular";
    private static final String SORT_BY_RATING = "rating";

    private static final String TRAILER_KEY_PATH = "key";
    private static final String TRAILER_NAME_PATH = "name";
    private static final String TRAILER_SITE_PATH = "site";
    private static final String TRAILER_TYPE_PATH = "type";
    private static final String TRAILER_ID_PATH = "id";

    private static final String REVIEWS_PATH = "reviews" ;
    private static final String REVIEW_ID_PATH = "id";
    private static final String REVIEW_AUTHOR_PATH = "author";
    private static final String REVIEW_CONTENT_PATH = "content";
    private static final String REVIEW_URL_PATH =  "url";

    private static final String API_KEY = "api_key";

    private final static String QUERY_PARAM = "q";

    private static final String BASE_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular";
    private static final String BASE_URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String MOVIE_PATH = "movie";
    private static final String VIDEOS_PATH= "videos";


    private static final String MOVIE_DB_KEY = BuildConfig.MovieDBKey;


    private NetworkUtility() {
    }

    private static List<Movie> fetchMoviesData(String requestUrl) {
        URL url = buildUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractFeatureFromJson(jsonResponse);
    }

    public static URL buildUrl(String sortMode) {
        URL url = null;
        try {
            if (sortMode.equals(SORT_BY_POPULAR)) {
                Uri builtUri = Uri.parse(BASE_URL_POPULAR).buildUpon()
                        .appendQueryParameter(API_KEY,MOVIE_DB_KEY)
                        .build();
                url = new URL(builtUri.toString());
            } else if (sortMode.equals(SORT_BY_RATING)) {
                Uri builtUri = Uri.parse(BASE_URL_TOP_RATED).buildUpon()
                        .appendQueryParameter(API_KEY, MOVIE_DB_KEY)
                        .build();
                url = new URL(builtUri.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL getTrailerUrl(String id){
        URL url = null;
        try {
             Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                                .appendPath(MOVIE_PATH)
                                .appendPath(id)
                                .appendPath(VIDEOS_PATH)
                                .appendQueryParameter(API_KEY, MOVIE_DB_KEY)
                                .build();
             url = new URL(builtUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL getReviewUrl(String id) {

        URL url = null;
        try {
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(MOVIE_PATH)
                    .appendPath(id)
                    .appendPath(REVIEWS_PATH)
                    .appendQueryParameter(API_KEY, MOVIE_DB_KEY)
                    .build();
            url = new URL(builtUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Movie> extractFeatureFromJson(String movieJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }
        List<Movie> movies = new ArrayList<>();
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(movieJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray movieArray = baseJsonResponse.getJSONArray("results");

// For each earthquake in the earthquakeArray, create an {@link Movie} object
            for (int i = 0; i < movieArray.length(); i++) {

                // Get a single movie description at position i within the list of movies
                JSONObject currentMovie = movieArray.getJSONObject(i);
                String movieId = currentMovie.getString(MOVIE_ID_PATH);
                String posterName = currentMovie.getString(KEY_POSTER_PATH);

                // Extract the value for the key called "original_title"
                String movieName = currentMovie.getString(KEY_ORIGINAL_TITLE);

                String overviewName = currentMovie.getString(KEY_OVERVIEW);

                Double voteName = currentMovie.getDouble(KEY_VOTE_AVERAGE);

                String releaseName = currentMovie.getString(KEY_RELEASE_DATE);

                Movie movie = new Movie(posterName, movieName, overviewName, voteName, releaseName,movieId);
                movie.setFavorite(false);
                movies.add(movie);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing movies JSON results", e);
        }

        // Return the list of movies
        return movies;
    }
    public static List<Trailer> extractTrailerFromJson(String trailerJson){
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(trailerJson)) {
            return null;
        }
        List<Trailer> trailers = new ArrayList<>();
        try {

            JSONObject baseJsonResponse = new JSONObject(trailerJson);
            JSONArray trailerArrays = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < trailerArrays.length(); i++) {
                JSONObject trailerExtracted = trailerArrays.getJSONObject(i);


                    String trailerId = trailerExtracted.getString(TRAILER_ID_PATH);

                    String trailerName = trailerExtracted.getString(TRAILER_NAME_PATH);

                    String trailerKey = trailerExtracted.getString(TRAILER_KEY_PATH);

                    String trailerSite = trailerExtracted.getString(TRAILER_SITE_PATH);

                    String trailerType = trailerExtracted.getString(TRAILER_TYPE_PATH);


                    Trailer trailer = new Trailer(trailerId,trailerType,trailerKey,trailerSite,trailerName);
                    trailers.add(trailer);


            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing trailer JSON results", e);
        }

        return trailers;

    }


    public static List<Review> extractReviewFromJson(String jsonReviewResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonReviewResponse)) {
            return null;
        }
        List<Review> reviews = new ArrayList<>();
        try {

            JSONObject baseJsonResponse = new JSONObject(jsonReviewResponse);
            JSONArray reviewArrays= baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < reviewArrays.length(); i++) {

                JSONObject reviewExtracted = reviewArrays.getJSONObject(i);

                String reviewId = reviewExtracted.getString(REVIEW_ID_PATH);

                String reviewAuthor = reviewExtracted.getString(REVIEW_AUTHOR_PATH);

                String reviewContent = reviewExtracted.getString(REVIEW_CONTENT_PATH);

                String reviewUrl = reviewExtracted.getString(REVIEW_URL_PATH);


                Review review = new Review(reviewId,reviewAuthor,reviewContent,reviewUrl);
                reviews.add(review);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing trailer JSON results", e);
        }

        return reviews;

    }
}
