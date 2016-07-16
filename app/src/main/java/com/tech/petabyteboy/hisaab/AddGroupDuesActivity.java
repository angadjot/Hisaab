package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

public class AddGroupDuesActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar_add_dues;

    private ImageButton btn_cross;
    private Button btn_next;

    private EditText editAmount;
    private EditText editComment;

    private TextView txtUserName;

    private TextView other_text;

    private Button btnBack;
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

    private String strEventType;

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
        strEventType = "Food";

        gridSelectedContact = (GridView) findViewById(R.id.gridSelectedContact);

        getScale();
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
                layDuesFood.setBackgroundColor(Color.parseColor("#0ecb91"));
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(0);
                strEventType = "Food";
                break;

            case R.id.btnDuesDrinks:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(Color.parseColor("#0ecb91"));
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(0);
                strEventType = "Drinks";
                break;

            case R.id.btnDuesMovies:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(Color.parseColor("#0ecb91"));
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(0);
                strEventType = "Movies";
                break;

            case R.id.btnDuesOuting:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(Color.parseColor("#0ecb91"));
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(0);
                strEventType = "outing";
                break;

            case R.id.btnDuesPersonal:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(Color.parseColor("#0ecb91"));
                layDuesOthers.setBackgroundColor(0);
                strEventType = "Personal";
                break;

            case R.id.btnDuesOthers:
                layDuesFood.setBackgroundColor(0);
                layDuesDrinks.setBackgroundColor(0);
                layDuesMovies.setBackgroundColor(0);
                layDuesOuting.setBackgroundColor(0);
                layDuesPersonal.setBackgroundColor(0);
                layDuesOthers.setBackgroundColor(Color.parseColor("#0ecb91"));
                strEventType = "Others";
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
                strEventType = editAddCategory.getText().toString();
                other_text.setText(strEventType);
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
    }
}
