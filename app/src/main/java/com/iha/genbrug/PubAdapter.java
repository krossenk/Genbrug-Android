package com.iha.genbrug;

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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PubAdapter extends RecyclerView.Adapter<PubAdapter.PubItemViewHolder> {


    private ArrayList<GiveItem> mDataset;
    private ImageLoader imgLoader;


    public PubAdapter(ArrayList<GiveItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PubItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_give_item, parent, false);


        return new PubItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PubItemViewHolder holder,  final int position) {
        // Get element from dataset for given position
        final GiveItem gi = mDataset.get(position);

        // Insert the contents of the current element into the view
        holder.tvHeadline.setText(gi.getHeadline());
        holder.tvDesc.setText(gi.getDescription());

        imgLoader = VolleySingleton.getInstance().getImageLoader();
        holder.givePhoto.setImageUrl(gi.getImageURL(),imgLoader);


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class PubItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvHeadline;
        protected TextView tvDesc;
        protected NetworkImageView givePhoto;

        public PubItemViewHolder( View v ) {
            super(v);
            tvHeadline = (TextView) v.findViewById(R.id.tv_headline);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            givePhoto = (NetworkImageView) v.findViewById(R.id.give_photo);

        }
    }


}
