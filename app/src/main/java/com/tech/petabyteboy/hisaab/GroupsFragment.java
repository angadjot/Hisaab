package com.tech.petabyteboy.hisaab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.petabyteboy.hisaab.Adapters.DuesViewAdapter;
import com.tech.petabyteboy.hisaab.Adapters.GroupViewAdapter;
import com.tech.petabyteboy.hisaab.Models.DuesModel;
import com.tech.petabyteboy.hisaab.Models.GroupModel;

import java.util.ArrayList;

public class GroupsFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton add_group;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String TAG = "GroupsFragment";

    private TextView txtGroupListMsg;
    private ProgressDialog progressDialog;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference groupListDataRef;
    private DatabaseReference groupDataRef;

    private String UserID;
    private Boolean flag_Groups_exists = false;

    public static ArrayList<GroupModel> groupModelArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groups, null);

        SharedPreferences UserDetail = this.getActivity().getSharedPreferences(RegisterActivity.PREF_NAME, Context.MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + UserDetail.getString("phone", null));
        UserID = UserDetail.getString("phone", null);

        firebaseDatabase = FirebaseDatabase.getInstance();
        groupListDataRef = firebaseDatabase.getReference().child("GroupList").child(UserID);
        groupDataRef = firebaseDatabase.getReference().child("Groups");

        groupModelArrayList = new ArrayList<>();

        add_group = (FloatingActionButton) view.findViewById(R.id.fab);
        add_group.setOnClickListener(this);

        txtGroupListMsg = (TextView) view.findViewById(R.id.txtGroupListMsg);

        progressDialog = new ProgressDialog(getContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.GroupCardList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GroupViewAdapter(groupModelArrayList, getContext());
        mRecyclerView.setAdapter(mAdapter);

        groupListDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                    flag_Groups_exists = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupListDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, "DATA Checked");

                if (flag_Groups_exists) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    txtGroupListMsg.setVisibility(View.GONE);
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

        groupDataRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GroupModel groupModel = dataSnapshot.getValue(GroupModel.class);
                groupModelArrayList.add(groupModel);
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
        Intent intent = new Intent(getContext(), AddGroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((GroupViewAdapter) mAdapter).setOnItemClickListener(new GroupViewAdapter.GroupClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.e(TAG, "Clicked on Item " + position);
                Intent intent = new Intent(getContext(), GroupDetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
}
