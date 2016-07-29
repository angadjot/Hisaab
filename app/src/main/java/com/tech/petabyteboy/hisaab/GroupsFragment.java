package com.tech.petabyteboy.hisaab;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class GroupsFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton add_group;
    private ListView listGroup;
    private TextView txtGroupListMsg;

    public static int REQUEST_CODE_ADDGROUP = 20;

    Boolean is_group_list_empty = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, null);

        add_group = (FloatingActionButton) view.findViewById(R.id.fab);
        add_group.setOnClickListener(this);

        listGroup = (ListView) view.findViewById(R.id.listGroup);

        txtGroupListMsg = (TextView) view.findViewById(R.id.txtGroupListMsg);
        getGroupList();

        return view;
    }

    public void getGroupList() {

        if (is_group_list_empty) {

            listGroup.setVisibility(View.GONE);
            txtGroupListMsg.setVisibility(View.VISIBLE);
            txtGroupListMsg.setText(R.string.no_groups);
            
        }
        else {
            txtGroupListMsg.setVisibility(View.GONE);
            listGroup.setVisibility(View.VISIBLE);
            //listGroup.setAdapter();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADDGROUP && AddGroupActivity.RESPONSE_CODE_ADDGROUP == 1) {
            getGroupList();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), AddGroupActivity.class);
        startActivityForResult(intent,REQUEST_CODE_ADDGROUP);
    }
}
