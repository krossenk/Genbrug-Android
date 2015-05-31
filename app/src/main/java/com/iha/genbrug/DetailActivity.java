package com.iha.genbrug;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import webservice.Publication;
import webservice.Subscription;
import webservice.User;



public class DetailActivity extends Activity {

    private long publicationId;
    private ImageLoader imgLoader;
    private ServerService serverServiceSubscribe;
    private GlobalSettings  globalSettings =GlobalSettings.getInstance();
    long  userId;
    boolean itemSubscribedCheck = false;
    String header;
    String imageUrl;
    String desc;
    List<Subscription> subList;
    private Publication getPublicationResponse;
    private DetailsMessagesReceiver  receiver;
    TextView headerTextView;
    TextView descTextView;
    NetworkImageView imageView;
    ImageButton subscribeBtn;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            serverServiceSubscribe = binder.getService();
            serverServiceSubscribe.startGetUserSubscriptions(userId);
            serverServiceSubscribe.startGetPublication(publicationId);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = new Intent(this,ServerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        receiver = new DetailsMessagesReceiver();
        IntentFilter intentFilter = new IntentFilter(ServerService.PUBLICATIONRESULT);
        registerReceiver(receiver, intentFilter);
        User user = globalSettings.getUserFromPref();
        userId = user.id;
        imgLoader = VolleySingleton.getInstance().getImageLoader();
        imageView = (NetworkImageView) findViewById(R.id.itemPhoto);
        headerTextView = (TextView) findViewById(R.id.headlineTextView);
        descTextView = (TextView) findViewById(R.id.descTextView);
        subList = new ArrayList<>();
        subscribeBtn = (ImageButton) findViewById(R.id.detail_subscribe);

        subList = SubscriptionsFragment.userSubscriptionsResponseList;
        Bundle bundle = getIntent().getExtras();
        publicationId = (bundle.getLong("itemId"));

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
                    subscribeBtn.setImageResource(R.drawable.ic_sub_selected);

                }
                else {
                    subscribeBtn.setImageResource(R.drawable.ic_sub_not_selected);
                }
            }

            else {
                itemSubscribedCheck = false;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        unregisterReceiver(receiver);

    }

    public void subscribe (View v){


        if(itemSubscribedCheck)
        {
            Toast.makeText(this, header + " is already subscribed", Toast.LENGTH_SHORT).show();

        }

        else
        {
            serverServiceSubscribe.createSubscription(userId, publicationId);
            Toast.makeText(this, header+ " is subscribed", Toast.LENGTH_SHORT).show();
            subscribeBtn.setImageResource(R.drawable.ic_sub_selected);
            itemSubscribedCheck = true;
        }

    }

    public void callMainActivity() {
        //Intent intent = new Intent(this,MainActivity.class);
        //this.startActivity(intent);
        //finish();
    }


    public void callFeedActivity(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private class DetailsMessagesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().compareTo(ServerService.PUBLICATIONRESULT) == 0) {

                getPublicationResponse = serverServiceSubscribe.getReturnedPublication();

                if(getPublicationResponse != null)
                {

                    header = getPublicationResponse.title;
                    desc = getPublicationResponse.description;
                    imageUrl = getPublicationResponse.imageURL;
                    headerTextView.setText(header);
                    descTextView.setText(desc);
                    imageView.setImageUrl(imageUrl,imgLoader);
                }

            }
        }

    }


}
