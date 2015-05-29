package com.iha.genbrug;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import webservice.Publication;
import webservice.Subscription;
import webservice.getPublicationSubscriptionsResponse;


public class TakersActivity extends Activity {

    private RecyclerView takersRecyclerView;
    private RecyclerView.Adapter takersAdapter;
    private RecyclerView.LayoutManager takersLayoutManager;
    private ServerService serverService;
    private TakersMessagesReceiver  receiver;
    private long itemId;
    private getPublicationSubscriptionsResponse publicationSubscriptionsResponseList;
    String itemDate;
    String pickTime;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            Bundle bundle = getIntent().getExtras();
            itemId =(bundle.getLong("itemId"));
            //User user = globalSettings.getUserFromPref();
            //userId = user.id;
            serverService = binder.getService();
            serverService.startGetPublicationSubscriptions(itemId);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takers);

        Intent intent = new Intent(this, ServerService.class);

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        receiver = new TakersMessagesReceiver();
        IntentFilter intentFilter = new IntentFilter(ServerService.ALL_PUBLICATIONSUBSRIPTIONS_RESULT);
        registerReceiver(receiver, intentFilter);

        takersRecyclerView = (RecyclerView) findViewById(R.id.rcview_takers);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        takersRecyclerView.setHasFixedSize(true);

        takersLayoutManager = new LinearLayoutManager(this);
        takersRecyclerView.setLayoutManager(takersLayoutManager);

        if (publicationSubscriptionsResponseList != null) {
            ArrayList<TakerItem> list = new ArrayList<>();

            for (Subscription subscription : publicationSubscriptionsResponseList) {
                long pubId = subscription.publicationId.id;

                if(FeedFragment.responseList !=null)
                {
                    for (Publication publication : FeedFragment.responseList) {
                        if (publication.id == itemId && publication.id == pubId) {
                            if(publication.pickupStartime != null)
                            {
                                itemDate =publication.pickupStartime.substring(0,10);
                                pickTime = publication.pickupStartime.substring(11,19);
                            }
                            else {
                                itemDate ="No date!";
                                pickTime ="No time!";
                            }

                            TakerItem item = new TakerItem(itemDate, pickTime, null, publication.id);
                            list.add(item);
                        }

                    }
                }
            }

            // specify an adapter (see also next example)
            takersAdapter = new TakersAdapter(list);
            takersRecyclerView.setAdapter(takersAdapter);

        }
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
                unbindService(serviceConnection);
                unregisterReceiver(receiver);

    }

    private class TakersMessagesReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().compareTo(ServerService.ALL_PUBLICATIONSUBSRIPTIONS_RESULT) == 0) {

                    publicationSubscriptionsResponseList = serverService.getPublicationSubscriptions();

                    if (publicationSubscriptionsResponseList != null) {
                        ArrayList<TakerItem> list = new ArrayList<>();

                        for (Subscription subscription : publicationSubscriptionsResponseList) {
                            long pubId = subscription.userId.id;

                            if(FeedFragment.responseList != null)
                            {


                                for (Publication publication : FeedFragment.responseList) {
                                      if (publication.id == itemId && publication.id == pubId) {
                                            if(publication.pickupStartime != null)
                                             {
                                        itemDate =publication.pickupStartime.substring(0,10);
                                        pickTime = publication.pickupStartime.substring(11,19);
                                             }
                                          else {
                                        itemDate ="No date!";
                                        pickTime ="No time!";
                                        }
                                         TakerItem item = new TakerItem(itemDate, pickTime, null, publication.id);
                                            list.add(item);
                                    }

                                 }
                            }
                        }

                        takersAdapter = new TakersAdapter(list);
                        takersRecyclerView.setAdapter(takersAdapter);
                    }


                }
            }

        }


    public void callMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
        finish();
    }


    public void callMainActivityClick(View view) {
        callMainActivity();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       callMainActivity();
        finish();

    }
}

