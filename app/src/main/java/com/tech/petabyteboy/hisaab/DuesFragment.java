package com.tech.petabyteboy.hisaab;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.petabyteboy.hisaab.Adapters.DuesViewAdapter;
import com.tech.petabyteboy.hisaab.Models.DuesDataObject;
import com.tech.petabyteboy.hisaab.Models.UserDuesModel;

import java.util.ArrayList;
import java.util.Collection;

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

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference duesDatabaseRef;

    private String TAG = "DuesFragment";
    private String UserID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dues, null);

        SharedPreferences UserDetail = this.getActivity().getSharedPreferences(RegisterActivity.PREF_NAME, Context.MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + UserDetail.getString("phone", null));
        UserID = UserDetail.getString("phone", null);

        firebaseDatabase = FirebaseDatabase.getInstance();
        duesDatabaseRef = firebaseDatabase.getReference().child("Dues");

        txtMessage = (TextView) view.findViewById(R.id.txtDuesMsg);

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
                Toast.makeText(getContext(), "Clicked on Item " + position, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, " Clicked on Item " + position);
                startActivity(new Intent(getContext(), FriendProfileActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), AddDuesActivity.class);
        intent.putExtra("from", "home");
        startActivity(intent);

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
