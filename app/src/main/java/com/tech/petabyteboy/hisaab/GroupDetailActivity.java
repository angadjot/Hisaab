package com.tech.petabyteboy.hisaab;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tech.petabyteboy.hisaab.Global.NetworkUtils;
import com.tech.petabyteboy.hisaab.Models.DuesModel;
import com.tech.petabyteboy.hisaab.Models.GroupModel;

import java.util.Arrays;
import java.util.List;

public class GroupDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static GroupModel groupModel;
    private static String TAG = "GroupDetailActivity";
    private FloatingActionButton add_group_due;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_group_detail);
        setSupportActionBar(toolbar);
        setTitle(R.string.group_detail_title);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int position = getIntent().getExtras().getInt("position");
        Log.e(TAG, "Position : " + position);

        groupModel = GroupsFragment.groupModelArrayList.get(position);

        SimpleDraweeView imageGroupDetail = (SimpleDraweeView) findViewById(R.id.imageGroupDetail);
        imageGroupDetail.setImageURI(groupModel.getGroupImage());

        Button btnSummary = (Button) findViewById(R.id.btnSummary);
        Button btnEdit = (Button) findViewById(R.id.btnEdit);
        btnSummary.setOnClickListener(this);
        btnEdit.setOnClickListener(this);


        TextView GroupDetailName = (TextView) findViewById(R.id.GroupDetailName);
        GroupDetailName.setText(groupModel.getGroupName());

        TextView groupMember = (TextView) findViewById(R.id.groupMember);

        String strMembers = groupModel.getGroupMemberName();
        StringBuilder sb_name = new StringBuilder();
        List<String> GroupNameList = Arrays.asList(strMembers.split(","));

        Log.e(TAG, "Group Name List Size : " + GroupNameList.size());

        int listSize;

        if (GroupNameList.size() < 3) {
            listSize = GroupNameList.size();
        } else
            listSize = 3;

        for (int i = 0; i < listSize; i++) {
            if (GroupNameList.get(i).equalsIgnoreCase(MainActivity.User.getUsername())) {
                sb_name.append("You");
            } else {
                sb_name.append(GroupNameList.get(i));
            }
            if (i != listSize - 1) {
                sb_name.append(" , ");
            }
        }

        if (GroupNameList.size() > 3) {
            sb_name.append(" & " + String.valueOf(GroupNameList.size() - 3) + " more");
        }

        groupMember.setText("With: " + String.valueOf(sb_name));

        ListView listGroupDetail = (ListView) findViewById(R.id.listGroupDetail);

        add_group_due = (FloatingActionButton) findViewById(R.id.fab);
        add_group_due.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSummary:
                break;

            case R.id.btnEdit:
                break;

            case R.id.fab:
                Intent intent = new Intent(getApplicationContext(), AddGroupDuesActivity.class);
                intent.putExtra(AddGroupDuesActivity.dueName_key, groupModel.getGroupName());
                intent.putExtra(AddGroupDuesActivity.dueImage_key, groupModel.getGroupImage());
                intent.putExtra(AddGroupDuesActivity.comment_key, groupModel.getGroupComment());
                startActivity(intent);
                break;

        }
    }
}
