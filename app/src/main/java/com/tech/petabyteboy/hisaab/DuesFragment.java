package com.tech.petabyteboy.hisaab;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class DuesFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton add_dues;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "DuesFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dues,null);

        add_dues = (FloatingActionButton) view.findViewById(R.id.fab);
        add_dues.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DuesViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DuesViewAdapter) mAdapter).setOnItemClickListener(new DuesViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Toast.makeText(getContext(),"Clicked on Item "+position,Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, " Clicked on Item " + position);
                startActivity(new Intent(getContext(),FriendProfileActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(),AddDuesActivity.class);
        startActivity(intent);

    }

    private ArrayList<DuesDataObject> getDataSet() {
        ArrayList results = new ArrayList<DuesDataObject>();
        for (int index = 0; index < 20; index++) {
            DuesDataObject obj = new DuesDataObject("Some Primary Text " + index,
                    "Secondary " + index);
            results.add(index, obj);
        }
        return results;
    }
}
