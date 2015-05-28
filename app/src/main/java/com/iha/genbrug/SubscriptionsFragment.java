package com.iha.genbrug;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import webservice.Publication;
import webservice.Subscription;
import webservice.User;
import webservice.getAllPublicationsResponse;
import webservice.getUserSubscriptionsResponse;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionsFragment extends Fragment {

    private RecyclerView subRecyclerView;
    private RecyclerView.Adapter subAdapter;
    private RecyclerView.LayoutManager subLayoutManager;
    private ServerService serverService;
    private SubscriptionMessagesReceiver  receiver;
    public static getUserSubscriptionsResponse userSubscriptionsResponseList;

    private GlobalSettings  globalSettings =GlobalSettings.getInstance();
    long  userId;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            User user = globalSettings.getUserFromPref();
            userId = user.id;
            serverService = binder.getService();
            serverService.startGetUserSubscriptions(userId);

                   }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public SubscriptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Activity parentAct = getActivity();

        Intent intent = new Intent(parentAct, ServerService.class);

        parentAct.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        receiver = new SubscriptionMessagesReceiver();
        IntentFilter intentFilter = new IntentFilter(ServerService.ALL_PUBLICATIONS_RESULT);
        parentAct.registerReceiver(receiver, intentFilter);

        subRecyclerView = (RecyclerView) getView().findViewById(R.id.rcview_sub);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        subRecyclerView.setHasFixedSize(true);

        subLayoutManager = new LinearLayoutManager(getActivity());
        subRecyclerView.setLayoutManager(subLayoutManager);


        if(userSubscriptionsResponseList != null)
        {
            ArrayList<TakeItem> list = new ArrayList<>();

            for (Subscription subscription : userSubscriptionsResponseList)
            {
                long publicationId = subscription.publicationId.id;
                for (Publication publication : FeedFragment.responseList)
                {
                    if(publication.id == publicationId)
                    {
                        TakeItem item = new TakeItem(publication.title, publication.description, publication.imageURL ,publication.id);
                        list.add(item);
                    }

                }
            }

            // specify an adapter (see also next example)
            subAdapter = new SubAdapter(list);
            subRecyclerView.setAdapter(subAdapter);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
        getActivity().unregisterReceiver(receiver);

    }


    private class SubscriptionMessagesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().compareTo(ServerService.ALL_PUBLICATIONS_RESULT)==0)
            {
                userSubscriptionsResponseList = serverService.getUserSubscriptions();

                ArrayList<TakeItem> list = new ArrayList<>();
                if(userSubscriptionsResponseList != null)
                {
                    for (Subscription subscription : userSubscriptionsResponseList)
                    {
                        long publicationId = subscription.publicationId.id;
                        for (Publication publication : FeedFragment.responseList)
                        {

                            if(publication.id == publicationId )
                            {
                                TakeItem item = new TakeItem(publication.title, publication.description, publication.imageURL ,publication.id);
                                list.add(item);
                            }
                        }

                    }

                    subAdapter = new SubAdapter(list);
                    subRecyclerView.setAdapter(subAdapter);
                }

            }
        }
    }
}
