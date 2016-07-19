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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.drawee.view.GenericDraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SplitDueActivity extends AppCompatActivity implements View.OnClickListener {

    public static int REQUEST_Split_Code = 0;

    private ImageButton btnDuesCross;
    private Button btnDuesDone;

    private SimpleDraweeView imgProfile;
    private TextView changepayee;
    private TextView txtUserName;

    private EditText editAmount;
    private EditText editComment;

    private Button btnDuesFood;
    private Button btnDuesDrinks;
    private Button btnDuesMovies;
    private Button btnDuesOuting;
    private Button btnDuesPersonal;
    private Button btnDuesOthers;

    private RelativeLayout layDuesDrinks;
    private RelativeLayout layDuesFood;
    private RelativeLayout layDuesMovies;
    private RelativeLayout layDuesOthers;
    private RelativeLayout layDuesOuting;
    private RelativeLayout layDuesPersonal;
    private RelativeLayout other_layout;
    private TextView other_text;

    private LinearLayout profileBottomLayout;

    private ToggleButton toggleButtonedit;

    private ListView listSharedWith;

    private TextView txtTotalAmount;

    private String edittexttotalamount;

    private ArrayList<String> duesAmount;
    private ArrayList<String> duesId = new ArrayList<>();
    private ArrayList<String> duesImage;
    private ArrayList<String> duesName;
    private ArrayList<String> duesNumber;

    private boolean is_amount_item_changed = false;
    private boolean is_payee_changed = false;
    private boolean is_toggle = true;

    private String strAmount;
    private String strComment;
    private String strGroupId;
    private String strGroupImage;
    private String strGroupTitle;
    private String strDueType;
    private String strExpID;
    private String strFromActivity;
    private String strRefType;
    private String strType;

    int update_position;

    private String PayeeNo;
    private String add_dues_shared_amount;

    //FireBase Database
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userdataReference;
    private DatabaseReference duesdataReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public Users usr = new Users();
    public static String userName;
    public static String userPhone;
    public static String payeeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_due);

        firebaseDatabase = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String uid = user.getUid();

        userdataReference = firebaseDatabase.getReference().child("Users").child(uid);
        duesdataReference = firebaseDatabase.getReference().child("Dues").child(uid);

        userdataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usr = dataSnapshot.getValue(Users.class);

                userName = usr.getUsername();
                userPhone = usr.getPhoneNumber();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        strFromActivity = getIntent().getExtras().getString("fromActivity");

        strAmount = getIntent().getExtras().getString(AddDuesActivity.amount_key);
        strComment = getIntent().getExtras().getString(AddDuesActivity.comment_key);
        strDueType = getIntent().getExtras().getString(AddDuesActivity.duetype_key);
        strGroupId = getIntent().getExtras().getString(AddDuesActivity.groupID_key);
        strExpID = getIntent().getExtras().getString("ExpId");
        strGroupTitle = getIntent().getExtras().getString(AddDuesActivity.groupName_key);
        strGroupImage = getIntent().getExtras().getString(AddDuesActivity.groupImage_key);

        btnDuesCross = (ImageButton) findViewById(R.id.btn_cross);
        btnDuesCross.setOnClickListener(this);

        txtUserName = (TextView) findViewById(R.id.txtUserName);

        btnDuesDone = (Button) findViewById(R.id.btn_done);
        btnDuesDone.setOnClickListener(this);

        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        txtTotalAmount.setText(String.valueOf(Double.parseDouble(strAmount.replace(",", ""))));

        imgProfile = (SimpleDraweeView) findViewById(R.id.imgProfile);

        profileBottomLayout = (LinearLayout) findViewById(R.id.profileBottomLayout);
        other_layout = (RelativeLayout) findViewById(R.id.other_layout);

        editAmount = (EditText) findViewById(R.id.editAmount);
        editAmount.setText(String.valueOf(Double.parseDouble(strAmount.replace(",", ""))));
        edittexttotalamount = String.valueOf(Double.parseDouble(strAmount.replace(",", "")));
        editAmount.setVisibility(View.VISIBLE);

        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!is_amount_item_changed && !editAmount.getText().toString().equalsIgnoreCase(strAmount)
                        && editAmount.getText().toString().length() > 0
                        && !editAmount.getText().toString().equalsIgnoreCase(".")) {
                    Float amtDivide = Float.valueOf(editAmount.getText().toString()) / ((float) duesName.size());
                    duesAmount.clear();
                    for (int count = 0; count < duesName.size(); count++) {
                        duesAmount.add("" + amtDivide);
                    }
                    strAmount = editAmount.getText().toString();
                    setAdapter(is_toggle);
                    txtTotalAmount.setText(String.valueOf(Double.parseDouble(strAmount)));
                    edittexttotalamount = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
                    setAdapter(toggleButtonedit.isChecked());
                    is_toggle = true;
                    return;
                }
                setAdapter(toggleButtonedit.isChecked());
                is_toggle = false;
            }
        });

        editComment = (EditText) findViewById(R.id.editComment);
        editComment.setText(strComment);

        layDuesFood = (RelativeLayout) findViewById(R.id.layDuesFood);
        layDuesDrinks = (RelativeLayout) findViewById(R.id.layDuesDrinks);
        layDuesMovies = (RelativeLayout) findViewById(R.id.layDuesMovies);
        layDuesPersonal = (RelativeLayout) findViewById(R.id.layDuesPersonal);
        layDuesOthers = (RelativeLayout) findViewById(R.id.layDuesOthers);
        layDuesOuting = (RelativeLayout) findViewById(R.id.layDuesOuting);
        other_text = (TextView) findViewById(R.id.other_text);

        changepayee = (TextView) findViewById(R.id.changepayee);
        changepayee.setOnClickListener(this);

        btnDuesFood = (Button) findViewById(R.id.btnDuesFood);
        btnDuesFood.setOnClickListener(this);

        btnDuesDrinks = (Button) findViewById(R.id.btnDuesDrinks);
        btnDuesDrinks.setOnClickListener(this);

        btnDuesMovies = (Button) findViewById(R.id.btnDuesMovies);
        btnDuesMovies.setOnClickListener(this);

        btnDuesOuting = (Button) findViewById(R.id.btnDuesOuting);
        btnDuesOuting.setOnClickListener(this);

        btnDuesPersonal = (Button) findViewById(R.id.btnDuesPersonal);
        btnDuesPersonal.setOnClickListener(this);

        btnDuesOthers = (Button) findViewById(R.id.btnDuesOthers);
        btnDuesOthers.setOnClickListener(this);

        duesAmount = new ArrayList<>();
        duesName = new ArrayList<>();
        duesNumber = new ArrayList<>();
        duesImage = new ArrayList<>();

        for (int i = 0; i < GlobalVariables.splitContactNumber.size(); i++) {
            duesName.add(GlobalVariables.splitContactName.get(i));
            duesNumber.add(GlobalVariables.splitContactNumber.get(i));
            duesImage.add(GlobalVariables.splitContactImage.get(i));
            if (strFromActivity.contains("Update")) {
                duesId.add(GlobalVariables.splitContactId.get(i));
            }
        }

        txtUserName.setText("You paid");
        payeeType = "iGET";

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) other_layout.getLayoutParams();
        lp.setMargins(15, 15, 15, 15);
        other_layout.setLayoutParams(lp);

        if (strFromActivity.contains("Dues")) {
            if (strDueType.equalsIgnoreCase("Food"))
                changeBackgroundDues(0);
            else if (strDueType.equalsIgnoreCase("Drink"))
                changeBackgroundDues(1);
            else if (strDueType.equalsIgnoreCase("Movies"))
                changeBackgroundDues(2);
            else if (strDueType.equalsIgnoreCase("Outing"))
                changeBackgroundDues(3);
            else if (strDueType.equalsIgnoreCase("Personal"))
                changeBackgroundDues(4);
            else {
                other_text.setText(strDueType);
                changeBackgroundDues(5);
            }
        } else {

            profileBottomLayout.setVisibility(View.VISIBLE);
            if (strDueType.equalsIgnoreCase("Food")) {
                changeBackgroundDues(0);
            } else if (strDueType.equalsIgnoreCase("Drink")) {
                changeBackgroundDues(1);
            } else if (strDueType.equalsIgnoreCase("Movies")) {
                changeBackgroundDues(2);
            } else if (strDueType.equalsIgnoreCase("Outing")) {
                changeBackgroundDues(3);
            } else if (strDueType.equalsIgnoreCase("Personal")) {
                changeBackgroundDues(4);
            } else {
                other_text.setText(strDueType);
                changeBackgroundDues(5);
            }
        }

        if (strFromActivity.contains("Updates")) {
            for (int i = 0; i < GlobalVariables.splitContactAmount.size(); i++) {
                duesAmount.add(GlobalVariables.splitContactAmount.get(i));
            }
        } else {
            Float amtDivide = Float.valueOf(strAmount) / ((float) duesName.size());
            for (int i = 0; i < duesName.size(); i++) {
                duesAmount.add("" + amtDivide);
            }
        }
        listSharedWith = (ListView) findViewById(R.id.listSharedWith);
        setAdapter(is_toggle);


    }

    public void setAdapter(Boolean is_toggle) {
        listSharedWith.setAdapter(new SplitAmountListAdapter(this, duesName, duesNumber, duesImage, duesAmount, is_toggle, (SplitAmountListAdapter.DataTransferInterface) this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.changepayee:
                showAlertDialog(this, txtUserName, imgProfile);
                break;

            case R.id.btnDuesFood:
                layDuesFood.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(0);
                strDueType = "Food";
                break;

            case R.id.btnDuesDrinks:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(0);
                strDueType = "Drinks";
                break;

            case R.id.btnDuesMovies:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(0);
                strDueType = "Movies";
                break;

            case R.id.btnDuesOuting:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(0);
                strDueType = "outing";
                break;

            case R.id.btnDuesPersonal:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                layDuesOthers.setBackgroundColor(0);
                strDueType = "Personal";
                break;

            case R.id.btnDuesOthers:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
                    Toast.makeText(this, "Amount splitted doesn't match with the Total Amount", Toast.LENGTH_SHORT).show();
                } else if (String.valueOf(Double.parseDouble(value)).equalsIgnoreCase("0.00") || String.valueOf(Double.parseDouble(value)).equalsIgnoreCase(".00")) {
                    Toast.makeText(this, "Amount should be greater than Rs 0.00", Toast.LENGTH_SHORT).show();
                } else {
                    addDuesIntent();
                }

                break;

        }
    }

    private void changeBackgroundDues(int value) {
        initDues();
        switch (value) {
            case 0:
                layDuesFood.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            case 1:
                this.layDuesDrinks.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            case 2:
                this.layDuesMovies.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            case 3:
                this.layDuesOuting.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            case 4:
                this.layDuesPersonal.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            case 5:
                this.layDuesOthers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void initDues() {
        layDuesFood.setBackgroundColor(0);
        layDuesDrinks.setBackgroundColor(0);
        layDuesMovies.setBackgroundColor(0);
        layDuesOuting.setBackgroundColor(0);
        layDuesPersonal.setBackgroundColor(0);
        layDuesOthers.setBackgroundColor(0);
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
                    usrname = ContactManager.getInstance().getName(duesNumber.get(i));
                    payeeType = "iPAY";
                    if (usrname.isEmpty()) {
                        usrname = duesNumber.get(i);
                    }
                }

                if (duesImage.get(i).isEmpty()) {
                    String strImageUri = ContactManager.getInstance().getImage(duesNumber.get(i));

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
                txtUserName.setText(usrname + "Paid");
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


    }
}
