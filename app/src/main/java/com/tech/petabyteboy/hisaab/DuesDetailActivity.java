package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tech.petabyteboy.hisaab.Models.UserDuesModel;

import java.util.Arrays;
import java.util.List;

public class DuesDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack;
    private Button btnSettle;
    private Button btnremind;

    private SimpleDraweeView imgProfile;

    private TextView profileName;
    private TextView dueAmount;

    //Either IGET or IPAY
    private String usertype;

    private float totalAmount;

    String inform_days;
    String infrom_amt;

    String selfname;

    private int position;
    private static String TAG = "DuesFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dues_detail);

        position = getIntent().getExtras().getInt("position");
        Log.e(TAG,"Position : "+position);

        int index = 0;

        UserDuesModel userDuesModel = DuesFragment.userDuesModelArrayList.get(position);

        String strUserID = userDuesModel.getShared_User_id();
        List<String> UserIDList = Arrays.asList(strUserID.split(","));

        String strUserName = userDuesModel.getShared_User_Name();
        List<String> UserNameList = Arrays.asList(strUserName.split(","));

        String strUserImage = userDuesModel.getShared_User_Image();
        List<String> UserImageList = Arrays.asList(strUserImage.split(","));

        for (int i = 0; i < UserIDList.size(); i++) {
            if (UserIDList.get(i).equalsIgnoreCase(userDuesModel.getWho_created_id())) {
                index = i;
                Log.e(TAG, "Index of Who Created User : " + index);
                break;
            }
        }

        imgProfile = (SimpleDraweeView) findViewById(R.id.imguser);
        imgProfile.setImageURI(UserImageList.get(index));

        btnSettle = (Button) findViewById(R.id.btnsettle);
        btnremind = (Button) findViewById(R.id.btnRemind);
        btnSettle.setOnClickListener(this);
        btnremind.setOnClickListener(this);

        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        profileName = (TextView) findViewById(R.id.profileName);
        profileName.setText(UserNameList.get(index));

        dueAmount = (TextView) findViewById(R.id.duesAmount);
        dueAmount.setText("Total Amount : "+userDuesModel.getTotal_amt());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnRemind:
                if (!isNetworkAvailable()) {
                    Toast.makeText(this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
                } else if (usertype.equalsIgnoreCase("IGet") && ((double) this.totalAmount) != 0.0d) {
                    ShowRemindDialog(this);
                } else if (this.usertype.equalsIgnoreCase("IPay") && ((double) this.totalAmount) != 0.0d) {
                    ShowInfromDialog(this);
                }
                break;

            case R.id.btnsettle:
                if (!isNetworkAvailable()) {
                    Toast.makeText(this, "Please Check Your Internet Connection.",Toast.LENGTH_SHORT).show();
                } else if (usertype.equalsIgnoreCase("IGet")) {
                    showSettleMentDialogIGET();
                } else if (usertype.equalsIgnoreCase("IPay")) {
                    showSettleMentDialogIPAY();
                }
                break;

            case R.id.btn_back:
                finish();
                break;

        }
    }

    public void showSettleMentDialogIGET() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_settlement);
        dialog.getWindow().setSoftInputMode(4);
        dialog.show();

        Button btnUser = (Button) dialog.findViewById(R.id.btnUser);

        ImageView imgUser = (ImageView) dialog.findViewById(R.id.imgUser);

        Button btnFriend = (Button) dialog.findViewById(R.id.btnFriend);

        ImageView imgFriend = (ImageView) dialog.findViewById(R.id.imgFriend);

        // Not Complete

    }

    public void showSettleMentDialogIPAY(){

    }

    public void LoadExpenseFromDatabase() {

    }

    public boolean isNetworkAvailable() {

        return false;
    }

    public void ShowRemindDialog(Context c) {

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_layout);
        dialog.show();

        String msg = "Hey! " + this.selfname + " just wanted to remind you that you have to pay " + String.valueOf(totalAmount);

        ((TextView) dialog.findViewById(R.id.title)).setText(msg);

        ((Button) dialog.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setText("SEND");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendReminder("Remind");
                dialog.dismiss();
            }
        });
    }

    public void SendReminder(String type){

    }

    public void ShowInfromDialog(Context c) {

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        dialog.setContentView(R.layout.dialoginform_layout);
        dialog.show();

        final EditText edtamt = (EditText) dialog.findViewById(R.id.editAmount);
        final EditText editdate = (EditText) dialog.findViewById(R.id.editdate);

        edtamt.setText((String.valueOf(totalAmount)).substring(1));
        edtamt.setSelection(String.valueOf(totalAmount).substring(1).length());

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnCancel.setText("CANCEL");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.btnOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtamt.getText().toString().equalsIgnoreCase("") || editdate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),"Please enter all details.",Toast.LENGTH_SHORT).show();
                    return;
                }
                inform_days = editdate.getText().toString();
                infrom_amt = edtamt.getText().toString();
                SendReminder("Inform");
                dialog.dismiss();
            }
        });
    }
}
