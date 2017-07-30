package com.example.tekwan.popularmovies.Layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.tekwan.popularmovies.DataModel.Trailer;
import com.example.tekwan.popularmovies.R;
import java.util.List;

/**
 * Created by tekwan on 7/29/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>  {
    private  List<Trailer> trailers;
    private Context context;

    private final TrailerOnClick clickHandler;

    public TrailerAdapter(List<Trailer> trailers, Context context) {
        this.trailers = trailers;
        this.context = context;
        clickHandler = (TrailerOnClick) context;
    }
    public void setTrailerList(List<Trailer> data){
        trailers = data;
        notifyDataSetChanged();
    }

    public interface TrailerOnClick {
        void onClick(Trailer trailer);
    }
    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        final Trailer trailer = trailers.get(position);
        holder.setDataTrailer(trailer);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder  {
        private final TextView trailerName;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView) itemView.findViewById(R.id.trailer_name);
        }

        public void setDataTrailer(final Trailer trailer){
            trailerName.setText(trailer.getName());
            trailerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickHandler.onClick(trailer);
                }
            });
        }
    }
}
