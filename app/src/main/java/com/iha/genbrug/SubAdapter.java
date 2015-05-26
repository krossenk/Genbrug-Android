package com.iha.genbrug;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubItemViewHolder>  {


    private ArrayList<TakeItem> mDataset;
    private ImageLoader imgLoader;


    public SubAdapter(ArrayList<TakeItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SubItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_take_item, parent, false);

        return new SubItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final SubItemViewHolder holder,  final int position) {
        // Get element from dataset for given position
        final TakeItem gi = mDataset.get(position);


        // Insert the contents of the current element into the view
        holder.tvHeadline.setText(gi.getHeadline());
        holder.tvDesc.setText(gi.getDescription());

        imgLoader = VolleySingleton.getInstance().getImageLoader();
        holder.takePhoto.setImageUrl(gi.getImageURL(),imgLoader);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvHeadline;
        protected TextView tvDesc;
        protected NetworkImageView takePhoto;

        public SubItemViewHolder( View v ) {
            super(v);
            tvHeadline = (TextView) v.findViewById(R.id.tv_headline);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            takePhoto = (NetworkImageView) v.findViewById(R.id.take_photo);

        }
    }
}
