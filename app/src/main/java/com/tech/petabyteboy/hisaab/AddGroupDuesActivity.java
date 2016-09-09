package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.petabyteboy.hisaab.Adapters.DuesSharedWithListAdapter;
import com.tech.petabyteboy.hisaab.Global.HelperClass;
import com.tech.petabyteboy.hisaab.Models.DuesSharedWithModel;
import com.tech.petabyteboy.hisaab.Models.GroupModel;
import com.tech.petabyteboy.hisaab.Models.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddGroupDuesActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editAmount;
    private EditText editComment;

    private TextView other_text;

    private RelativeLayout layDrinks;
    private RelativeLayout layFood;
    private RelativeLayout layMovies;
    private RelativeLayout layOthers;
    private RelativeLayout layOuting;
    private RelativeLayout layPersonal;

    private GridView gridSelectedContact;

    private String strDueType;

    // key Values for Intent and Request Code
    public static final String dueName_key = "duename";
    public static final String dueImage_key = "dueImage";
    public static final String amount_key = "amount";
    public static final String comment_key = "comment";
    public static final String duetype_key = "duetype";

    public static int REQUEST_SELECT_CONTACT = 0;

    public static final int REQUEST_CODE_ADD_DUES = 10;

    // Custom BaseAdapter for GridView (Contacts)
    private DuesSharedWithListAdapter adapter;

    public static UserModel User;
    public static String UserID;
    private String strDuesName;
    private String strDuesImage;
    private String strDuesComment;
    private GroupModel groupModel;

    private String TAG = "AddGroupDuesActivity";

    private List<String> memberNumbers;
    private List<String> memberNames;
    private List<String> memberImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dues_detail);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences userPref = getSharedPreferences(RegisterActivity.PREF_NAME, MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + userPref.getString("phone", null));
        UserID = userPref.getString("phone", null);

        strDuesName = getIntent().getExtras().getString(dueName_key);
        strDuesImage = getIntent().getExtras().getString(dueImage_key);
        strDuesComment = getIntent().getExtras().getString(comment_key);

        groupModel = GroupDetailActivity.groupModel;

        memberNumbers = Arrays.asList(groupModel.getGroupMemberNumber().split(","));
        memberNames = Arrays.asList(groupModel.getGroupMemberName().split(","));
        memberImages = Arrays.asList(groupModel.getGroupMemberImage().split(","));

        DatabaseReference userdataReference = firebaseDatabase.getReference().child("Users").child(UserID);

        userdataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initCreate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initCreate(){
        Toolbar toolbar_add_dues = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_add_dues);

        ImageButton btn_cross = (ImageButton) findViewById(R.id.btn_cross);
        btn_cross.setOnClickListener(this);

        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText(strDuesName);

        editAmount = (EditText) findViewById(R.id.editAmount);
        editComment = (EditText) findViewById(R.id.editComment);

        if (!strDuesComment.isEmpty() || !strDuesComment.equalsIgnoreCase("")){
            editComment.setText(strDuesComment);
        }

        SimpleDraweeView imgDue = (SimpleDraweeView) findViewById(R.id.imgDue);

        if (strDuesImage.isEmpty() || strDuesImage.equalsIgnoreCase("")){
            Uri uri = new Uri.Builder().scheme("res") // "res"
                    .path(String.valueOf(R.drawable.icon_group)).build();
            imgDue.setImageURI(uri);
        }else {
            imgDue.setImageURI(strDuesImage);
        }

        other_text = (TextView) findViewById(R.id.other_text);
        other_text.setText(R.string.add_others);

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

        layFood = (RelativeLayout) findViewById(R.id.layFood);
        layDrinks = (RelativeLayout) findViewById(R.id.layDrinks);
        layMovies = (RelativeLayout) findViewById(R.id.layMovies);
        layPersonal = (RelativeLayout) findViewById(R.id.layPersonal);
        layOthers = (RelativeLayout) findViewById(R.id.layOthers);
        layOuting = (RelativeLayout) findViewById(R.id.layOuting);

        gridSelectedContact = (GridView) findViewById(R.id.gridSelectedContact);

        getScale();

        init();
    }
    private void init() {

            HelperClass.data = initGridButton();
            getScale();
            adapter = new DuesSharedWithListAdapter(getApplicationContext());
            gridSelectedContact.setAdapter(adapter);

    }

    private ArrayList<DuesSharedWithModel> initGridButton() {

        ArrayList<DuesSharedWithModel> galleryList = new ArrayList<>();

        for(int i = 0;i < memberNumbers.size(); i++){

            DuesSharedWithModel model = new DuesSharedWithModel();

            model.setAddBtn("no");
            model.setName(memberNames.get(i));
            model.setNumber(memberNumbers.get(i));
            model.setImage(memberImages.get(i));
            model.setSeleted(true);

            galleryList.add(model);

            Collections.reverse(galleryList);

        }

        return galleryList;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_cross:
                finish();
                HelperClass.data.clear();
                break;

            case R.id.btn_next:
                AddDuesIntent();
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

        }
    }

    public void getScale() {
        getWindowManager().getDefaultDisplay().getSize(new Point());
        gridSelectedContact.setNumColumns(4);
    }

    public void AddDuesIntent() {
        String strAmount = editAmount.getText().toString();
        String strComment = editComment.getText().toString();

        Log.e(TAG, "Amount : " + strAmount + " Comment : " + strComment);

        if (TextUtils.isEmpty(strAmount)) {
            Toast.makeText(getApplicationContext(), "Please fill all the required details to add Dues", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(strDueType)) {
            Toast.makeText(getApplicationContext(), "Please select the type of Due.", Toast.LENGTH_SHORT).show();
            return;
        }

        HelperClass.splitContactAmount.clear();
        HelperClass.splitContactName.clear();
        HelperClass.splitContactNumber.clear();
        HelperClass.selectedContactList.clear();

        if (adapter == null || adapter.getSelected().size() <= 0) {
            Toast.makeText(getApplicationContext(), "In the shared with section, please select contacts with whom you want to share this dues!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<DuesSharedWithModel> selected = adapter.getSelected();

        for (int i = 0; i < selected.size(); i++) {
            HelperClass.splitContactName.add(selected.get(i).name);
            HelperClass.splitContactNumber.add(selected.get(i).number);
            HelperClass.splitContactImage.add(selected.get(i).image);
            HelperClass.selectedContactList.add(selected.get(i));

            Log.e(TAG, "Split Contact Name " + i + " : " + HelperClass.splitContactName.get(i) + "\n"
                    + "Split Contact Number " + i + " : " + HelperClass.splitContactNumber.get(i) + "\n"
                    + "Split Contact Image " + i + " : " + HelperClass.splitContactImage.get(i) + "\n"
                    + "Selected Contact \n"
                    + "Name " + i + " : " + HelperClass.selectedContactList.get(i).getName() + "\n"
                    + "Number " + i + " : " + HelperClass.selectedContactList.get(i).getNumber() + "\n"
                    + "Image " + i + " : " + HelperClass.selectedContactList.get(i).getImage() + "\n");
        }

        if (HelperClass.splitContactName.size() > 0) {
            Intent intent = new Intent(this, SplitDuesActivity.class);
            intent.putExtra(dueName_key,strDuesName);
            intent.putExtra(dueImage_key,strDuesImage);
            intent.putExtra(amount_key, strAmount);
            intent.putExtra(comment_key, strComment);
            intent.putExtra(duetype_key, strDueType);
            startActivityForResult(intent, REQUEST_CODE_ADD_DUES);
            return;
        }

        Toast.makeText(getApplicationContext(), "In the shared with section, please select contacts with whom you want to share this dues!", Toast.LENGTH_SHORT).show();
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
                if (editAddCategory.getText().toString().isEmpty()) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HelperClass.data.clear();
        finish();

    }

}
