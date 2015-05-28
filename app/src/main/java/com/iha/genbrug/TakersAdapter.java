package com.iha.genbrug;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class TakersAdapter extends RecyclerView.Adapter<TakersAdapter.TakerItemViewHolder>  {


    private ArrayList<TakerItem> mDataset;
    private ImageLoader imgLoader;
    String avator = "http://vmi19372.iry.dk:8880/RecycleWebService/images/testFilename1432768150187.jpeg";


    public TakersAdapter(ArrayList<TakerItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TakerItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_taker_item, parent, false);

        return new TakerItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final TakerItemViewHolder holder,  final int position) {
        // Get element from dataset for given position
        final TakerItem gi = mDataset.get(position);
        imgLoader = VolleySingleton.getInstance().getImageLoader();

        if(gi.getDate() != null && gi.getPickUpTime() != null)
            {
                holder.dateView.setText(gi.getDate());
                holder.timeView.setText(gi.getPickUpTime());
            }


            if (gi.getImageURL() != null)
            {
                /*imgLoader = VolleySingleton.getInstance().getImageLoader();
                 holder.takerPhoto.setImageUrl(gi.getImageURL(),imgLoader);*/
            }
        else {
                holder.takerPhoto.setImageUrl(avator,imgLoader);
            }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class TakerItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView dateView;
        protected TextView timeView;
        protected NetworkImageView takerPhoto;

        public TakerItemViewHolder( View v ) {
            super(v);
            dateView = (TextView) v.findViewById(R.id.dateView);
            timeView = (TextView) v.findViewById(R.id.timeView);
            takerPhoto = (NetworkImageView) v.findViewById(R.id.taker_photo);

        }
    }
}
