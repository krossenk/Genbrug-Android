package com.iha.genbrug;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class FeedFragment extends Fragment {
    private RecyclerView fRecyclerView;
    private RecyclerView.Adapter fAdapter;
    private RecyclerView.LayoutManager fLayoutManager;

    public FeedFragment() {
        // Required empty public constructor
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

        // Create fake dataset
        final ArrayList<GenbrugItem> genbrugList = new ArrayList<GenbrugItem>();
        for(int i = 1; i < 10; i++){
            genbrugList.add(new GenbrugItem("Genbrug #" + (i+1), "Genbrug #" + (i+1) + " is an interesting item", getResources().getDrawable(R.drawable.img+i)));
        }

        // specify an adapter (see also next example)
        fAdapter = new FeedAdapter(genbrugList);
        fRecyclerView.setAdapter(fAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }


}

