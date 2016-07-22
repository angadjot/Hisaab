package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SplitDueActivity extends AppCompatActivity implements View.OnClickListener, DataTransferInterface {

    public static int REQUEST_CODE_SPLIT = 0;

    private ImageButton btnCross;
    private Button btnDone;

    private SimpleDraweeView imgProfile;
    private TextView changepayee;
    private TextView txtUserName;

    private EditText editAmount;
    private EditText editComment;

    private Button btnFood;
    private Button btnDrinks;
    private Button btnMovies;
    private Button btnOuting;
    private Button btnPersonal;
    private Button btnOthers;

    private RelativeLayout layDrinks;
    private RelativeLayout layFood;
    private RelativeLayout layMovies;
    private RelativeLayout layOthers;
    private RelativeLayout layOuting;
    private RelativeLayout layPersonal;
    private RelativeLayout other_layout;
    private TextView other_text;

    private LinearLayout profileBottomLayout;

    private ToggleButton toggleButtonedit;

    private ListView listSharedWith;

    private TextView txtTotalAmount;

    private String edittexttotalamount;
    public static String payeeType;

    private ArrayList<String> duesAmount;
    private ArrayList<String> duesId;
    private ArrayList<String> duesImage;
    private ArrayList<String> duesName;
    private ArrayList<String> duesNumber;

    private boolean is_amount_item_changed = false;
    private boolean is_payee_changed = false;
    private boolean is_toggle = true;
    private boolean is_update = false;

    private String strAmount;
    private String strComment;
    private String strGroupId;
    private String strGroupImage;
    private String strGroupName;
    private String strDueType;
    private String strFromActivity;
    private String strRefType;
    private String strType;

    int update_position;

    private String PayeeNo;
    private String add_dues_shared_amount;

    private String userName;
    private String userPhone;

    SplitDueListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_due);

        userName = AddDuesActivity.userName;
        userPhone = AddDuesActivity.userPhone;

        strFromActivity = getIntent().getExtras().getString("fromActivity");

        strAmount = getIntent().getExtras().getString(AddDuesActivity.amount_key);
        strComment = getIntent().getExtras().getString(AddDuesActivity.comment_key);
        strDueType = getIntent().getExtras().getString(AddDuesActivity.duetype_key);
        strGroupId = getIntent().getExtras().getString(AddDuesActivity.groupID_key);
        strGroupName = getIntent().getExtras().getString(AddDuesActivity.groupName_key);
        strGroupImage = getIntent().getExtras().getString(AddDuesActivity.groupImage_key);

        Log.e("SplitDuesActivity", "strFromActivity : " + strFromActivity + "\n"
                + "strAmount : " + strAmount + "\n" + "strComment : " + strComment + "\n"
                + "strDueType : " + strDueType + "\n" + "strGroupId : " + strGroupId + "\n"
                + "strGroupName : " + strGroupName + "\n" + "strGroupImage : " + strGroupImage + "\n");

        btnCross = (ImageButton) findViewById(R.id.btn_cross);
        btnCross.setOnClickListener(this);

        txtUserName = (TextView) findViewById(R.id.txtUserNameSplit);

        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmountSplit);
        txtTotalAmount.setText(String.valueOf(Double.parseDouble(strAmount.replace(",", ""))));

        imgProfile = (SimpleDraweeView) findViewById(R.id.imgProfileSplit);

        profileBottomLayout = (LinearLayout) findViewById(R.id.profileBottomLayout);
        other_layout = (RelativeLayout) findViewById(R.id.other_layout);

        editAmount = (EditText) findViewById(R.id.editamountSplit);
        editAmount.setText(String.valueOf(Double.parseDouble(strAmount.replace(",", ""))));
        edittexttotalamount = String.valueOf(Double.parseDouble(strAmount.replace(",", "")));
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
                    txtTotalAmount.setText(String.valueOf(Double.parseDouble(strAmount)));
                    edittexttotalamount = charSequence.toString();

                    Log.e("SplitDueActivity", "onTextChange"
                            + "\nDivide Amount " + amtDivide + "\nName Size :" + duesName.size()
                            + "\nAmount Size : " + duesAmount.size() + "\nstrAmount " + strAmount
                            + "\nis_toggle " + is_toggle + "\n edittexttotalamount" + edittexttotalamount);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        toggleButtonedit = (ToggleButton) findViewById(R.id.toggleButtonedit);
        toggleButtonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toggleButtonedit.isChecked()) {
                    Log.e("SplitDuesActivity", "Inside Toggle Button : Toggle Button : " + toggleButtonedit.isChecked());
                    setSplitAdapter(toggleButtonedit.isChecked());
                    is_toggle = true;
                    return;
                }
                Log.e("SplitDuesActivity", "Inside Toggle Button : Toggle Button : " + toggleButtonedit.isChecked());
                setSplitAdapter(toggleButtonedit.isChecked());
                is_toggle = false;
            }
        });

        editComment = (EditText) findViewById(R.id.editCommentSplit);
        editComment.setText(strComment);

        layFood = (RelativeLayout) findViewById(R.id.layFood);
        layDrinks = (RelativeLayout) findViewById(R.id.layDrinks);
        layMovies = (RelativeLayout) findViewById(R.id.layMovies);
        layPersonal = (RelativeLayout) findViewById(R.id.layPersonal);
        layOthers = (RelativeLayout) findViewById(R.id.layOthers);
        layOuting = (RelativeLayout) findViewById(R.id.layOuting);
        other_text = (TextView) findViewById(R.id.other_text);

        changepayee = (TextView) findViewById(R.id.changepayee);
        changepayee.setOnClickListener(this);

        btnFood = (Button) findViewById(R.id.btnFood);
        btnFood.setOnClickListener(this);

        btnDrinks = (Button) findViewById(R.id.btnDrinks);
        btnDrinks.setOnClickListener(this);

        btnMovies = (Button) findViewById(R.id.btnMovies);
        btnMovies.setOnClickListener(this);

        btnOuting = (Button) findViewById(R.id.btnOuting);
        btnOuting.setOnClickListener(this);

        btnPersonal = (Button) findViewById(R.id.btnPersonal);
        btnPersonal.setOnClickListener(this);

        btnOthers = (Button) findViewById(R.id.btnOthers);
        btnOthers.setOnClickListener(this);

        duesAmount = new ArrayList<>();
        duesName = new ArrayList<>();
        duesNumber = new ArrayList<>();
        duesImage = new ArrayList<>();
        duesId = new ArrayList<>();

        for (int i = 0; i < GlobalVariables.splitContactNumber.size(); i++) {
            duesName.add(GlobalVariables.splitContactName.get(i));
            duesNumber.add(GlobalVariables.splitContactNumber.get(i));
            duesImage.add(GlobalVariables.splitContactImage.get(i));

            Log.e("SplitDueActivity", "\nDues Values " + i + "\n"
                    + "Name : " + duesName.get(i) + "\n"
                    + "Number : " + duesNumber.get(i) + "\n"
                    + "Image : " + duesImage.get(i));
        }

        String strName;
        String strImage;

        strName = userName;
        // strImage = get from firebase database
        strImage = "";

        txtUserName.setText("You paid");
        payeeType = "iGET";

        if (strImage.equalsIgnoreCase("")) {
            String imguri = DuesSharedWithModel.getImage(userPhone);
            Log.e("SplitDuesActivity", "Users Image URI " + imguri);
            if (imguri.equalsIgnoreCase("") || imguri == null) {
                Uri uri = new Uri.Builder().scheme("res") // "res"
                        .path(String.valueOf(R.drawable.icon_placeholder)).build();
                imgProfile.setImageURI(uri);
            } else {
                imgProfile.setImageURI(imguri);
            }
        } else {
            imgProfile.setImageURI(strImage);
        }

        duesNumber.add(userPhone);
        duesImage.add(strImage); //Get Image from firebase database
        duesName.add("YOU");

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) other_layout.getLayoutParams();
        lp.setMargins(15, 15, 15, 15);
        other_layout.setLayoutParams(lp);

        if (strFromActivity.contains("Dues")) {
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
        } else {

            initDues();
            profileBottomLayout.setVisibility(View.VISIBLE);
            if (strDueType.equalsIgnoreCase("Food")) {
                layFood.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (strDueType.equalsIgnoreCase("Drinks")) {
                layDrinks.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (strDueType.equalsIgnoreCase("Movies")) {
                layMovies.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (strDueType.equalsIgnoreCase("Outing")) {
                layOuting.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (strDueType.equalsIgnoreCase("Personal")) {
                layPersonal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                other_text.setText(strDueType);
                layOthers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
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

        if (adapter == null){
            listSharedWith = (ListView) findViewById(R.id.listSharedWithSplit);
            adapter = new SplitDueListAdapter(this,duesName,duesNumber,duesImage,duesAmount,is_toggle,this);
            listSharedWith.setAdapter(adapter);
            return;
        }
        adapter = new SplitDueListAdapter(this,duesName,duesNumber,duesImage,duesAmount,is_toggle,this);
        listSharedWith.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.changepayee:
                showAlertDialog(this, txtUserName, imgProfile);
                break;

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
                if (!value.equalsIgnoreCase(txtTotalAmount.getText().toString())) {
                    Toast.makeText(this, "Amount Splitted doesn't match with the Total Amount", Toast.LENGTH_SHORT).show();
                } else if (String.valueOf(Double.parseDouble(value)).equalsIgnoreCase("0.00") || String.valueOf(Double.parseDouble(value)).equalsIgnoreCase(".00")) {
                    Toast.makeText(this, "Amount should be greater than Rs 0.00", Toast.LENGTH_SHORT).show();
                } else {
                    addDuesIntent();
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

    public void showAlertDialog(Context mContext, final TextView txtUserName, final SimpleDraweeView img) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_contact_list);
        dialog.setCancelable(false);
        dialog.show();

        ListView contactNoList = (ListView) dialog.findViewById(R.id.contactNoList);

        contactNoList.setAdapter(new PayeeAdapter(this, duesName, duesNumber, duesImage));

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        contactNoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String usrname;
                if (duesNumber.get(i).equalsIgnoreCase(userPhone)) {
                    usrname = "You";
                    payeeType = "iGET";
                } else {
                    usrname = DuesSharedWithModel.getName(duesNumber.get(i));
                    payeeType = "iPAY";
                    if (usrname.isEmpty()) {
                        usrname = duesNumber.get(i);
                    }
                }

                if (duesImage.get(i).isEmpty()) {
                    String strImageUri = DuesSharedWithModel.getImage(duesNumber.get(i));

                    if (strImageUri == null) {
                        Uri uri = new Uri.Builder().scheme("res") // "res"
                                .path(String.valueOf(R.drawable.icon_placeholder)).build();
                        img.setImageURI(uri);
                    } else {
                        img.setImageURI(strImageUri);
                    }
                } else {
                    img.setImageURI(duesImage.get(i));
                }

                update_position = i;
                txtUserName.setText(usrname + " Paid");
                is_payee_changed = true;
                PayeeNo = duesNumber.get(i);
                dialog.dismiss();

            }
        });
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

    private void addDuesIntent() {

        if (strAmount.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter all Details.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (is_payee_changed) {
            if (!PayeeNo.equalsIgnoreCase(userPhone)) {
                duesNumber.remove(PayeeNo);
                duesNumber.add(PayeeNo);
                String updateamt = duesAmount.get(update_position);
                duesAmount.remove(update_position);
                duesAmount.add(updateamt);
            }
        }

        StringBuilder sb = new StringBuilder();

        int i = 0;
        while (true) {
            if (i >= duesNumber.size()) {
                break;
            }
            if (duesNumber.get(i).length() <= 10) {
                sb.append(duesNumber.get(i));
                if (i != duesNumber.size() - 1) {
                    sb.append(",");
                }
            } else {
                sb.append(duesNumber.get(i).substring((duesNumber.get(i)).length() - 10));
                if (i != duesNumber.size() - 1) {
                    sb.append(",");
                }
            }
            i++;
        }

        String strNumber = String.valueOf(sb);

        Toast.makeText(this, "All Dues Number List : " + strNumber, Toast.LENGTH_SHORT).show();

        ArrayList<String> Send_Push_Number = new ArrayList<>();
        Send_Push_Number.addAll(duesNumber);

        StringBuilder sbAmt = new StringBuilder();
        int amtCount = duesAmount.size();

        for (int count = 0; count < amtCount; i++) {
            sbAmt.append(duesAmount.get(i));
            if (count != amtCount - 1) {
                sbAmt.append(",");
            }
        }

        String strAmtDivided = String.valueOf(sbAmt);
        add_dues_shared_amount = strAmtDivided;

        Toast.makeText(this, "All Dues Amount List : " + add_dues_shared_amount, Toast.LENGTH_SHORT).show();

        // Add Expense in Database

    }

    public void setTotal(String amt, int pos) {

        float amount = 0.0f;

        duesAmount.set(pos, amt);

        for (int i = 0; i < duesAmount.size(); i++) {
            amount += Float.valueOf(duesAmount.get(i));
        }

        txtTotalAmount.setText(String.valueOf(amount));
        edittexttotalamount = String.valueOf(amount);
        editAmount.setVisibility(View.VISIBLE);
        is_amount_item_changed = true;
    }

}
