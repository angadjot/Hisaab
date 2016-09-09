package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.petabyteboy.hisaab.Adapters.SplitDuesListAdapter;
import com.tech.petabyteboy.hisaab.Global.HelperClass;
import com.tech.petabyteboy.hisaab.Interfaces.DataTransferInterface;
import com.tech.petabyteboy.hisaab.Models.HomeDuesModel;
import com.tech.petabyteboy.hisaab.Models.DuesModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SplitDuesActivity extends AppCompatActivity implements View.OnClickListener, DataTransferInterface {

    private SimpleDraweeView imageDues;
    private TextView txtDueName;

    private EditText editAmount;

    private RelativeLayout layDrinks;
    private RelativeLayout layFood;
    private RelativeLayout layMovies;
    private RelativeLayout layOthers;
    private RelativeLayout layOuting;
    private RelativeLayout layPersonal;
    private TextView other_text;

    private ToggleButton toggleButtonEdit;

    private ListView listSharedWith;

    private TextView txtTotalAmount;

    private String editTextTotalAmount;
    public static String payeeType;

    private ArrayList<String> duesAmount;
    private ArrayList<String> duesImage;
    private ArrayList<String> duesName;
    private ArrayList<String> duesNumber;

    private boolean is_amount_item_changed = false;
    private boolean is_toggle = true;

    private String strAmount;
    private String strComment;
    private String strDueType;
    private String strDuesName;
    private String strDuesImage;

    int update_position;

    private String CreatorNo;

    private static String strPhoneNo;
    private static String strUserName;
    private static String strUserImage;

    // FireBase Database
    private DatabaseReference duesDataRef;
    private DatabaseReference duesListDataRef;
    private DatabaseReference homeDuesDataRef;

    private String TAG = "SplitDueActivity";

    private Boolean[] flag_HomeDue_exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_due);

        strUserName = AddDuesDetailActivity.User.getUsername();
        strPhoneNo = AddDuesDetailActivity.User.getPhoneNumber();
        strUserImage = AddDuesDetailActivity.User.getImage();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        duesDataRef = firebaseDatabase.getReference().child("Dues");
        duesListDataRef = firebaseDatabase.getReference().child("DuesList");
        homeDuesDataRef = firebaseDatabase.getReference().child("HomeDues");

        strAmount = getIntent().getExtras().getString(AddDuesDetailActivity.amount_key);
        strComment = getIntent().getExtras().getString(AddDuesDetailActivity.comment_key);
        strDuesName = getIntent().getExtras().getString(AddDuesDetailActivity.dueName_key);
        strDuesImage = getIntent().getExtras().getString(AddDuesDetailActivity.dueImage_key);
        strDueType = getIntent().getExtras().getString(AddDuesDetailActivity.duetype_key);

        Log.e("SplitDuesActivity", "strAmount : " + strAmount + "\n" + "strComment : " + strComment + "\n"
                + "strDueType : " + strDueType + "\n" + "strGroupId : ");

        ImageButton btnCross = (ImageButton) findViewById(R.id.btn_cross);
        btnCross.setOnClickListener(this);

        txtDueName = (TextView) findViewById(R.id.txtDueName);
        txtDueName.setText(strDuesName);

        Button btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmountSplit);
        txtTotalAmount.setText(HelperClass.ConvertDouble(Double.parseDouble(strAmount.replace(",", ""))));

        imageDues = (SimpleDraweeView) findViewById(R.id.imageDues);
        imageDues.setImageURI(strDuesImage);

        RelativeLayout other_layout = (RelativeLayout) findViewById(R.id.other_layout);

        editAmount = (EditText) findViewById(R.id.editamountSplit);
        editAmount.setText(HelperClass.ConvertDouble(Double.parseDouble(strAmount.replace(",", ""))));
        editTextTotalAmount = String.valueOf(HelperClass.ConvertDouble(Double.parseDouble(strAmount.replace(",", ""))));
        editAmount.setVisibility(View.VISIBLE);

        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!is_amount_item_changed && !editAmount.getText().toString().equalsIgnoreCase(strAmount)
                        && editAmount.getText().toString().length() > 0
                        && !editAmount.getText().toString().equalsIgnoreCase(".")) {

                    Float amtDivide = Float.valueOf(editAmount.getText().toString()) / ((float) duesName.size());

                    duesAmount.clear();

                    for (int count = 0; count < duesName.size(); count++) {
                        duesAmount.add("" + amtDivide);
                    }

                    strAmount = editAmount.getText().toString();
                    setSplitAdapter(is_toggle);
                    txtTotalAmount.setText(HelperClass.ConvertDouble(Double.parseDouble(strAmount)));
                    editTextTotalAmount = charSequence.toString();

                    Log.e("SplitDueActivity", "onTextChange"
                            + "\nDivide Amount " + amtDivide + "\nName Size :" + duesName.size()
                            + "\nAmount Size : " + duesAmount.size() + "\nstrAmount " + strAmount
                            + "\nis_toggle " + is_toggle + "\n editTextTotalAmount" + editTextTotalAmount);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        toggleButtonEdit = (ToggleButton) findViewById(R.id.toggleButtonedit);
        toggleButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toggleButtonEdit.isChecked()) {
                    Log.e("SplitDuesActivity", "Inside Toggle Button : Toggle Button : " + toggleButtonEdit.isChecked());
                    setSplitAdapter(toggleButtonEdit.isChecked());
                    is_toggle = true;
                    return;
                }
                Log.e("SplitDuesActivity", "Inside Toggle Button : Toggle Button : " + toggleButtonEdit.isChecked());
                setSplitAdapter(toggleButtonEdit.isChecked());
                is_toggle = false;
            }
        });

        EditText editComment = (EditText) findViewById(R.id.editCommentSplit);
        editComment.setText(strComment);

        layFood = (RelativeLayout) findViewById(R.id.layFood);
        layDrinks = (RelativeLayout) findViewById(R.id.layDrinks);
        layMovies = (RelativeLayout) findViewById(R.id.layMovies);
        layPersonal = (RelativeLayout) findViewById(R.id.layPersonal);
        layOthers = (RelativeLayout) findViewById(R.id.layOthers);
        layOuting = (RelativeLayout) findViewById(R.id.layOuting);
        other_text = (TextView) findViewById(R.id.other_text);

        Button btnFood = (Button) findViewById(R.id.btnFood);
        btnFood.setOnClickListener(this);

        Button btnDrinks = (Button) findViewById(R.id.btnDrinks);
        btnDrinks.setOnClickListener(this);

        Button btnMovies = (Button) findViewById(R.id.btnMovies);
        btnMovies.setOnClickListener(this);

        Button btnOuting = (Button) findViewById(R.id.btnOuting);
        btnOuting.setOnClickListener(this);

        Button btnPersonal = (Button) findViewById(R.id.btnPersonal);
        btnPersonal.setOnClickListener(this);

        Button btnOthers = (Button) findViewById(R.id.btnOthers);
        btnOthers.setOnClickListener(this);

        duesAmount = new ArrayList<>();
        duesName = new ArrayList<>();
        duesNumber = new ArrayList<>();
        duesImage = new ArrayList<>();

        for (int i = 0; i < HelperClass.splitContactNumber.size(); i++) {
            duesName.add(HelperClass.splitContactName.get(i));
            duesNumber.add(HelperClass.splitContactNumber.get(i));
            duesImage.add(HelperClass.splitContactImage.get(i));

            Log.e("SplitDueActivity", "\nDues Values " + i + "\n"
                    + "Name : " + duesName.get(i) + "\n"
                    + "Number : " + duesNumber.get(i) + "\n"
                    + "Image : " + duesImage.get(i));
        }

        payeeType = "iGET";

        duesNumber.add(strPhoneNo);
        if (strUserImage == null || strUserImage.equalsIgnoreCase("")) {
            Uri uri = new Uri.Builder().scheme("res") // "res"
                    .path(String.valueOf(R.drawable.icon_placeholder)).build();
            duesImage.add(uri.toString());
        } else {
            duesImage.add(strUserImage);
        }
        duesName.add(strUserName);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) other_layout.getLayoutParams();
        lp.setMargins(15, 15, 15, 15);
        other_layout.setLayoutParams(lp);

        initDues();
        if (strDueType.equalsIgnoreCase("Food"))
            layFood.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        else if (strDueType.equalsIgnoreCase("Drinks"))
            layDrinks.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        else if (strDueType.equalsIgnoreCase("Movies"))
            layMovies.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        else if (strDueType.equalsIgnoreCase("Outing"))
            layOuting.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        else if (strDueType.equalsIgnoreCase("Personal"))
            layPersonal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        else {
            other_text.setText(strDueType);
            layOthers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        Float amtDivide = Float.valueOf(strAmount) / ((float) duesName.size());

        Log.e("SplitDuesActivity", "amtDivide : " + amtDivide);

        for (int i = 0; i < duesName.size(); i++) {
            duesAmount.add("" + amtDivide);

            Log.e("SplitDuesActivity", "Dues Amount " + i + " : " + duesAmount.get(i));
        }

        Log.e("SplitDuesActivity", "DuesName Size : " + duesName.size() + "\nDuesAmount Size : " + duesAmount.size());

        //Debug
        for (int i = 0; i < duesNumber.size(); i++) {
            Log.e("SplitDuesActivity", "Dues values " + i
                    + "\n Name : " + duesName.get(i) + "\n Number : " + duesNumber.get(i)
                    + "\n Image : " + duesImage.get(i) + "\n Due Amount : " + duesAmount.get(i));
        }

        listSharedWith = (ListView) findViewById(R.id.listSharedWithSplit);
        setSplitAdapter(is_toggle);

    }

    public void setSplitAdapter(Boolean is_toggle) {

        Log.e("SplitDuesActivity", "Inside setAdapter :\nAdater is_toggle : " + is_toggle);
        SplitDuesListAdapter adapter = new SplitDuesListAdapter(this, duesName, duesNumber, duesImage, duesAmount, is_toggle, this);
        listSharedWith.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnFood:
                layFood.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layDrinks.setBackgroundColor(0);
                layMovies.setBackgroundColor(0);
                layOuting.setBackgroundColor(0);
                layPersonal.setBackgroundColor(0);
                layOthers.setBackgroundColor(0);
                strDueType = "Food";
                break;

            case R.id.btnDrinks:
                layFood.setBackgroundColor(0);
                layDrinks.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layMovies.setBackgroundColor(0);
                layOuting.setBackgroundColor(0);
                layPersonal.setBackgroundColor(0);
                layOthers.setBackgroundColor(0);
                strDueType = "Drinks";
                break;

            case R.id.btnMovies:
                layFood.setBackgroundColor(0);
                layDrinks.setBackgroundColor(0);
                layMovies.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layOuting.setBackgroundColor(0);
                layPersonal.setBackgroundColor(0);
                layOthers.setBackgroundColor(0);
                strDueType = "Movies";
                break;

            case R.id.btnOuting:
                layFood.setBackgroundColor(0);
                layDrinks.setBackgroundColor(0);
                layMovies.setBackgroundColor(0);
                layOuting.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layPersonal.setBackgroundColor(0);
                layOthers.setBackgroundColor(0);
                strDueType = "Outing";
                break;

            case R.id.btnPersonal:
                layFood.setBackgroundColor(0);
                layDrinks.setBackgroundColor(0);
                layMovies.setBackgroundColor(0);
                layOuting.setBackgroundColor(0);
                layPersonal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layOthers.setBackgroundColor(0);
                strDueType = "Personal";
                break;

            case R.id.btnOthers:
                layFood.setBackgroundColor(0);
                layDrinks.setBackgroundColor(0);
                layMovies.setBackgroundColor(0);
                layOuting.setBackgroundColor(0);
                layPersonal.setBackgroundColor(0);
                layOthers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                strDueType = "Others";
                if (other_text.getText().toString().equalsIgnoreCase("Other")) {
                    showOtherCategoryDialog("Other");
                } else {
                    showOtherCategoryDialog(other_text.getText().toString());
                }
                break;

            case R.id.btn_cross:
                finish();
                break;

            case R.id.btn_done:

                String str_value;
                String value = editAmount.getText().toString();

                int index = value.indexOf(".");
                if (index != -1) {
                    str_value = value.substring(index + 1, value.length());
                    if (str_value.length() == 1) {
                        value = value + "0";
                    } else if (str_value.length() != 2) {
                        value = value + "00";
                    }
                } else {
                    value = value + ".00";
                }

                Log.e("SplitDueActivity", "Value : " + value);
                //Log.e("SplitDueActivity", "Total Amount : " + formatDecimal(txtTotalAmount.getText().toString()));
                if (!value.equalsIgnoreCase(txtTotalAmount.getText().toString())) {
                    Toast.makeText(this, "Amount Splitted doesn't match with the Total Amount", Toast.LENGTH_SHORT).show();
                } else if (HelperClass.ConvertDouble(Double.parseDouble(value)).equalsIgnoreCase("0.00")
                        || HelperClass.ConvertDouble(Double.parseDouble(value)).equalsIgnoreCase(".00")) {
                    Toast.makeText(this, "Amount should be greater than Rs 0.00", Toast.LENGTH_SHORT).show();
                } else {
                    AddDuesIntent();
                }

                break;

        }
    }

    private void initDues() {
        layFood.setBackgroundColor(0);
        layDrinks.setBackgroundColor(0);
        layMovies.setBackgroundColor(0);
        layOuting.setBackgroundColor(0);
        layPersonal.setBackgroundColor(0);
        layOthers.setBackgroundColor(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showOtherCategoryDialog(String str_category) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_add_other);
        dialog.show();
        final EditText editAddCategory = (EditText) dialog.findViewById(R.id.editAddCategory);
        if (str_category.equalsIgnoreCase("Other")) {
            editAddCategory.setText("");
        } else
            editAddCategory.setText(str_category);
        editAddCategory.setSelection(editAddCategory.length());
        (dialog.findViewById(R.id.btnDialogOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editAddCategory.getText().toString().equalsIgnoreCase("Other")) {
                    Toast.makeText(getApplicationContext(), "Please add Category", Toast.LENGTH_SHORT).show();
                    return;
                }
                strDueType = editAddCategory.getText().toString();
                other_text.setText(strDueType);
                dialog.dismiss();
            }
        });

        (dialog.findViewById(R.id.btnDialogCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void AddDuesIntent() {

        if (strAmount.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter all Details.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sbNumber = new StringBuilder();
        StringBuilder sbName = new StringBuilder();
        StringBuilder sbImage = new StringBuilder();
        StringBuilder sbPaidAmt = new StringBuilder();
        StringBuilder sbDividedAmt = new StringBuilder();

        String strNumber;
        String strNames;
        String strImage;
        String strAmtPaid;
        String strAmtDivided;

        DuesModel duesModel = new DuesModel();

        for (int i = 0; i < duesNumber.size(); i++) {
            if (duesNumber.get(i).length() <= 10) {
                sbNumber.append(duesNumber.get(i));
                if (i != duesNumber.size())
                    sbNumber.append(",");
            } else {
                sbNumber.append(duesNumber.get(i).substring(duesNumber.get(i).length() - 10));
                if (i != duesNumber.size())
                    sbNumber.append(",");
            }
        }
        strNumber = String.valueOf(sbNumber);
        Log.e("SplitDuesActivity", "strNumber : " + strNumber);

        for (int count = 0; count < duesName.size(); count++) {
            sbName.append(duesName.get(count));
            if (count != duesName.size() - 1) {
                sbName.append(",");
            }
        }
        strNames = String.valueOf(sbName);
        Log.e("SplitDuesActivity", "strNames : " + strNames);

        for (int count = 0; count < duesImage.size(); count++) {
            sbImage.append(duesImage.get(count));
            if (count != duesImage.size() - 1) {
                sbImage.append(",");
            }
        }
        strImage = String.valueOf(sbImage);
        Log.e("SplitDuesActivity", "strImage : " + strImage);

        for (int count = 0; count < duesAmount.size(); count++) {
            sbPaidAmt.append(HelperClass.ConvertDouble(Double.valueOf(duesAmount.get(count))));
            if (count != duesAmount.size() - 1) {
                sbPaidAmt.append(",");
            }
        }
        strAmtPaid = String.valueOf(sbPaidAmt);
        Log.e("SplitDuesActivity", "strAmtPaid : " + strAmtPaid);

        Float avgamt = Float.valueOf(strAmount) / duesNumber.size();

        if (!is_toggle) {

            for (int i = 0; i < duesNumber.size(); i++) {
                String amtDivided = HelperClass.ConvertDouble(Double.parseDouble(String.valueOf(avgamt - Float.parseFloat(duesAmount.get(i)))));
                sbDividedAmt.append(amtDivided);
                if (i != duesNumber.size()) {
                    sbDividedAmt.append(",");
                }
            }
            strAmtDivided = String.valueOf(sbDividedAmt);
            Log.e(TAG, "strAmtDivided : " + strAmtDivided);
        } else {
            for (int i = 0; i < duesNumber.size(); i++) {
                String amtDivided = "0.00";
                sbDividedAmt.append(amtDivided);
                if (i != duesNumber.size()) {
                    sbDividedAmt.append(",");
                }
            }
            strAmtDivided = String.valueOf(sbDividedAmt);
            Log.e(TAG, "strAmtDivided : " + strAmtDivided);
        }

        Log.e("SplitDuesActivity", "Total Amount : " + strAmount);

        Log.e("SplitDuesActivity", "\nDues Name : " + strNames
                + "\nDues Number : " + strNumber
                + "\nDues Image : " + strImage
                + "\nDues Amount : " + strAmtPaid);

        String Date = DateFormat.getDateInstance().format(new Date());
        String Time = DateFormat.getTimeInstance().format(new Date());

        Log.e(TAG, "Time : " + Time);

        duesModel.setDueName(strDuesName);
        duesModel.setDueImage(strDuesImage);
        duesModel.setCategory(strDueType);
        duesModel.setComment(strComment);
        duesModel.setDate(Date);
        duesModel.setTime(Time);
        duesModel.setSharedUserID(strNumber);
        duesModel.setSharedUserName(strNames);
        duesModel.setSharedUserImage(strImage);
        duesModel.setUserPaidAmt(strAmtPaid);
        duesModel.setUserSharedAmt(strAmtDivided);
        duesModel.setSharedAmongSize(duesNumber.size());
        duesModel.setTotalAmt(strAmount);
        duesModel.setCreatorID(strPhoneNo);

        DatabaseReference duesRef = duesDataRef.push();
        String DuesID = duesRef.getKey();
        duesModel.setDueID(DuesID);

        duesRef.setValue(duesModel);

        for (int i = 0; i < duesNumber.size(); i++) {
            duesListDataRef.child(duesNumber.get(i)).child(DuesID).setValue(true);
        }

        QueryHomeDues();

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();


    }

    private void QueryHomeDues() {

        flag_HomeDue_exists = new Boolean[duesNumber.size()];
        Arrays.fill(flag_HomeDue_exists, Boolean.FALSE);

        homeDuesDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String userID = dataSnapshot.getKey();
                Log.e(TAG, "CHILD ADDED Called");
                Log.e(TAG, "User ID : " + userID);
                for (int i = 0; i < duesNumber.size(); i++) {
                    Log.e(TAG, "DuesNumber " + i + " : " + duesNumber.get(i));
                    if (userID.equalsIgnoreCase(duesNumber.get(i))) {
                        Log.e(TAG, "User ID Exists");
                        flag_HomeDue_exists[i] = true;
                        HomeDuesModel homeDuesModel = dataSnapshot.getValue(HomeDuesModel.class);
                        Log.e(TAG, "Total Amount : " + strAmount);
                        Float avgamt = Float.valueOf(strAmount) / duesNumber.size();
                        Log.e(TAG, "Avg amt : " + avgamt);
                        Log.e(TAG, "Dues Amount : " + duesAmount.get(i));
                        Float self_amt = avgamt - Float.valueOf(duesAmount.get(i));
                        Log.e(TAG, "Self_amt : " + self_amt);
                        Double total_amt = Double.parseDouble(homeDuesModel.getAmount()) + self_amt;
                        Log.e(TAG, "Total amt : " + total_amt);
                        String amount = HelperClass.ConvertDouble(total_amt);
                        Log.e(TAG, "Final Amt : " + amount);
                        homeDuesModel.setUserID(duesNumber.get(i));
                        homeDuesModel.setAmount(amount);

                        if (total_amt < 0)
                            homeDuesModel.setPayType("iGet");
                        else if (total_amt == 0)
                            homeDuesModel.setPayType("NoDue");
                        else if (total_amt > 0)
                            homeDuesModel.setPayType("iPay");

                        homeDuesDataRef.child(duesNumber.get(i)).setValue(homeDuesModel);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, "Child Changed Called");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        homeDuesDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "DATA LOADED");
                for (int i = 0; i < duesNumber.size(); i++)
                    Log.e(TAG, "Flag Exists " + i + " : " + flag_HomeDue_exists[i]);

                AddHomeDuesInFirebase();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "ERROR AFTER LOADED");
            }
        });

    }

    private void AddHomeDuesInFirebase() {

        for (int i = 0; i < duesNumber.size(); i++) {
            Log.e(TAG, "Dues Number " + i + " : " + duesNumber.get(i) + "\nFlag Exists " + i + " : " + flag_HomeDue_exists[i]);
            if (!flag_HomeDue_exists[i]) {
                HomeDuesModel homeDuesModel = new HomeDuesModel();

                homeDuesModel.setUserID(duesNumber.get(i));
                Float avgamt = Float.valueOf(strAmount) / duesNumber.size();
                Float self_amt = avgamt - Float.valueOf(duesAmount.get(i));

                if (self_amt < 0)
                    homeDuesModel.setPayType("iPay");
                else if (self_amt >= 0)
                    homeDuesModel.setPayType("iGet");

                String amount = HelperClass.ConvertDouble(Double.valueOf(self_amt));
                homeDuesModel.setAmount(amount);
                homeDuesDataRef.child(duesNumber.get(i)).setValue(homeDuesModel);
            }
        }
    }

    public void setTotal(String amt, int pos) {

        float amount = 0.0f;

        duesAmount.set(pos, amt);

        Log.e("SplitDueActivity", "setTotal Check : Dues Amount : " + duesAmount.get(pos));

        for (int i = 0; i < duesAmount.size(); i++) {
            amount += Float.valueOf(duesAmount.get(i));
        }

        Log.e("SplitDueActivity", "Amount : " + HelperClass.ConvertDouble((double) amount));

        txtTotalAmount.setText(HelperClass.ConvertDouble((double) amount));
        editTextTotalAmount = HelperClass.ConvertDouble((double) amount);
        editAmount.setVisibility(View.VISIBLE);
        is_amount_item_changed = true;

    }

}
