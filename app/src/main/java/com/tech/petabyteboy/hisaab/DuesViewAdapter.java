package com.tech.petabyteboy.hisaab;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by petabyteboy on 15/07/16.
 */
public class DuesViewAdapter extends RecyclerView.Adapter<DuesViewAdapter.DataObjectHolder> {

    private static String LOG_TAG = "DuesViewAdapter";
    private ArrayList<DuesDataObject> mDataset;
    private static MyClickListener myClickListener;

    @Override
    public DuesViewAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_dues,parent,false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DuesViewAdapter.DataObjectHolder holder, int position) {

        holder.due_status.setText(mDataset.get(position).getmText1());
        holder.due_amount.setText(mDataset.get(position).getmText2());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SimpleDraweeView image_profile;

        private TextView due_status;
        private TextView due_amount;

        public DataObjectHolder(View itemView) {
            super(itemView);

            image_profile = (SimpleDraweeView) itemView.findViewById(R.id.image_profile);
            due_status = (TextView) itemView.findViewById(R.id.due_status);
            due_amount = (TextView) itemView.findViewById(R.id.due_amount);

            Log.i(LOG_TAG, "Adding Listener");

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getAdapterPosition(),view);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }

    public DuesViewAdapter(ArrayList<DuesDataObject> myDataset){
        mDataset = myDataset;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public void addItem(DuesDataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }
}
