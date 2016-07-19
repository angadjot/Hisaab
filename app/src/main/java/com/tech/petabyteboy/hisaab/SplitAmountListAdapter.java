package com.tech.petabyteboy.hisaab;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by petabyteboy on 19/07/16.
 */
public class SplitAmountListAdapter extends BaseAdapter {

    ArrayList<String> contactName;
    ArrayList<String> contactNumber;
    Context context;
    DataTransferInterface dtInterface;
    ArrayList<String> expenseAmount;
    HashMap<Integer, View> hashMap;
    ArrayList<String> imageAdd;
    Boolean onedit;

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

        holder.txtContactName = (TextView) view.findViewById(R.id.txtContactName);
        holder.editAmount = (EditText) view.findViewById(R.id.editAmount);
        holder.friendImage = (SimpleDraweeView) view.findViewById(R.id.friendImage);
        holder.rupeesymbol = (TextView) view.findViewById(R.id.rupeesymbol);

        if (onedit.booleanValue()) {
            holder.editAmount.setTextColor(context.getResources().getColor(R.color.black));
            holder.editAmount.setEnabled(false);
            holder.rupeesymbol.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.editAmount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.editAmount.setEnabled(true);
            holder.rupeesymbol.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.txtContactName.setTag(i);
        holder.editAmount.setTag(i);
        holder.friendImage.setTag(i);

        if (contactNumber.get(i).toString().equalsIgnoreCase(SplitDueActivity.userPhone)) {
            strName = "You";
        } else {
            strName = ContactManager.getInstance().getName(contactName.get(i).toString());
            if (strName.isEmpty()) {
                strName = contactNumber.get(i);
            }
        }

        holder.txtContactName.setText(strName);

        if (!expenseAmount.get(i).toString().isEmpty()) {
            holder.editAmount.setText(String.valueOf(Double.parseDouble(expenseAmount.get(i))));
        }
        holder.editAmount.setId(i);
        holder.editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equalsIgnoreCase(String.valueOf(Double.parseDouble(expenseAmount.get(i))))
                        && charSequence.toString().length() > 0 && !charSequence.toString().isEmpty()
                        && !charSequence.toString().equalsIgnoreCase(".")) {
                    dtInterface.setTotal(charSequence.toString(), i);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (imageAdd.get(i).isEmpty()) {
            String strImageUri = ContactManager.getInstance().getImage(contactNumber.get(i)).toString();

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

    public class Holder {
        EditText editAmount;
        SimpleDraweeView friendImage;
        TextView txtContactName;
        TextView rupeesymbol;
    }

    public SplitAmountListAdapter(Context context, ArrayList<String> contactName, ArrayList<String> contactNumber, ArrayList<String> imageAdd, ArrayList<String> expenseAmount, Boolean is_toggle, DataTransferInterface dtInterface) {

        imageAdd = new ArrayList<>();
        contactName = new ArrayList<>();
        contactNumber = new ArrayList<>();
        expenseAmount = new ArrayList<>();
        hashMap = new HashMap<>();
        this.context = context;
        this.imageAdd = imageAdd;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.expenseAmount = expenseAmount;
        this.dtInterface = dtInterface;
        onedit = is_toggle;
    }

    public interface DataTransferInterface {
        void setTotal(String str, int i);
    }
}
