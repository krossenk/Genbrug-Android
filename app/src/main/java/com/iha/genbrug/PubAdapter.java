package com.iha.genbrug;

import android.app.Activity;
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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    long itemId;
    View vTest;

    String avator = "http://vmi19372.iry.dk:8880/RecycleWebService/images/testFilename1432768150187.jpeg";


    public PubAdapter(ArrayList<GiveItem> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PubItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {


        vTest = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_give_item, parent, false);

            return new PubItemViewHolder(vTest);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PubItemViewHolder holder,  final int position) {
        // Get element from dataset for given position
        final GiveItem gi = mDataset.get(position);
        imgLoader = VolleySingleton.getInstance().getImageLoader();

        // Insert the contents of the current element into the view

        if(gi.getHeadline() != null)
        {
            holder.tvHeadline.setText(gi.getHeadline());
        }
        else {
            holder.tvHeadline.setText("No Headline");
        }

        if(gi.getDescription() != null)
        {
            holder.tvDesc.setText(gi.getDescription());
        }
        else {
            holder.tvDesc.setText("No description!");
        }

    /*    if(gi.getAmount() != 0)
        {
            holder.amount.setText(Integer.toString(gi.getAmount()));
        }*/

        if(gi.getImageURL() != null)
        {

            holder.givePhoto.setImageUrl(gi.getImageURL(), imgLoader);
        }
        else {
            holder.givePhoto.setImageUrl(avator,imgLoader);
        }





       /* holder.givePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), TakersActivity.class);
                itemId = gi.getItemId();

                intent.putExtra("itemId", itemId);

                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).finish();

            }
        });*/

      holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                itemId = gi.getItemId();
                Intent intent = new Intent(v.getContext(), TakersActivity.class);

                intent.putExtra("itemId", itemId);

                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).finish();

            }
        });
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
        protected View itemView;
        protected TextView amount;


        public PubItemViewHolder( View v ) {
            super(v);
            tvHeadline = (TextView) v.findViewById(R.id.tv_headline);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            givePhoto = (NetworkImageView) v.findViewById(R.id.give_photo);
            itemView = v.findViewById(R.id.pub_layout);
            //amount = (TextView) v.findViewById(R.id.subscriptionsAmount);

        }
    }

}
