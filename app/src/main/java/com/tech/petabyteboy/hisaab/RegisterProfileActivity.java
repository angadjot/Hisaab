package com.tech.petabyteboy.hisaab;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private DatePickerDialog DatePicker;

    private Button btn_done;
    private Button btn_later;
    private SimpleDateFormat dateFormatter;

    private EditText editEmail;
    private AutoCompleteTextView txtCountry;
    private TextView txt_dob;
    private TextView txt_gender;

    public static String strPhoneNo;
    public static String strUserName;
    public static String strDOB;
    public static String strGender;
    public static String strCity;
    public static String strEmailID;
    public static String strPassword;
    public static Uri imgProfile;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userDataRef;
    private DatabaseReference regUserDataRef;
    private FirebaseStorage storage;
    private StorageReference storeRef;

    private static final String TAG = "Hisaab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        strPhoneNo = getIntent().getExtras().getString(RegisterActivity.phonenumber_key);
        strUserName = getIntent().getExtras().getString(RegisterActivity.username_key);
        strPassword = getIntent().getExtras().getString(RegisterActivity.userID_key);

        Log.e("UserID", strPassword);

        if (RegisterActivity.flag_camera || RegisterActivity.flag_gallery) {
            imgProfile = Uri.parse(getIntent().getExtras().getString(RegisterActivity.imgProfile_key));
        }

        txt_dob = (TextView) findViewById(R.id.txt_dob);
        txt_dob.setOnClickListener(this);

        txt_gender = (TextView) findViewById(R.id.txt_gender);
        txt_gender.setOnClickListener(this);

        txtCountry = (AutoCompleteTextView) findViewById(R.id.txtCountry);

        editEmail = (EditText) findViewById(R.id.editEmail);

        btn_later = (Button) findViewById(R.id.btn_later);
        btn_later.setOnClickListener(this);

        btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        SelectDate();

        mAuth = FirebaseAuth.getInstance();

        userDataRef = firebaseDatabase.getReference();

        regUserDataRef = firebaseDatabase.getReference().child("Registered Users List");

        storage = FirebaseStorage.getInstance();
        storeRef =  storage.getReference("Profile Pics");

    }

    @Override
    public void onBackPressed() {
        hideKeyBoard(RegisterProfileActivity.this);
    }

    public static void hideKeyBoard(Activity mActivity) {
        if (mActivity != null) {
            InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View v = mActivity.getCurrentFocus();
            if (v != null) {
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 2);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_later:
                if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    Toast.makeText(this, "Please Enter Email ID.", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    strEmailID = editEmail.getText().toString();
                    createAccountLater(strEmailID, strPassword);
                    signIn(strEmailID, strPassword);
                }
                break;

            case R.id.btn_done:
                if (checkFields()) {
                    strCity = txtCountry.getText().toString();
                    strEmailID = editEmail.getText().toString();
                    createAccount(strEmailID, strPassword);
                    signIn(strEmailID, strPassword);
                    break;
                } else
                    break;

            case R.id.txt_gender:
                showSelectGenderDialog(this);
                break;
            case R.id.txt_dob:
                DatePicker.show();
                break;

        }
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
                strDOB = str_showDate;
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
                strGender = "Male";
                txt_gender.setText("Male");
            }
        });
        ((Button) dialog.findViewById(R.id.btnFemale)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                strGender = "Female";
                txt_gender.setText("Female");
            }
        });
        ((Button) dialog.findViewById(R.id.btnDialogOther)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                strGender = "Other";
                txt_gender.setText("Other");
            }
        });
    }

    private boolean checkFields() {

        if (TextUtils.isEmpty(txt_dob.getText().toString())
                || TextUtils.isEmpty(txt_gender.getText().toString())
                || txt_gender.getText().toString().equalsIgnoreCase("Gender")
                || TextUtils.isEmpty(txtCountry.getText().toString())
                || txtCountry.getText().toString().equalsIgnoreCase("City")
                || TextUtils.isEmpty(editEmail.getText().toString())) {
            Toast.makeText(this, "Please Enter any of detail and press Done.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createAccountLater(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterProfileActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = task.getResult().getUser();
                            createUserDatabaseLater(user);
                            createRegisterDatabase(user);
                            if (imgProfile != null)
                                uploadProfilePic(user);
                        }

                    }
                });

    }

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterProfileActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = task.getResult().getUser();
                            createUserDatabase(user);
                            createRegisterDatabase(user);
                            if (imgProfile != null)
                                uploadProfilePic(user);
                        }

                    }
                });

    }

    private void createUserDatabaseLater(FirebaseUser user) {
        String uid = user.getUid();

        Users usr = new Users(strUserName, strPhoneNo, strEmailID);
        userDataRef.child("Users").child(uid).setValue(usr);

    }
    
    private void createRegisterDatabase(FirebaseUser user){
        
        String uid = user.getUid();

        regUserDataRef.child(strPhoneNo);
        regUserDataRef.child(strPhoneNo).child("Name").setValue(strUserName);
        regUserDataRef.child(strPhoneNo).child("User ID").setValue(uid);
        
        
    }

    private void createUserDatabase(FirebaseUser user) {
        String uid = user.getUid();

        Users usr = new Users(strUserName, strPhoneNo, strDOB, strGender, strCity, strEmailID);
        userDataRef.child("Users").child(uid).setValue(usr);

    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(RegisterProfileActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }

                    }
                });
    }

    private void uploadProfilePic(FirebaseUser user){
        String uid = user.getUid();

        storeRef.child(uid).child("ProfilePic").putFile(imgProfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Profile Pic Uploaded Link:"+taskSnapshot.getMetadata().getDownloadUrl(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
