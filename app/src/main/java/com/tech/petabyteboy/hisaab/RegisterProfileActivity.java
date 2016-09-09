package com.tech.petabyteboy.hisaab;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.tech.petabyteboy.hisaab.Models.UserModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private DatePickerDialog DatePicker;

    private SimpleDateFormat dateFormatter;

    private EditText editEmail;
    private AutoCompleteTextView txtCountry;
    private TextView txt_dob;
    private TextView txt_gender;
    private ProgressDialog progress;

    public static String strPhoneNo;
    public static String strUserName;
    public static String strDOB;
    public static String strGender;
    public static String strCity;
    public static String strEmailID;
    public static String strPassword;
    public static String strImage;

    private FirebaseAuth mAuth;
    private DatabaseReference userDataRef;
    private StorageReference storeRef;

    private FirebaseUser User;

    private UserModel user;

    private static final String TAG = "RegisterProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        SharedPreferences userPref = getSharedPreferences(RegisterActivity.PREF_NAME, MODE_PRIVATE);

        strPhoneNo = userPref.getString("phone", null);
        strUserName = getIntent().getExtras().getString(RegisterActivity.username_key);
        strPassword = getIntent().getExtras().getString(RegisterActivity.userID_key);
        strImage = getIntent().getExtras().getString(RegisterActivity.imgProfile_key);

        Log.e(TAG, "\nstrPhoneNo : " + strPhoneNo + "\nstrUserName : " + strUserName + "\nstrPassWord : " + strPassword + "\nstrImage : " + strImage);

        user = new UserModel();
        user.setUsername(strUserName);
        user.setPhoneNumber(strPhoneNo);
        user.setUserID(strPhoneNo);
        if (!strImage.equalsIgnoreCase("") || !strImage.isEmpty()) {
            user.setImage(strImage);
        }

        txt_dob = (TextView) findViewById(R.id.txt_dob);
        txt_dob.setOnClickListener(this);

        txt_gender = (TextView) findViewById(R.id.txt_gender);
        txt_gender.setOnClickListener(this);

        txtCountry = (AutoCompleteTextView) findViewById(R.id.txtCountry);

        editEmail = (EditText) findViewById(R.id.editEmail);

        Button btn_later = (Button) findViewById(R.id.btn_later);
        btn_later.setOnClickListener(this);

        Button btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);

        progress = new ProgressDialog(this);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        SelectDate();

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        userDataRef = firebaseDatabase.getReference();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storeRef = storage.getReference("Profile Pics");

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
                RegisterUserLater();
                break;

            case R.id.btn_done:
                RegisterUser();
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
                String str_showDate = dateFormatter.format(newDate.getTime());
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
        dialog.findViewById(R.id.btnCross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnMale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                strGender = "Male";
                txt_gender.setText(strGender);
            }
        });
        dialog.findViewById(R.id.btnFemale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                strGender = "Female";
                txt_gender.setText(strGender);
            }
        });
        dialog.findViewById(R.id.btnDialogOther).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                strGender = "Other";
                txt_gender.setText(strGender);
            }
        });
    }

    private void RegisterUserLater() {

        strEmailID = editEmail.getText().toString();

        if (strEmailID.isEmpty() || strEmailID.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Enter any of detail and then press Later.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            user.setEmailID(strEmailID);
            Log.e(TAG, "EmailID : " + user.getEmailID());
        }

        mAuth.signInWithEmailAndPassword(strEmailID,strPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e(TAG, "Log in Successful");
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    LogInIntent();
                }else{
                    Log.e(TAG, "Authentication failed.Account Doesn't Exists");
                    progress.dismiss();
                    createAccount(strEmailID, strPassword);
                }
            }
        });

    }

    private void RegisterUser() {
        strDOB = txt_dob.getText().toString();
        strGender = txt_gender.getText().toString();
        strCity = txtCountry.getText().toString();
        strEmailID = editEmail.getText().toString();

        if (strDOB.isEmpty()) {
            Toast.makeText(this, "Please Enter all the detail and press Done.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            user.setDateOfBirth(strDOB);
        }


        if (strGender.isEmpty() || strGender.equalsIgnoreCase("Gender")) {
            Toast.makeText(this, "Please Enter all the detail and press Done.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            user.setGender(strGender);
        }

        if (strCity.isEmpty() || strCity.equalsIgnoreCase("City")) {
            Toast.makeText(this, "Please Enter all the detail and press Done.", Toast.LENGTH_SHORT).show();
            return;
        } else
            user.setCity(strCity);

        if (strEmailID.isEmpty()) {
            Toast.makeText(this, "Please Enter all the detail and press Done.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            user.setEmailID(strEmailID);
        }

        progress.setMessage("Checking Account...\nPlease Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        mAuth.signInWithEmailAndPassword(strEmailID,strPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e(TAG, "Log in Successful");
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    LogInIntent();
                }else{
                    Log.e(TAG, "Authentication failed.Account Doesn't Exists");
                    progress.dismiss();
                    createAccount(strEmailID, strPassword);
                }
            }
        });

    }

    private void createAccount(final String email, final String password) {

        progress.setMessage("Registering...\nPlease Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(TAG, "createUserWithEmail : onComplete : " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Authentication failed.");
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(),"Problem Creating Account...\nPlease Try again...",Toast.LENGTH_SHORT).show();
                        } else {
                            User = task.getResult().getUser();
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(),"Account Created Successfully.",Toast.LENGTH_SHORT).show();
                            signIn(email, password);
                        }

                    }
                });

    }

    private void signIn(String email, final String password) {

        progress.setMessage("Signing In...\nPlease Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.e(TAG, "signInWithEmail : ", task.getException());
                            Log.e(TAG, "Authentication failed.");
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(),"Authentication Problem. Please try again.",Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Log in Successful");
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            MainActivityIntent();
                        }
                    }

                });
    }

    private void MainActivityIntent() {

        progress.setMessage("Loading Data...\nPlease Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                User = firebaseAuth.getCurrentUser();
            }
        });

        if (User != null) {

            if (strImage != null && !strImage.equalsIgnoreCase("")) {
                storeRef.child(user.getUserID()).child("Images").putFile(Uri.parse(strImage)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri uri = taskSnapshot.getDownloadUrl();
                        Log.e(TAG, "Image URI : " + uri);
                        user.setImage(String.valueOf(uri));

                        Log.e(TAG, "Users Detail : \nUsername = " + user.getUsername() + "\nImage = " + user.getImage() +
                                "\nPhoneNumber = " + user.getPhoneNumber() + "\nGender = " + user.getGender() +
                                "\nEmailID = " + user.getEmailID() + "\nCity = " + user.getCity() +
                                "\nDateOfBirth = " + user.getDateOfBirth() + "\nUserID = " + user.getUserID());

                        userDataRef.child("Users").child(user.getUserID()).setValue(user);

                        progress.dismiss();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            else{

                Log.e(TAG, "Users Detail : \nUsername = " + user.getUsername() + "\nImage = " + user.getImage() +
                        "\nPhoneNumber = " + user.getPhoneNumber() + "\nGender = " + user.getGender() +
                        "\nEmailID = " + user.getEmailID() + "\nCity = " + user.getCity() +
                        "\nDateOfBirth = " + user.getDateOfBirth() + "\nUserID = " + user.getUserID());

                userDataRef.child("Users").child(user.getUserID()).setValue(user);

                progress.dismiss();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void LogInIntent() {

        progress.setMessage("Loading Data...\nPlease Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                User = firebaseAuth.getCurrentUser();
                Log.e(TAG,"User : "+User);

                if (User != null) {
                    progress.dismiss();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}
