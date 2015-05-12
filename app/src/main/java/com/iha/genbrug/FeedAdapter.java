package com.iha.genbrug;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.GenbrugItemViewHolder> {
    private ArrayList<GenbrugItem> mDataset;

    // CONSTRUCTOR
    public FeedAdapter(ArrayList<GenbrugItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GenbrugItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genbrug_item, parent, false);

        return new GenbrugItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(GenbrugItemViewHolder holder, int position) {
        // Get element from dataset for given position
        GenbrugItem gi = mDataset.get(position);

        // Insert the contents of the current element into the view
        holder.tvHeadline.setText(gi.getHeadline());
        holder.tvDesc.setText(gi.getDescription());
        holder.ivPhoto.setImageDrawable(gi.getPhotoDrawable());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class GenbrugItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvHeadline;
        protected TextView tvDesc;
        protected ImageView ivPhoto;

        public GenbrugItemViewHolder( View v ) {
            super(v);
            tvHeadline = (TextView) v.findViewById(R.id.tv_headline);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            ivPhoto = (ImageView) v.findViewById(R.id.iv_photo);
        }
    }

}
