package com.example.tekwan.popularmovies.Layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tekwan.popularmovies.DataModel.Review;
import com.example.tekwan.popularmovies.R;

import java.util.List;

/**
 * Created by tekwan on 7/29/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapaterViewHolder> {
    private List<Review> reviews;
    private Context context;

    public ReviewAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }
    public void setReviewsData(List<Review> reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public ReviewAdapter.ReviewAdapaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewAdapter.ReviewAdapaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapaterViewHolder holder, int position) {
        final Review review = reviews.get(position);
        holder.setData(review);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewAdapaterViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;
        private final TextView author;
        public ReviewAdapaterViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);
        }

        public void setData(Review review) {
            content.setText(review.getContent());
            author.setText(review.getAuthor());
        }
    }
}
