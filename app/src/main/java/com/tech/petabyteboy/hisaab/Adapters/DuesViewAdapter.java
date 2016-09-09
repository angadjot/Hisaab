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
import com.tech.petabyteboy.hisaab.Models.DuesModel;
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
    private ArrayList<DuesModel> duesModelArrayList;
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

    public DuesViewAdapter(ArrayList<DuesModel> duesModelArrayList, Context context) {
        this.duesModelArrayList = duesModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_dues_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int index = 0;

        SharedPreferences UserDetail = context.getSharedPreferences(RegisterActivity.PREF_NAME, Context.MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + UserDetail.getString("phone", null));
        String UserID = UserDetail.getString("phone", null);

        DuesModel duesModel = duesModelArrayList.get(position);

        holder.due_name.setText(duesModel.getDueName());

        String strdueImage = duesModel.getDueImage();

        if (strdueImage.isEmpty() || strdueImage.equalsIgnoreCase("")) {
            Uri uri = new Uri.Builder().scheme("res") // "res"
                    .path(String.valueOf(R.drawable.icon_group)).build();
            holder.due_image.setImageURI(uri);
        } else {
            holder.due_image.setImageURI(strdueImage);
        }

        String strUserID = duesModel.getSharedUserID();
        List<String> UserIDList = Arrays.asList(strUserID.split(","));

        Log.e(TAG, "UserID List Size : " + UserIDList.size());

        for (int i = 0; i < UserIDList.size(); i++) {
            if (UserIDList.get(i).equalsIgnoreCase(UserID)) {
                index = i;
                Log.e(TAG, "Index of User : " + index);
                break;
            }
        }

        String strSharedAmt = duesModel.getUserSharedAmt();

        if (strSharedAmt.isEmpty() || strSharedAmt.equalsIgnoreCase("")) {
            holder.due_status.setText("You Get");
            holder.due_amount.setText("\u20B9 0.00");
        } else {
            List<String> SharedAmtList = Arrays.asList(strSharedAmt.split(","));
            Float amt = Float.parseFloat(SharedAmtList.get(index));
            Log.e(TAG, "Shared Amount : " + amt);

            if (amt <= 0) {
                holder.due_status.setText("You Get");
            } else if (amt > 0)
                holder.due_status.setText("You Pay");

            holder.due_amount.setText("\u20B9 " + SharedAmtList.get(index).replace("-", ""));
        }

    }

    @Override
    public int getItemCount() {
        return duesModelArrayList.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View view);
    }

}
