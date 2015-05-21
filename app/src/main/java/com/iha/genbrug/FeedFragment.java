package com.iha.genbrug;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import webservice.Category;
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
    private getAllPublicationsResponse responseList;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Activity parentAct = getActivity();

        Intent intent = new Intent(parentAct, ServerService.class);

        parentAct.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        receiver = new PublicationMessagesReceiver();
        IntentFilter intentFilter = new IntentFilter(ServerService.ALL_PUBLICATIONS_RESULT);
        parentAct.registerReceiver(receiver, intentFilter);

        fRecyclerView = (RecyclerView) getView().findViewById(R.id.rcview_feed);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        fRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        fLayoutManager = new LinearLayoutManager(getActivity());
        fRecyclerView.setLayoutManager(fLayoutManager);

        // Create fake dataset
        /*
        final ArrayList<GenbrugItem> genbrugList = new ArrayList<GenbrugItem>();
        for(int i = 1; i < 10; i++){

            genbrugList.add(new GenbrugItem("Genbrug #" + (i+1), "Genbrug #" + (i+1) + " is an interesting item", getResources().getDrawable(R.drawable.img+i)));
        }

        // specify an adapter (see also next example)
        fAdapter = new FeedAdapter(genbrugList);
        fRecyclerView.setAdapter(fAdapter);*/


        if(responseList != null)
        {
            ArrayList<GenbrugItem> list = new ArrayList<>();

            for (Publication pub : responseList)
            {
                GenbrugItem item = new GenbrugItem(pub.title, pub.description, null,pub.id);
                list.add(item);
            }

            fAdapter = new FeedAdapter(list);
            fRecyclerView.setAdapter(fAdapter);

  }

        fLayoutManager.scrollToPosition(FeedAdapter.getPostion());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


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

            if (intent.getAction().compareTo(ServerService.ALL_PUBLICATIONS_RESULT)==0)
            {
                responseList = serverService.getAllPublications();

                ArrayList<GenbrugItem> list = new ArrayList<>();

                for (Publication pub : responseList)
                {
                    GenbrugItem item = new GenbrugItem(pub.title, pub.description, null,pub.id);
                    list.add(item);
                }

                fAdapter = new FeedAdapter(list);
                fRecyclerView.setAdapter(fAdapter);
            }
        }
    }
}

