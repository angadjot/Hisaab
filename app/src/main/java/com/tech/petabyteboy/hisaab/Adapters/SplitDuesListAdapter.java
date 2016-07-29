package com.tech.petabyteboy.hisaab.Adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tech.petabyteboy.hisaab.AddDuesActivity;
import com.tech.petabyteboy.hisaab.Global.GlobalVariables;
import com.tech.petabyteboy.hisaab.Interfaces.DataTransferInterface;
import com.tech.petabyteboy.hisaab.MainActivity;
import com.tech.petabyteboy.hisaab.Models.DuesSharedWithModel;
import com.tech.petabyteboy.hisaab.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by petabyteboy on 23/07/16.
 */
public class SplitDuesListAdapter extends BaseAdapter {

    ArrayList<String> contactName;
    ArrayList<String> contactNumber;
    Context context;
    DataTransferInterface dtInterface;
    ArrayList<String> duesAmount;
    HashMap<Integer, View> hashMap;
    ArrayList<String> imageAdd;
    Boolean onedit;

    public class Holder {
        EditText editAmount;
        SimpleDraweeView friendImage;
        TextView txtContactName;
        TextView rupeesymbol;
    }

    public SplitDuesListAdapter(Context context, ArrayList<String> contactName, ArrayList<String> contactNumber, ArrayList<String> imageAdd, ArrayList<String> duesAmount, Boolean is_toggle, DataTransferInterface dtInterface) {

        hashMap = new HashMap<>();
        this.context = context;
        this.imageAdd = imageAdd;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.duesAmount = duesAmount;
        this.dtInterface = dtInterface;
        onedit = is_toggle;
    }

    @Override
    public int getCount() {
        return imageAdd.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (hashMap.containsKey(i))
            return hashMap.get(i);

        String strName;

        Holder holder = new Holder();

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.list_split_amount, null);

        holder.txtContactName = (TextView) view.findViewById(R.id.txtContactNameSplitAdapter);
        holder.editAmount = (EditText) view.findViewById(R.id.editAmountSplitAdapter);
        holder.friendImage = (SimpleDraweeView) view.findViewById(R.id.friendImage);
        holder.rupeesymbol = (TextView) view.findViewById(R.id.rupeesymbol);

        if (onedit) {
            Log.e("SplitDuesList","Adapter : Inside onedit : "+onedit);
            holder.editAmount.setTextColor(context.getResources().getColor(R.color.black));
            holder.editAmount.setEnabled(false);
            holder.rupeesymbol.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            Log.e("SplitDuesList","Adapter : Inside onedit : "+onedit);
            holder.editAmount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.editAmount.setEnabled(true);
            holder.rupeesymbol.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.txtContactName.setTag(i);
        holder.editAmount.setTag(i);
        holder.friendImage.setTag(i);

        if (contactNumber.get(i).equalsIgnoreCase(AddDuesActivity.User.getPhoneNumber())) {
            strName = "You";
        } else {
            strName = DuesSharedWithModel.getName(contactNumber.get(i));
            if (strName.isEmpty()) {
                strName = contactNumber.get(i);
            }
        }

        if (strName.contains(" ") && strName.length() > 16) {
            if (strName.indexOf(" ") < strName.length() - 2) {
                strName = strName.substring(0, strName.indexOf(" ") + 2);
            } else {
                strName = strName.substring(0, strName.indexOf(" "));
            }
        }

        Log.e("SplitDuesList","Adapter strName : "+strName);
        holder.txtContactName.setText(strName);

        Log.e("SplitDuesList","Adapter Dues Amount "+i+" : "+ GlobalVariables.ConvertDouble(Double.valueOf(Double.parseDouble(duesAmount.get(i)))).toString());
        if (!duesAmount.get(i).equalsIgnoreCase("")) {
            holder.editAmount.setText(GlobalVariables.ConvertDouble(Double.valueOf(Double.parseDouble(duesAmount.get(i)))).toString());
        }

        final int position = i;

        holder.editAmount.setId(i);
        holder.editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().equalsIgnoreCase(GlobalVariables.ConvertDouble(Double.valueOf(Double.parseDouble(duesAmount.get(position)))))
                        && charSequence.toString().length() > 0 && !charSequence.toString().isEmpty()
                        && !charSequence.toString().equalsIgnoreCase(".")) {
                    Log.e("SplitDuesList","Adapter Inside onTextChanged : Position : "+position);
                    dtInterface.setTotal(charSequence.toString(), position);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (imageAdd.get(i).equalsIgnoreCase("")) {
            String strImageUri = DuesSharedWithModel.getImage(contactNumber.get(i));

            if (strImageUri == null) {
                Uri uri = new Uri.Builder().scheme("res") // "res"
                        .path(String.valueOf(R.drawable.icon_placeholder)).build();
                holder.friendImage.setImageURI(uri);
            } else {
                holder.friendImage.setImageURI(strImageUri);
            }
        }
        else {
            holder.friendImage.setImageURI(imageAdd.get(i));
        }

        view.setTag(holder);

        hashMap.put(i,view);
        return view;
    }
}
