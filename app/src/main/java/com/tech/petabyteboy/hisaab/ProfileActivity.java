package com.tech.petabyteboy.hisaab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private DatePickerDialog DatePicker;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userdataReference;
    private DatabaseReference duesdataReference;
    private FirebaseStorage storage;
    private StorageReference storeRef;

    private FirebaseAuth auth;
    private FirebaseUser user;


    private EditText editName;
    private EditText editPhoneNo;
    private SimpleDraweeView imgUserProfile;
    private EditText editEmail;
    private AutoCompleteTextView txtCountry;
    private TextView txt_dob;
    private TextView txt_gender;

    private ImageButton btn_back;
    private Button btndone;
    private ImageView btnEdit;

    private SimpleDateFormat dateFormatter;

    public static String strPhoneNo;
    public static String strUserName;
    public static String strDOB;
    public static String strGender;
    public static String strCity;
    public static String strEmailID;

    private Uri outputFileUri;
    private Uri galleryUri;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GET = 2;

    private Toolbar toolbar;

    private Users usr;

    private Boolean flag_valueChnaged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editName = (EditText) findViewById(R.id.editUsername);
        editPhoneNo = (EditText) findViewById(R.id.MobileNumber);

        txt_dob = (TextView) findViewById(R.id.dateofbirth);
        txt_dob.setOnClickListener(this);

        txt_gender = (TextView) findViewById(R.id.gender);
        txt_gender.setOnClickListener(this);

        txtCountry = (AutoCompleteTextView) findViewById(R.id.City);

        editEmail = (EditText) findViewById(R.id.EmailID);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        btnEdit = (ImageView) findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);

        btndone = (Button) findViewById(R.id.btn_done);
        btndone.setOnClickListener(this);

        imgUserProfile = (SimpleDraweeView) findViewById(R.id.imgUserProfile);
        imgUserProfile.setOnClickListener(this);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        SelectDate();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String uid = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();

        userdataReference = firebaseDatabase.getReference().child("Users").child(uid);

        showUserDetail();

    }

    private void showUserDetail() {

        userdataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usr = dataSnapshot.getValue(Users.class);

                strUserName = usr.getUsername();
                strEmailID = usr.getEmailID();
                strPhoneNo = usr.getPhoneNumber();
                strDOB = usr.getDateOfBirth();
                strCity = usr.getCity();
                strGender = usr.getGender();

                setValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setValues() {

        if (strUserName.isEmpty())
            editName.setText("Username");
        else
            editName.setText(strUserName);

        if (strPhoneNo.isEmpty())
            editPhoneNo.setText("Phone Number");
        else
            editPhoneNo.setText(strPhoneNo);

        if (strDOB.isEmpty())
            txt_dob.setText("01/01/1990");
        else
            txt_dob.setText(strDOB);

        if (strGender.isEmpty())
            txt_gender.setText("Gender");
        else
            txt_gender.setText(strGender);

        if (strCity.isEmpty())
            txtCountry.setText("City");
        else
            txtCountry.setText(strCity);

        if (strEmailID.isEmpty())
            editEmail.setText("emailid@domain.com");
        else
            editEmail.setText(strEmailID);

    }

    private void SelectDate() {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(Calendar.DAY_OF_MONTH, 1);
        newCalendar.set(Calendar.MONTH, 0);
        newCalendar.set(Calendar.YEAR, 1990);
        DatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String str_showDate = dateFormatter.format(newDate.getTime()).toString();
                txt_dob.setText(str_showDate);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        DatePicker.getDatePicker().setCalendarViewShown(false);
        DatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        DatePicker.setTitle("Select Date");
    }

    public void showSelectGenderDialog(Context c) {

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_select_gender);
        dialog.show();
        ((Button) dialog.findViewById(R.id.btnCross)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.btnMale)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                txt_gender.setText("Male");
            }
        });
        ((Button) dialog.findViewById(R.id.btnFemale)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                txt_gender.setText("Female");
            }
        });
        ((Button) dialog.findViewById(R.id.btnDialogOther)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                txt_gender.setText("Other");
            }
        });
    }

    public void showSelectProfilePicDialog(Context c) {

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_select_profile_pic);
        dialog.show();
        (dialog.findViewById(R.id.btnCross)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        (dialog.findViewById(R.id.btnCamera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
                dialog.dismiss();
            }
        });
        (dialog.findViewById(R.id.btnGallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                dialog.dismiss();
            }
        });
    }

    private void capturePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "imgProfile.jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_done:
                updateDatabase();
                finish();
                break;

            case R.id.btn_edit:
                editName.setClickable(true);
                editName.setFocusable(true);
                editName.setCursorVisible(true);
                editName.setFocusableInTouchMode(true);
                break;

            case R.id.imgUserProfile:
                showSelectProfilePicDialog(this);
                break;

            case R.id.gender:
                showSelectGenderDialog(this);
                break;
            case R.id.dateofbirth:
                DatePicker.show();
                break;
        }

    }

    private void updateDatabase(){

        if (!strUserName.equalsIgnoreCase(editName.getText().toString()))
            userdataReference.child("username").setValue(editName.getText().toString());
        if (!strPhoneNo.equalsIgnoreCase(editPhoneNo.getText().toString()))
            userdataReference.child("phoneNumber").setValue(editPhoneNo.getText().toString());
        if (!strDOB.equalsIgnoreCase(txt_dob.getText().toString()))
            userdataReference.child("dateOfBirth").setValue(txt_dob.getText().toString());
        if (!strGender.equalsIgnoreCase(txt_gender.getText().toString()))
            userdataReference.child("gender").setValue(txt_gender.getText().toString());
        if (!strCity.equalsIgnoreCase(txtCountry.getText().toString()))
            userdataReference.child("city").setValue(txtCountry.getText().toString());
        if (!strEmailID.equalsIgnoreCase(editEmail.getText().toString()))
            userdataReference.child("emailID").setValue(editEmail.getText().toString());
    }
}
