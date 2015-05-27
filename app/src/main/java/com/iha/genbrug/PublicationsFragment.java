package com.iha.genbrug;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import webservice.Publication;
import webservice.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class PublicationsFragment extends Fragment{

    private RecyclerView pubRecyclerView;
    private RecyclerView.Adapter pubAdapter;
    private RecyclerView.LayoutManager pubLayoutManager;
    private GlobalSettings  globalSettings =GlobalSettings.getInstance();
    long  userId;
    long publicationUserId;


    public PublicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pubRecyclerView = (RecyclerView) getView().findViewById(R.id.rcview_pub);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        pubRecyclerView.setHasFixedSize(true);

        pubLayoutManager = new LinearLayoutManager(getActivity());
        pubRecyclerView.setLayoutManager(pubLayoutManager);

        User user = globalSettings.getUserFromPref();
        userId = user.id;
        final ArrayList<GiveItem> GiveList = new ArrayList<>();

        if(FeedFragment.responseList != null)
        {


            for (Publication pub : FeedFragment.responseList)
            {
                publicationUserId = pub.userId.id;
                if(publicationUserId == userId)
                {
                    GiveItem giveItem = new GiveItem(pub.title,pub.description,pub.imageURL,pub.id);

                    GiveList.add(giveItem);
                }
            }

        }

        // specify an adapter (see also next example)
        pubAdapter = new PubAdapter(GiveList);
        pubRecyclerView.setAdapter(pubAdapter);



      /*  for(int i = 0; i < 3; i++){

            GiveList.add(new GiveItem("Genbrug #" + (i+1), "Genbrug #" + (i+1) + " is an interesting item",null,(i+1)));
        }*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publications, container, false);
    }


}
