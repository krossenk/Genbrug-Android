package com.iha.genbrug;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.GenbrugItemViewHolder> {
    private ArrayList<GenbrugItem> mDataset;
    private static int pos;
    private ImageLoader imgLoader;
    private RequestQueue requestQueue;

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
    public void onBindViewHolder(GenbrugItemViewHolder holder,  final int position) {
        // Get element from dataset for given position
        final GenbrugItem gi = mDataset.get(position);

        // Insert the contents of the current element into the view
        holder.tvHeadline.setText(gi.getHeadline());
        holder.tvDesc.setText(gi.getDescription());

        imgLoader = VolleySingleton.getInstance().getImageLoader();
        holder.ivPhoto.setImageUrl(gi.getImageURL(), imgLoader);




        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                pos = position;
                String imageUrl = gi.getImageURL();
                String headline = gi.getHeadline();
                String desc = gi.getDescription();
                long itemId = gi.getItemId();

                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("headline", headline);
                intent.putExtra("desc", desc);
                intent.putExtra("itemId",itemId);
                pos = position;
                v.getContext().startActivity(intent);
                ((Activity)v.getContext()).finish();
            }
        });
    }

    public static int getPostion ()
    {
        return pos;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class GenbrugItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvHeadline;
        protected TextView tvDesc;
        protected NetworkImageView ivPhoto;

        public GenbrugItemViewHolder( View v ) {
            super(v);
            tvHeadline = (TextView) v.findViewById(R.id.tv_headline);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            ivPhoto = (NetworkImageView) v.findViewById(R.id.iv_photo);

        }
    }

}
