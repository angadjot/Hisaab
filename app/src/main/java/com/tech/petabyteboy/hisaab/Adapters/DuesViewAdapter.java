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
import com.tech.petabyteboy.hisaab.Models.UserDuesModel;
import com.tech.petabyteboy.hisaab.R;
import com.tech.petabyteboy.hisaab.RegisterActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by petabyteboy on 29/07/16.
 */
public class DuesViewAdapter extends RecyclerView.Adapter<DuesViewAdapter.ViewHolder> {

    private static String TAG = "DuesViewAdapter";
    private ArrayList<UserDuesModel> userDuesModelArrayList;
    private Context context;
    private static MyClickListener myClickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView due_image;
        TextView due_name;
        TextView due_status;
        TextView due_amount;

        public ViewHolder(View view) {
            super(view);

            due_image = (SimpleDraweeView) view.findViewById(R.id.dues_image);
            due_name = (TextView) view.findViewById(R.id.due_name);
            due_status = (TextView) view.findViewById(R.id.due_status);
            due_amount = (TextView) view.findViewById(R.id.due_amount);
            Log.e(TAG, "Adding Listener");
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public DuesViewAdapter(ArrayList<UserDuesModel> userDuesModelArrayList, Context context) {
        this.userDuesModelArrayList = userDuesModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_view_dues, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int index = 0;

        SharedPreferences UserDetail = context.getSharedPreferences(RegisterActivity.PREF_NAME, Context.MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + UserDetail.getString("phone", null));
        String UserID = UserDetail.getString("phone", null);

        UserDuesModel userDuesModel = userDuesModelArrayList.get(position);

        holder.due_name.setText("Dues " + position);

        String strdueImage = userDuesModel.getDue_image();

        if (strdueImage.isEmpty() || strdueImage.equalsIgnoreCase("")) {
            Uri uri = new Uri.Builder().scheme("res") // "res"
                    .path(String.valueOf(R.drawable.icon_placeholder)).build();
            holder.due_image.setImageURI(uri);
        } else {
            holder.due_image.setImageURI(strdueImage);
        }

        String strUserID = userDuesModel.getShared_User_id();
        List<String> UserIDList = Arrays.asList(strUserID.split(","));

        Log.e(TAG, "UserID List Size : " + UserIDList.size());

        for (int i = 0; i < UserIDList.size(); i++) {
            if (UserIDList.get(i).equalsIgnoreCase(UserID)) {
                index = i;
                Log.e(TAG, "Index of User : " + index);
                break;
            }
        }

        String strSharedAmt = userDuesModel.getUser_Shared_Amt();

        if (strSharedAmt.isEmpty() || strSharedAmt.equalsIgnoreCase("")) {
            holder.due_status.setText("You Get");
            holder.due_amount.setText("\u20B9 0.00");
        } else {
            List<String> SharedAmtList = Arrays.asList(strSharedAmt.split(","));
            Float amt = Float.parseFloat(SharedAmtList.get(index));
            Log.e(TAG, "Shared Amount : " + amt);

            if (amt < 0) {
                holder.due_status.setText("You Pay");
            } else if (amt >= 0)
                holder.due_status.setText("You Get");

            holder.due_amount.setText("\u20B9 " + SharedAmtList.get(index).replace("-", ""));
        }

    }

    @Override
    public int getItemCount() {
        return userDuesModelArrayList.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View view);
    }

}
