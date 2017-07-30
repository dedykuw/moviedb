package com.example.tekwan.popularmovies.Persistent;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.tekwan.popularmovies.DataModel.Review;
import com.example.tekwan.popularmovies.Utilities.NetworkUtility;

import java.net.URL;
import java.util.List;

/**
 * Created by tekwan on 7/29/2017.
 */

public class ReviewAsyncTackLoader extends AsyncTaskLoader<List<Review>> {
    private String id;
    public ReviewAsyncTackLoader(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    public List<Review> loadInBackground() {
        if (this.id == null || Integer.parseInt(this.id) < 1 ) {
            return null;
        }

        URL url = NetworkUtility.getReviewUrl(id);

        try {
            String jsonReviewResponse = NetworkUtility
                    .makeHttpRequest(url);

            return NetworkUtility.extractReviewFromJson(jsonReviewResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
