package com.tech.petabyteboy.hisaab;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;

public class DuesSharedWithListAdapter extends BaseAdapter {

    Context context;
    HashMap<Integer, View> hashMap;

    public class Holder {
        ImageView imgCheckedImage;
        SimpleDraweeView imgProfile;
        CheckBox imgQueueMultiSelected;
        TextView txtContactName;
    }

    public DuesSharedWithListAdapter(Context context) {
        this.context = context;
        hashMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return GlobalVariables.data.size();
    }

    @Override
    public Object getItem(int i) {
        return GlobalVariables.data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (hashMap.containsKey(i))
            return hashMap.get(i);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.contact_grid_layout, null);

        final Holder holder = new Holder();

        holder.txtContactName = (TextView) view.findViewById(R.id.txtContactName);
        holder.imgProfile = (SimpleDraweeView) view.findViewById(R.id.imgProfile);
        holder.imgCheckedImage = (ImageView) view.findViewById(R.id.imgCheckedImage);
        holder.imgQueueMultiSelected = (CheckBox) view.findViewById(R.id.imgQueueMultiSelected);

        final int position = i;

        holder.imgQueueMultiSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                DuesSharedWithModel model = GlobalVariables.data.get(position);

                if (model.isSeleted) {
                    model.isSeleted = false;
                    GlobalVariables.data.set(position, model);
                    holder.imgCheckedImage.setVisibility(View.GONE);
                } else {
                    model.isSeleted = true;
                    GlobalVariables.data.set(position, model);
                    holder.imgCheckedImage.setVisibility(View.VISIBLE);
                }
            }
        });

        try {
            DuesSharedWithModel model = GlobalVariables.data.get(position);

            if (model.isSeleted)
                holder.imgCheckedImage.setVisibility(View.VISIBLE);
            else
                holder.imgCheckedImage.setVisibility(View.GONE);

            if (model.addBtn.equalsIgnoreCase("yes")) {
                holder.txtContactName.setText(model.name);
                holder.imgQueueMultiSelected.setVisibility(View.GONE);
                holder.imgCheckedImage.setVisibility(View.GONE);
            } else {
                String name = ContactManager.getInstance().getName(model.number);
                if (name.isEmpty() || name.equalsIgnoreCase("")) {
                    name = model.number;
                }
                holder.txtContactName.setText(name);

                if (model.image.isEmpty() || model.image.equalsIgnoreCase("")) {
                    String strImageUri = ContactManager.getInstance().getImage(model.number);

                    if (strImageUri == null || strImageUri.isEmpty() || strImageUri.equalsIgnoreCase("")) {
                        //test This first
                        Uri uri = new Uri.Builder().scheme("res") // "res"
                                .path(String.valueOf(R.drawable.icon_placeholder)).build();
                        holder.imgProfile.setImageURI(uri);
                    } else {
                        holder.imgProfile.setImageURI(Uri.parse(strImageUri));
                    }
                } else {
                    holder.imgProfile.setImageURI(model.image);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        hashMap.put(position, view);
        view.setTag(holder);
        return view;
    }

    public ArrayList<DuesSharedWithModel> getSelected() {

        ArrayList<DuesSharedWithModel> dataSelected = new ArrayList<>();
        for (int i = 0; i < GlobalVariables.data.size(); i++) {
            if ( GlobalVariables.data.get(i).isSeleted ) {
                dataSelected.add(GlobalVariables.data.get(i));
            }
        }

        return dataSelected;

    }

}
