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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DuesFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton add_dues;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "DuesFragment";

    public float intAllAmount = 0.0f;
    public float intIGet = 0.0f;
    public float intIPay = 0.0f;

    private TextView txtMessage;

    //private ArrayList<HomeModel> AllListDues;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dues,null);

        txtMessage = (TextView) view.findViewById(R.id.txtDuesMsg);

        add_dues = (FloatingActionButton) view.findViewById(R.id.fab);
        add_dues.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DuesViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        getDuesList();

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
        intent.putExtra("from", "home");
        startActivity(intent);

    }

    public void getDuesList() {

        /*try {
            AllListDues = new ArrayList<>();
            RealmResults<HomeModel> query = this.realm.where(HomeModel.class).findAll();
            intIGet = 0.0f;
            intIPay = 0.0f;
            Iterator i$ = query.iterator();
            while (i$.hasNext()) {
                HomeModel p = (HomeModel) i$.next();
                this.AllListExpense.add(p);
                if (p.getPay_type().equalsIgnoreCase("iGET")) {
                    this.intIGet = Float.valueOf(p.getAmount()).floatValue() + this.intIGet;
                } else if (p.getPay_type().equalsIgnoreCase("iPAY")) {
                    this.intIPay = Float.valueOf(p.getAmount()).floatValue() + this.intIPay;
                }
            }
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
        System.out.println("AllData" + this.AllListExpense);
        if (this.AllListExpense.size() > 0) {
            HomeListAdapter_NEw adapter = new HomeListAdapter_NEw(getActivity(), this.AllListExpense);
            this.listHome.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            this.listHome.setVisibility(0);
            setCalculateValue();
            return;
        }
        this.txtMessage.setVisibility(0);
        this.txtMessage.setText("No Dues Yet!!");
        */
    }

    public void setCalculateValue() {
        float totalAmount = this.intIGet - this.intIPay;
        if (totalAmount > 0.0f) {
            MainActivity.txtAmount.setText("You Get \u20b9 " + String.valueOf(totalAmount));
            return;
        }
        String amt = Float.valueOf(totalAmount).toString();
        if (amt.equalsIgnoreCase("0.00")) {
            MainActivity.txtAmount.setText("You Get \u20b9 " + amt);
        } else {
            MainActivity.txtAmount.setText("You Pay \u20b9 " + amt);
        }
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
