package com.iha.genbrug;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionsFragment extends Fragment {

    private RecyclerView subRecyclerView;
    private RecyclerView.Adapter subAdapter;
    private RecyclerView.LayoutManager subLayoutManager;


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

        subRecyclerView = (RecyclerView) getView().findViewById(R.id.rcview_sub);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        subRecyclerView.setHasFixedSize(true);

        subLayoutManager = new LinearLayoutManager(getActivity());
        subRecyclerView.setLayoutManager(subLayoutManager);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String pictureUri =  prefs.getString("ProfileURL", "");
        // Create fake dataset

        final ArrayList<TakeItem> TakeList = new ArrayList<>();

        for(int i = 0; i < 15; i++){

            TakeList.add(new TakeItem("Genbrug #" + (i+1), "Genbrug #" + (i+1) + " is an interesting item",null,(i+1)));
        }

        // specify an adapter (see also next example)
        subAdapter = new SubAdapter(TakeList);
        subRecyclerView.setAdapter(subAdapter);

    }
}
