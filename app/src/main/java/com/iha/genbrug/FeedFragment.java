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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import webservice.Publication;
import webservice.getAllPublicationsResponse;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    private RecyclerView fRecyclerView;
    private RecyclerView.Adapter fAdapter;
    private RecyclerView.LayoutManager fLayoutManager;
    private ServerService serverService;
    private PublicationMessagesReceiver receiver;
    public static getAllPublicationsResponse responseList;
    private SwipeRefreshLayout swipeContainer;
    private int scrollPosition = 0;
    private  Activity parentAct;
    private boolean fragmentValidation =false;
    private Intent intent;
    private IntentFilter intentFilter;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            serverService = binder.getService();
            serverService.startGetAllPublications();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentAct= getActivity();
        intent = new Intent(parentAct, ServerService.class);

        //parentAct.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        receiver = new PublicationMessagesReceiver();
        intentFilter = new IntentFilter(ServerService.ALL_PUBLICATIONS_RESULT);

       // parentAct.registerReceiver(receiver, intentFilter);
       // parentAct.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        fragmentValidation = true;


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fRecyclerView = (RecyclerView) getView().findViewById(R.id.rcview_feed);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        fRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        fLayoutManager = new LinearLayoutManager(getActivity());
        fRecyclerView.setLayoutManager(fLayoutManager);

        scrollPosition = FeedAdapter.getPostion();
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                serverService.startGetAllPublications();
                scrollPosition = 0;
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_purple);


       // parentAct.registerReceiver(receiver, intentFilter);

        if(responseList != null) {
            ArrayList<GenbrugItem> list = new ArrayList<>();

            for (Publication pub : responseList) {
                GenbrugItem item = new GenbrugItem(pub.title, pub.description, pub.imageURL ,pub.id, pub.addressid);
                item.setPickupStartTime(pub.pickupStartime);
                item.setPickupEndTime(pub.pickupEndtime);
                list.add(item);
            }

            fAdapter = new FeedAdapter(list,this.getActivity());
            fRecyclerView.setAdapter(fAdapter);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if(isVisibleToUser&&fragmentValidation)
        {
            parentAct.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            parentAct.registerReceiver(receiver, intentFilter);

            fragmentValidation = false;

        } else if (isVisibleToUser == false && fragmentValidation ==true ) {
            parentAct.unbindService(serviceConnection);
            parentAct.unregisterReceiver(receiver);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    private class PublicationMessagesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().compareTo(ServerService.ALL_PUBLICATIONS_RESULT)==0) {
                responseList = serverService.getAllPublications();

                ArrayList<GenbrugItem> list = new ArrayList<>();

                for (Publication pub : responseList) {
                    GenbrugItem item = new GenbrugItem(pub.title, pub.description, pub.imageURL ,pub.id, pub.addressid);
                    item.setPickupStartTime(pub.pickupStartime);
                    item.setPickupEndTime(pub.pickupEndtime);
                    list.add(item);
                }

                fAdapter = new FeedAdapter(list, getActivity());
                fLayoutManager.scrollToPosition(scrollPosition);
                fRecyclerView.setAdapter(fAdapter);
                swipeContainer.setRefreshing(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        parentAct.unbindService(serviceConnection);
        parentAct.unregisterReceiver(receiver);
    }
}

