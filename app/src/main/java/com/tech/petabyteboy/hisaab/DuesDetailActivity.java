package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tech.petabyteboy.hisaab.Adapters.DuesDetailListAdapter;
import com.tech.petabyteboy.hisaab.Global.HelperClass;
import com.tech.petabyteboy.hisaab.Global.NetworkUtils;
import com.tech.petabyteboy.hisaab.Models.DuesModel;

import java.util.Arrays;
import java.util.List;

public class DuesDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String selfName;
    private DuesModel duesModel;
    private static String TAG = "DuesDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dues_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_due_detail);
        setSupportActionBar(toolbar);
        setTitle(R.string.due_detail_title);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int position = getIntent().getExtras().getInt("position");
        Log.e(TAG, "Position : " + position);

        SharedPreferences userPref = getSharedPreferences(RegisterActivity.PREF_NAME, MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + userPref.getString("phone", null));
        String UserID = userPref.getString("phone", null);

        int index , index_self = 0;

        duesModel = DuesFragment.duesModelArrayList.get(position);

        String strUserID = duesModel.getSharedUserID();
        List<String> UserIDList = Arrays.asList(strUserID.split(","));

        for (int i = 0; i < UserIDList.size(); i++)
            Log.e(TAG, "UserSharedAmtList : " + UserIDList.get(i));

        String strUserName = duesModel.getSharedUserName();
        List<String> UserNameList = Arrays.asList(strUserName.split(","));

        String strUserImage = duesModel.getSharedUserImage();
        List<String> UserImageList = Arrays.asList(strUserImage.split(","));

        String strUserSharedAmt = duesModel.getUserSharedAmt();
        List<String> UserSharedAmtList = Arrays.asList(strUserSharedAmt.split(","));

        String strUserPaidAmt = duesModel.getUserPaidAmt();
        List<String> UserPaidAmtList = Arrays.asList(strUserPaidAmt.split(","));

        for (int i = 0; i < UserSharedAmtList.size(); i++)
            Log.e(TAG, "UserSharedAmtList : " + UserSharedAmtList.get(i));

        for (int i = 0; i < UserIDList.size(); i++) {
            if (UserIDList.get(i).equalsIgnoreCase(duesModel.getCreatorID())) {
                index = i;
                Log.e(TAG, "Index of Who Created User : " + index);
                break;
            }
        }

        for (int i = 0; i < UserIDList.size(); i++) {
            if (UserIDList.get(i).equalsIgnoreCase(UserID)) {
                index_self = i;
                Log.e(TAG, "Index of Self User : " + index_self);
                break;
            }
        }

        selfName = UserNameList.get(index_self);

        SimpleDraweeView imageDue = (SimpleDraweeView) findViewById(R.id.imageDueDetail);
        imageDue.setImageURI(duesModel.getDueImage());

        Button btnSettle = (Button) findViewById(R.id.btnsettle);
        Button btnremind = (Button) findViewById(R.id.btnRemind);
        btnSettle.setOnClickListener(this);
        btnremind.setOnClickListener(this);


        TextView DueName = (TextView) findViewById(R.id.DueName);
        DueName.setText(duesModel.getDueName());

        TextView dueAmount = (TextView) findViewById(R.id.duesAmount);
        dueAmount.setText("Total Amount : " + duesModel.getTotalAmt());

        ListView listDueDetails = (ListView) findViewById(R.id.listDueDetail);

        DuesDetailListAdapter adapter = new DuesDetailListAdapter(this,duesModel,UserIDList,UserNameList,UserImageList,UserSharedAmtList,UserPaidAmtList);
        listDueDetails.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnRemind:
                if (!NetworkUtils.checkConnection(this)) {
                    Toast.makeText(this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
                } else
                    ShowRemindDialog(this);
                break;

            case R.id.btnsettle:
                if (!NetworkUtils.checkConnection(this)) {
                    Toast.makeText(this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
                } else
                    showSettlementDialog(this);
                break;

        }
    }

    private void ShowRemindDialog(Context context) {

        String msg = "Hey! " + selfName + " just wanted to remind you that you have to pay  \u20b9  " + HelperClass.ConvertDouble(Double.valueOf(duesModel.getTotalAmt()));
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_layout);
        dialog.show();

        ((TextView) dialog.findViewById(R.id.title)).setText(msg);

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
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
                SendReminder();
                dialog.dismiss();
            }
        });

    }

    private void showSettlementDialog(Context context) {
        String msg = "Want to Settle All the Dues?";
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_layout);
        dialog.show();

        ((TextView) dialog.findViewById(R.id.title)).setText(msg);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setText("No");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setText("Yes");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettleDue();
                dialog.dismiss();
            }
        });
    }

    private void SendReminder() {

    }

    private void SettleDue() {

    }


}
