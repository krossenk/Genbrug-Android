package com.iha.genbrug;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;
import java.util.List;
import webservice.Subscription;
import webservice.User;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.GenbrugItemViewHolder> {
    private ArrayList<GenbrugItem> mDataset;
    private ImageLoader imgLoader;
    private static int pos;
    private GlobalSettings  globalSettings =GlobalSettings.getInstance();
    long  userId;
    List<Subscription> subList;
    boolean itemSubscribedCheck = false;
    private Context ctx;
    long publicationId;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            serverService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    // CONSTRUCTOR
    public FeedAdapter(ArrayList<GenbrugItem> myDataset, Context context) {

        this.ctx = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GenbrugItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        User user = globalSettings.getUserFromPref();
        userId = user.id;
        Intent intent = new Intent(ctx, ServerService.class);
        ctx.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genbrug_item, parent, false);
        subList = SubscriptionsFragment.userSubscriptionsResponseList;

        return new GenbrugItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final GenbrugItemViewHolder holder,  final int position) {
        // Get element from dataset for given position
        final GenbrugItem gi = mDataset.get(position);

        // Insert the contents of the current element into the view
        holder.tvHeadline.setText(gi.getHeadline());
        holder.tvDesc.setText(gi.getDescription());
        publicationId = gi.getItemId();


        if(userId != 0)
        {
            if(subList != null)
            {
                for (Subscription sub : subList) {
                    if (sub.publicationId.id == publicationId) {
                        itemSubscribedCheck = true;
                    }

                }

                if(itemSubscribedCheck)
                {
                   holder.ibSubscribe.setImageResource(R.drawable.ic_sub_selected);

                }
                else {
                    holder.ibSubscribe.setImageResource(R.drawable.ic_sub_not_selected);
                }
            }

            else {
                itemSubscribedCheck = false;
            }
        }


        // Parsa: Get current state of item (item from feed) and do:
        // holder.ibSubscribe.setPressed(true); if already subscribed.

        holder.ibSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.ib_feed_subscribe) {
                    //If user clicks on subscribe button.

                    if(itemSubscribedCheck)
                    {
                        Toast.makeText(ctx, gi.getHeadline() + " is already subscribed", Toast.LENGTH_SHORT).show();

                    }

                    else
                    {
                        serverService.createSubscription(userId, publicationId);
                        Toast.makeText(ctx, gi.getHeadline()+ " is subscribed", Toast.LENGTH_SHORT).show();
                        holder.ibSubscribe.setImageResource(R.drawable.ic_sub_selected);
                        itemSubscribedCheck = true;
                    }
                }
            }
        });

        imgLoader = VolleySingleton.getInstance().getImageLoader();
        holder.ivPhoto.setImageUrl(gi.getImageURL(), imgLoader);

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String imageUrl = gi.getImageURL();
                String headline = gi.getHeadline();
                String desc = gi.getDescription();
                long itemId = gi.getItemId();
                pos = position;


                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("headline", headline);
                intent.putExtra("desc", desc);
                intent.putExtra("itemId",itemId);
                v.getContext().startActivity(intent);
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
        protected ImageButton ibSubscribe;

        public GenbrugItemViewHolder( View v ) {
            super(v);
            tvHeadline = (TextView) v.findViewById(R.id.tv_headline);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            ivPhoto = (NetworkImageView) v.findViewById(R.id.iv_photo);
            ibSubscribe = (ImageButton) v.findViewById(R.id.ib_feed_subscribe);

            // Square the background image dynamically
            // SOURCE: http://stackoverflow.com/questions/9798392/imageview-have-height-match-width
            ivPhoto.post(new Runnable() {
                @Override
                public void run() {
                    android.view.ViewGroup.LayoutParams mParams;
                    mParams = ivPhoto.getLayoutParams();
                    mParams.height = ivPhoto.getWidth();
                    ivPhoto.setLayoutParams(mParams);
                    ivPhoto.postInvalidate();
                }
            });


        }
    }


}
