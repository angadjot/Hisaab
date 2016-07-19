package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AddDuesActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar_add_dues;

    private ImageButton btn_cross;
    private Button btn_next;

    private EditText editAmount;
    private EditText editComment;

    private TextView txtUserName;

    private TextView other_text;

    private Button btnDuesDrinks;
    private Button btnDuesFood;
    private Button btnDuesMovies;
    private Button btnDuesOthers;
    private Button btnDuesOuting;
    private Button btnDuesPersonal;

    private SimpleDraweeView imgUser;

    private RelativeLayout layDuesDrinks;
    private RelativeLayout layDuesFood;
    private RelativeLayout layDuesMovies;
    private RelativeLayout layDuesOthers;
    private RelativeLayout layDuesOuting;
    private RelativeLayout layDuesPersonal;

    private GridView gridSelectedContact;

    private String strDueType;
    private String passedFrom;

    // key Values for Intent and Request Code

    public static final String amount_key = "amount";
    public static final String comment_key = "comment";
    public static final String duetype_key = "duetype";
    public static final String groupID_key = "groupID";
    public static final String groupName_key = "groupName";
    public static final String groupImage_key = "groupImage";

    public static int REQUEST_SELECT_CONTACT = 0;

    public static final int REQUEST_CODE_ADD_DUES = 10;

    // Custom BaseAdapter for GridView (Contacts)
    private DuesSharedWithListAdapter adapter;

    private AdapterView.OnItemClickListener mItemMultiClickListener;

    public AddDuesActivity(){
        mItemMultiClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(getApplicationContext(), SelectContactActivity.class);
                    intent.putExtra("from", "dues");
                    startActivityForResult(intent, AddDuesActivity.REQUEST_SELECT_CONTACT);
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dues);

        toolbar_add_dues = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_add_dues);

        btn_cross = (ImageButton) findViewById(R.id.btn_cross);
        btn_cross.setOnClickListener(this);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText("You Paid");

        passedFrom = getIntent().getStringExtra("from");

        editAmount = (EditText) findViewById(R.id.editAmount);
        editComment = (EditText) findViewById(R.id.editComment);

        imgUser = (SimpleDraweeView) findViewById(R.id.imguser);

        other_text = (TextView) findViewById(R.id.other_text);
        other_text.setText(R.string.add_dues_others);

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

        layDuesFood = (RelativeLayout) findViewById(R.id.layDuesFood);
        layDuesDrinks = (RelativeLayout) findViewById(R.id.layDuesDrinks);
        layDuesMovies = (RelativeLayout) findViewById(R.id.layDuesMovies);
        layDuesPersonal = (RelativeLayout) findViewById(R.id.layDuesPersonal);
        layDuesOthers = (RelativeLayout) findViewById(R.id.layDuesOthers);
        layDuesOuting = (RelativeLayout) findViewById(R.id.layDuesOuting);

        gridSelectedContact = (GridView) findViewById(R.id.gridSelectedContact);

        getScale();

        init();
    }

    private void init() {

        if (adapter == null) {
            gridSelectedContact = (GridView) findViewById(R.id.gridSelectedContact);
            gridSelectedContact.setFastScrollEnabled(true);
            GlobalVariables.data = getGalleryPhotos();
            getScale();
            adapter = new DuesSharedWithListAdapter(getApplicationContext());
            gridSelectedContact.setOnItemClickListener(mItemMultiClickListener);
            gridSelectedContact.setAdapter(adapter);
        }
        adapter = new DuesSharedWithListAdapter(getApplication());
        gridSelectedContact.setOnItemClickListener(mItemMultiClickListener);
        gridSelectedContact.setAdapter(adapter);

    }

    private ArrayList<DuesSharedWithModel> getGalleryPhotos() {

        ArrayList<DuesSharedWithModel> galleryList = new ArrayList<>();

        DuesSharedWithModel model = new DuesSharedWithModel();
        model.name = "Select Contact";
        model.image = "Add";
        model.addBtn = "yes";
        model.id = "007007";
        model.isSeleted = false;
        galleryList.add(model);

        Collections.reverse(galleryList);

        return galleryList;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_cross:
                finish();
                break;

            case R.id.btn_next:
                AddDuesIntent();
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

        }
    }

    public void getScale() {
        getWindowManager().getDefaultDisplay().getSize(new Point());
        gridSelectedContact.setNumColumns(4);
    }

    public void AddDuesIntent() {
        String strAmount = this.editAmount.getText().toString();
        String strComment = this.editComment.getText().toString();

        if (TextUtils.isEmpty(strAmount)) {
            Toast.makeText(getApplicationContext(), "Please fill all the required details to add Dues", Toast.LENGTH_SHORT).show();
            return;
        }
        GlobalVariables.splitContactAmount.clear();
        GlobalVariables.splitContactName.clear();
        GlobalVariables.splitContactNumber.clear();

        if (adapter == null || adapter.getSelected().size() <= 0) {
            Toast.makeText(getApplicationContext(), "In the shared with section, please select contacts with whom you want to share this dues!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<DuesSharedWithModel> selected = adapter.getSelected();

        for (int i = 0; i < selected.size(); i++) {
            GlobalVariables.splitContactName.add(selected.get(i).name);
            GlobalVariables.splitContactImage.add(selected.get(i).image);
            GlobalVariables.splitContactNumber.add(selected.get(i).number);
        }

        if (GlobalVariables.splitContactName.size() > 0) {
            Intent intent = new Intent(this, SplitDueActivity.class);
            intent.putExtra("fromActivity", "Dues");
            intent.putExtra(amount_key, strAmount);
            intent.putExtra(comment_key, strComment);
            intent.putExtra(duetype_key, strDueType);
            intent.putExtra(groupID_key, "");
            intent.putExtra(groupName_key, "");
            intent.putExtra(groupImage_key, "");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        GlobalVariables.data.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_DUES && SplitDueActivity.REQUEST_Split_Code == 1){
            finish();
        }
        if (requestCode == REQUEST_SELECT_CONTACT)
            init();
    }
}
