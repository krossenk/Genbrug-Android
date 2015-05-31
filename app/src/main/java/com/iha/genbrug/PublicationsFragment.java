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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import webservice.Publication;
import webservice.Subscription;
import webservice.User;
import webservice.getPublicationSubscriptionsResponse;


/**
 * A simple {@link Fragment} subclass.
 */
public class PublicationsFragment extends Fragment{

    private RecyclerView pubRecyclerView;
    private RecyclerView.Adapter pubAdapter;
    private RecyclerView.LayoutManager pubLayoutManager;
    private GlobalSettings  globalSettings =GlobalSettings.getInstance();
    long  userId;
    private ServerService serverService;
    User user = globalSettings.getUserFromPref();
    long publicationUserId;
    Activity parentActivity = getActivity();
    private getPublicationSubscriptionsResponse publicationSubscriptionsResponseList;
    private AmountMessageReceiver  receiver;
    int amount;
    long publicationId;
    ArrayList<Long> amountList = new ArrayList<>();




    public PublicationsFragment() {
        // Required empty public constructor
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;

            //User user = globalSettings.getUserFromPref();
            userId = user.id;

            if(FeedFragment.responseList != null)
            {

                for (Publication pub : FeedFragment.responseList)
                {
                    publicationUserId = pub.userId.id;
                    if(publicationUserId == userId)
                    {
                       /* itemId = pub.id;*/
                        amountList.add(pub.id);
                    }

                }


                for(long pubId : amountList)
                {
                    serverService = binder.getService();
                    serverService.startGetPublicationSubscriptions(pubId);
                }

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity = getActivity();
        Intent intent = new Intent(parentActivity, ServerService.class);

        parentActivity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        receiver = new AmountMessageReceiver();
        IntentFilter intentFilter = new IntentFilter(ServerService.ALL_PUBLICATIONSUBSRIPTIONS_RESULT);
        parentActivity.registerReceiver(receiver, intentFilter);

        pubRecyclerView = (RecyclerView) getView().findViewById(R.id.rcview_pub);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        pubRecyclerView.setHasFixedSize(true);

        pubLayoutManager = new LinearLayoutManager(getActivity());
        pubRecyclerView.setLayoutManager(pubLayoutManager);

        User user = globalSettings.getUserFromPref();
        userId = user.id;
        final ArrayList<GiveItem> GiveList = new ArrayList<>();

        if(FeedFragment.responseList != null) {
            for (Publication pub : FeedFragment.responseList) {
                amount = 0;
                publicationUserId = pub.userId.id;
                if(publicationUserId == userId) {
                    if(publicationSubscriptionsResponseList != null) {
                       for (Subscription sub : publicationSubscriptionsResponseList) {
                           publicationId = sub.publicationId.id;

                           for (long pubId : amountList) {
                               if (sub.publicationId.id == pubId) {
                                   amount++;
                               }
                           }
                       }
                    }
                    GiveItem giveItem = new GiveItem(pub.title,pub.description,pub.imageURL,pub.id,amount);
                    GiveList.add(giveItem);
                }
            }
        }

        // specify an adapter (see also next example)
        pubAdapter = new PubAdapter(GiveList);
        pubRecyclerView.setAdapter(pubAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publications, container, false);
    }

    private class AmountMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().compareTo(ServerService.ALL_PUBLICATIONSUBSRIPTIONS_RESULT) == 0) {
                final ArrayList<GiveItem> GiveList = new ArrayList<>();
                if(FeedFragment.responseList != null) {
                    for (Publication pub : FeedFragment.responseList) {
                        amount = 0;
                        publicationUserId = pub.userId.id;
                        if(publicationUserId == userId)
                        {
                            publicationSubscriptionsResponseList = serverService.getPublicationSubscriptions();
                            for(Subscription sub: publicationSubscriptionsResponseList)
                            {
                                publicationId = sub.publicationId.id;
                                for(long pubId : amountList)
                                {
                                    if(sub.publicationId.id == pubId)
                                    {
                                        amount ++;
                                    }
                                }

                            }
                            //amount = publicationSubscriptionsResponseList.size();
                            GiveItem giveItem = new GiveItem(pub.title,pub.description,pub.imageURL,pub.id,amount);
                            GiveList.add(giveItem);
                        }
                    }
                }
            }
        }
    }
}
