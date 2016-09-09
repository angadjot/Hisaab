package com.tech.petabyteboy.hisaab.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tech.petabyteboy.hisaab.MainActivity;
import com.tech.petabyteboy.hisaab.Models.GroupModel;
import com.tech.petabyteboy.hisaab.R;
import com.tech.petabyteboy.hisaab.RegisterActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by petabyteboy on 13/08/16.
 */
public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.ViewHolder> {

    private static String TAG = "GroupsViewAdapter";
    private ArrayList<GroupModel> groupModelArrayList;
    private Context context;
    private static GroupClickListener groupClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView imageGroup;
        TextView GroupName;
        TextView GroupMembers;

        public ViewHolder(View itemView) {
            super(itemView);

            imageGroup = (SimpleDraweeView) itemView.findViewById(R.id.imageGroup);
            GroupName = (TextView) itemView.findViewById(R.id.groupName);
            GroupMembers = (TextView) itemView.findViewById(R.id.txtMembers);
            Log.e(TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            groupClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(GroupClickListener groupClickListener) {
        this.groupClickListener = groupClickListener;
    }

    public GroupViewAdapter(ArrayList<GroupModel> groupModelArrayList, Context context) {
        this.groupModelArrayList = groupModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_group_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int Listsize;

        GroupModel groupModel = groupModelArrayList.get(position);

        Log.e(TAG, "Group Name : " + groupModel.getGroupName());
        holder.GroupName.setText(groupModel.getGroupName());

        String strGroupImage = groupModel.getGroupImage();

        if (strGroupImage.isEmpty() || strGroupImage.equalsIgnoreCase("")) {
            Uri uri = new Uri.Builder().scheme("res") // "res"
                    .path(String.valueOf(R.drawable.icon_group)).build();
            holder.imageGroup.setImageURI(uri);
        } else {
            holder.imageGroup.setImageURI(strGroupImage);
        }

        String strMembers = groupModel.getGroupMemberName();
        StringBuilder sb_name = new StringBuilder();
        List<String> GroupNameList = Arrays.asList(strMembers.split(","));

        Log.e(TAG, "Group Name List Size : " + GroupNameList.size());

        if (GroupNameList.size() < 3) {
            Listsize = GroupNameList.size();
        } else
            Listsize = 3;

        for (int i = 0; i < Listsize; i++) {
            if (GroupNameList.get(i).equalsIgnoreCase(MainActivity.User.getUsername())) {
                sb_name.append("You");
            } else {
                sb_name.append(GroupNameList.get(i));
            }
            if (i != Listsize - 1) {
                sb_name.append(" , ");
            }
        }

        if (GroupNameList.size() > 3) {
            sb_name.append(" & " + String.valueOf(GroupNameList.size() - 3) + " more");
        }

        holder.GroupMembers.setText("With: " + String.valueOf(sb_name));

    }

    @Override
    public int getItemCount() {
        return groupModelArrayList.size();
    }

    public interface GroupClickListener {
        void onItemClick(int position, View view);
    }

}
