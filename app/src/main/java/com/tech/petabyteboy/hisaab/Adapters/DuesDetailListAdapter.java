package com.tech.petabyteboy.hisaab.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tech.petabyteboy.hisaab.Models.DuesModel;
import com.tech.petabyteboy.hisaab.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by petabyteboy on 30/07/16.
 */
public class DuesDetailListAdapter extends BaseAdapter {

    Context context;
    DuesModel duesModel;
    List<String> UserID;
    List<String> UserName;
    List<String> UserImage;
    List<String> UserSharedAmt;
    List<String> UserPaidAmt;
    Float paytype;

    private static String TAG = "DuesDetailListAdapter";

    public class ViewHolder {
        SimpleDraweeView imgUser;
        TextView txtAmount;
        TextView txtDueCategory;
        TextView txtDueDate;
        TextView txtName;
        TextView txtPayType;
        TextView txtPaid;

    }

    public DuesDetailListAdapter(Context context, DuesModel duesModel,
                                 List<String> UserID, List<String> UserName,
                                 List<String> UserImage, List<String> UserSharedAmt,
                                 List<String> UserPaidAmt) {
        this.context = context;
        this.duesModel = duesModel;
        this.UserID = UserID;
        this.UserName = UserName;
        this.UserImage = UserImage;
        this.UserSharedAmt = UserSharedAmt;
        this.UserPaidAmt = UserPaidAmt;

    }

    @Override
    public int getCount() {
        return UserID.size();
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

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.list_due_detail, null);

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.imgUser = (SimpleDraweeView) view.findViewById(R.id.imgUser);
        viewHolder.txtPayType = (TextView) view.findViewById(R.id.txtPayType);
        viewHolder.txtDueCategory = (TextView) view.findViewById(R.id.txtDueCategory);
        viewHolder.txtDueDate = (TextView) view.findViewById(R.id.txtDueDate);
        viewHolder.txtAmount = (TextView) view.findViewById(R.id.txtAmount);
        viewHolder.txtName = (TextView) view.findViewById(R.id.txtName);
        viewHolder.txtPaid = (TextView) view.findViewById(R.id.txtPaid);

        if (UserImage.get(i).isEmpty() || UserImage.get(i).equalsIgnoreCase("")) {
            Uri uri = new Uri.Builder().scheme("res") // "res"
                    .path(String.valueOf(R.drawable.icon_group)).build();
            viewHolder.imgUser.setImageURI(uri);
        } else {
            viewHolder.imgUser.setImageURI(UserImage.get(i));
        }


        Log.e(TAG, UserSharedAmt.get(i));

        try {
            paytype = Float.parseFloat(UserSharedAmt.get(i));
        } catch (NumberFormatException ex) {
            paytype = 0.0f;
        }

        if (paytype <= 0) {
            viewHolder.txtPayType.setText("You Get");
            viewHolder.txtAmount.setText("\u20B9 " + UserSharedAmt.get(i).replace("-",""));
        } else if (paytype > 0) {
            viewHolder.txtPayType.setText("You Pay");
            viewHolder.txtAmount.setText("\u20B9 " + UserSharedAmt.get(i).replace("-",""));
        }

        viewHolder.txtDueCategory.setText(duesModel.getCategory());

        viewHolder.txtDueDate.setText("on "+duesModel.getDate());

        viewHolder.txtName.setText(UserName.get(i));
        viewHolder.txtPaid.setText(" Paid \u20B9 " + UserPaidAmt.get(i).replace("-",""));

        return view;
    }

}
