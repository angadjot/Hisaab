package com.tech.petabyteboy.hisaab;


import android.app.ProgressDialog;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.petabyteboy.hisaab.Adapters.DuesViewAdapter;
import com.tech.petabyteboy.hisaab.Models.DuesModel;

import java.util.ArrayList;

public class DuesFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton add_dues;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String TAG = "DuesFragment";

    private TextView txtMessage;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference duesListDataRef;
    private DatabaseReference duesDataRef;

    private String UserID;
    private Boolean flag_Dues_exists = false;

    private ProgressDialog progressDialog;

    public static ArrayList<DuesModel> duesModelArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dues, null);

        SharedPreferences UserDetail = this.getActivity().getSharedPreferences(RegisterActivity.PREF_NAME, Context.MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + UserDetail.getString("phone", null));
        UserID = UserDetail.getString("phone", null);

        firebaseDatabase = FirebaseDatabase.getInstance();
        duesListDataRef = firebaseDatabase.getReference().child("DuesList").child(UserID);
        duesDataRef = firebaseDatabase.getReference().child("Dues");
        duesModelArrayList = new ArrayList<>();

        add_dues = (FloatingActionButton) view.findViewById(R.id.fab);
        add_dues.setOnClickListener(this);

        txtMessage = (TextView) view.findViewById(R.id.txtDuesMsg);

        progressDialog = new ProgressDialog(getContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DuesViewAdapter(duesModelArrayList, getContext());
        mRecyclerView.setAdapter(mAdapter);

        duesListDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                    flag_Dues_exists = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.e(TAG,"Dues Exists : "+flag_Dues_exists);

        duesListDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, "DATA Checked");

                if (flag_Dues_exists) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    txtMessage.setVisibility(View.GONE);
                    progressDialog.setMessage("Fetching Data...\nPlease Wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setProgress(0);
                    progressDialog.show();
                    FetchData(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void FetchData(String key) {

        duesDataRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DuesModel duesModel = dataSnapshot.getValue(DuesModel.class);
                duesModelArrayList.add(duesModel);
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), AddDuesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((DuesViewAdapter) mAdapter).setOnItemClickListener(new DuesViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.e(TAG, "Clicked on Item " + position);
                Intent intent = new Intent(getContext(), DuesDetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

    }
}
