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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubItemViewHolder> {


    private ArrayList<TakeItem> mDataset;
    private String imagePath;

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
    public void onBindViewHolder(SubItemViewHolder holder,  final int position) {
        // Get element from dataset for given position
        final TakeItem gi = mDataset.get(position);

        // Insert the contents of the current element into the view
        holder.tvHeadline.setText(gi.getHeadline());
        holder.tvDesc.setText(gi.getDescription());
        imagePath = gi.getImagePath();

        LongOperation task = new LongOperation();

        try {
            Bitmap bitmap = task.execute().get();
            holder.ivPhoto.setImageBitmap(getCroppedBitmap(bitmap,300));


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvHeadline;
        protected TextView tvDesc;
        protected ImageView ivPhoto;

        public SubItemViewHolder( View v ) {
            super(v);
            tvHeadline = (TextView) v.findViewById(R.id.tv_headline);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            ivPhoto = (ImageView) v.findViewById(R.id.imageView1);

        }
    }

    public class LongOperation extends AsyncTask<Bitmap, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Bitmap... params) {

            String pictureUri = imagePath;
            Uri myUri = Uri.parse(pictureUri);
            String test = myUri.toString();
            URL url = null;
            try {
                url = new URL(test);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStream in = null;
            try {
                in = (InputStream) url.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap  bitmap = BitmapFactory.decodeStream(in);

            return bitmap;

        }
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
                sbmp.getWidth() / 2+0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);


        return output;
    }
}
