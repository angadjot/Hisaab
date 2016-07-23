package com.tech.petabyteboy.hisaab;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by petabyteboy on 19/07/16.
 */
public class PayeeAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> duesImage;
    ArrayList<String> duesName;
    ArrayList<String> duesNumber;

    private String TAG = "PayeeAdapter";

    public class Holder {
        ImageView friendImage;
        TextView txtContactName;
    }

    public PayeeAdapter(Context context, ArrayList<String> Name, ArrayList<String> Number, ArrayList<String> Image) {
        duesName = new ArrayList<>();
        duesNumber = new ArrayList<>();
        duesImage = new ArrayList<>();
        this.context = context;
        duesName = Name;
        duesNumber = Number;
        duesImage = Image;
    }


    @Override
    public int getCount() {
        return duesNumber.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_overview_contact, null);
            holder = new Holder();

            holder.txtContactName = (TextView) view.findViewById(R.id.txtContactName);
            holder.friendImage = (SimpleDraweeView) view.findViewById(R.id.friendImage);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        try {
            String strName;

            if (duesNumber.get(i).equalsIgnoreCase(AddDuesActivity.UserID)) {
                strName = "You";
            } else {
                strName = DuesSharedWithModel.getName(duesNumber.get(i));
                if (strName.isEmpty()) {
                    strName = duesNumber.get(i);
                }
            }

            holder.txtContactName.setText(strName);

            Log.e(TAG, "\nDues Values " + i + "\n"
                    + "Name : " + duesName.get(i) + "\n"
                    + "Number : " + duesNumber.get(i) + "\n"
                    + "Image : " + duesImage.get(i));

            Log.e(TAG,"Dues Image : "+duesImage.get(i));

            if (duesImage.get(i).toString().isEmpty()) {
                String strImageUri = DuesSharedWithModel.getImage(duesNumber.get(i));

                if (strImageUri == null || strImageUri.isEmpty()) {
                    Uri uri = new Uri.Builder().scheme("res") // "res"
                            .path(String.valueOf(R.drawable.icon_placeholder)).build();
                    holder.friendImage.setImageURI(uri);
                    return view;
                }
                holder.friendImage.setImageURI(Uri.parse(strImageUri));
                return view;


            }
            holder.friendImage.setImageURI(Uri.parse(duesImage.get(i).toString()));
            return view;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
}
