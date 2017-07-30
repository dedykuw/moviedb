package com.example.tekwan.popularmovies.Persistent;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.tekwan.popularmovies.DataModel.Trailer;
import com.example.tekwan.popularmovies.Utilities.NetworkUtility;

import java.net.URL;
import java.util.List;

/**
 * Created by tekwan on 7/29/2017.
 */

public class TrailerAsyncTaskLoader extends AsyncTaskLoader<List<Trailer>> {
    private String id;
    public TrailerAsyncTaskLoader(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    public List<Trailer> loadInBackground() {
        if (this.id == null || Integer.parseInt(this.id) < 1 ) {
            return null;
        }

        URL url = NetworkUtility.getTrailerUrl(id);

        try {
            String jsonTrailerResponse = NetworkUtility
                    .makeHttpRequest(url);

            return NetworkUtility.extractTrailerFromJson(jsonTrailerResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
