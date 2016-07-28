package com.tech.petabyteboy.hisaab.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tech.petabyteboy.hisaab.Global.GlobalVariables;
import com.tech.petabyteboy.hisaab.Models.DuesSharedWithModel;
import com.tech.petabyteboy.hisaab.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DuesSharedWithListAdapter extends BaseAdapter {

    Context context;
    HashMap<Integer, View> hashMap;

    public class Holder {
        ImageView imgCheckedImage;
        SimpleDraweeView imgProfile;
        CheckBox checkbox;
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
        holder.imgProfile = (SimpleDraweeView) view.findViewById(R.id.imgContactGrid);
        holder.imgCheckedImage = (ImageView) view.findViewById(R.id.imgCheckedImage);
        holder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);

        final int position = i;

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                DuesSharedWithModel model = GlobalVariables.data.get(position);

                if (model.isSeleted) {
                    Log.e("DuesShared", "WithList Adapter Inside isSelected : " + model.isSeleted);

                    model.isSeleted = false;
                    GlobalVariables.data.set(position, model);

                    Log.e("DuesShared", "WithListAdapter GlobalVariables.data Values \n"
                            + "Name : " + GlobalVariables.data.get(position).getName() + "\n"
                            + "Number : " + GlobalVariables.data.get(position).getNumber() + "\n"
                            + "Image : " + GlobalVariables.data.get(position).getImage() + "\n"
                            + "AddBtn : " + GlobalVariables.data.get(position).getAddBtn() + "\n"
                            + "isSelected : " + GlobalVariables.data.get(position).isSeleted() + "\n");

                    holder.imgCheckedImage.setVisibility(View.GONE);
                } else {
                    Log.e("DuesShared", "WithList Adapter Inside isSelected : " + model.isSeleted);

                    model.isSeleted = true;
                    GlobalVariables.data.set(position, model);

                    Log.e("DuesShared", "WithListAdapter GlobalVariables.data Values \n"
                            + "Name : " + GlobalVariables.data.get(position).getName() + "\n"
                            + "Number : " + GlobalVariables.data.get(position).getNumber() + "\n"
                            + "Image : " + GlobalVariables.data.get(position).getImage() + "\n"
                            + "AddBtn : " + GlobalVariables.data.get(position).getAddBtn() + "\n"
                            + "isSelected : " + GlobalVariables.data.get(position).isSeleted() + "\n");

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
                Log.e("DuesShared", "WithListAdapter \n AddBtn : yes \n Name : " + model.name);
                holder.txtContactName.setText(model.name);
                holder.checkbox.setVisibility(View.GONE);
                holder.imgCheckedImage.setVisibility(View.GONE);
            } else {
                String name = model.name;
                if (name.isEmpty()) {
                    name = model.number;
                }
                holder.txtContactName.setText(name);

                String imgUri = model.image;
                if (imgUri.isEmpty()) {
                    Uri uri = new Uri.Builder().scheme("res") // "res"
                            .path(String.valueOf(R.drawable.icon_placeholder)).build();

                    Log.e("DuesShared", "WithListAdapter \n Inside model.image : Empty \n Image Uri :" + uri);

                    holder.imgProfile.setImageURI(uri);

                } else {
                    Log.e("DuesShared", "WithListAdapter \nName : " + model.name + "\nNumber : " + model.number
                            + "\nImage URI : " + imgUri);
                    holder.imgProfile.setImageURI(imgUri);
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
            if (GlobalVariables.data.get(i).isSeleted) {
                dataSelected.add(GlobalVariables.data.get(i));
            }
        }

        return dataSelected;

    }

}
