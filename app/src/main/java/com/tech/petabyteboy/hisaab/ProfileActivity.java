package com.tech.petabyteboy.hisaab;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tech.petabyteboy.hisaab.Global.HelperClass;
import com.tech.petabyteboy.hisaab.Models.UserModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private DatePickerDialog DatePicker;

    private DatabaseReference userdataReference;
    private StorageReference storeRef;

    private EditText editName;
    private EditText editPhoneNo;
    private SimpleDraweeView imgUserProfile;
    private EditText editEmail;
    private AutoCompleteTextView txtCountry;
    private TextView txt_dob;
    private TextView txt_gender;
    private ProgressDialog progress;

    private SimpleDateFormat dateFormatter;

    public static String strPhoneNo;
    public static String strUserName;
    public static String strDOB;
    public static String strGender;
    public static String strCity;
    public static String strEmailID;
    public static String strImage;

    private Uri outputFileUri;

    private Bitmap profileImage;

    private UserModel User;

    private String TAG = "ProfileActivity";
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 2;
    public static final String TEMP_PHOTO_FILE_NAME = "hisaab.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        setTitle(R.string.profile_title);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences userPref = getSharedPreferences(RegisterActivity.PREF_NAME, MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + userPref.getString("phone", null));
        String UserPhone = userPref.getString("phone", null);

        editName = (EditText) findViewById(R.id.editUsername);
        editPhoneNo = (EditText) findViewById(R.id.MobileNumber);

        txt_dob = (TextView) findViewById(R.id.dateofbirth);
        txt_dob.setOnClickListener(this);

        txt_gender = (TextView) findViewById(R.id.gender);
        txt_gender.setOnClickListener(this);

        txtCountry = (AutoCompleteTextView) findViewById(R.id.City);

        editEmail = (EditText) findViewById(R.id.EmailID);

        ImageView btnEdit = (ImageView) findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);

        Button btndone = (Button) findViewById(R.id.btn_done);
        btndone.setOnClickListener(this);

        imgUserProfile = (SimpleDraweeView) findViewById(R.id.imgUserProfile);
        imgUserProfile.setOnClickListener(this);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        SelectDate();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storeRef = storage.getReference("Profile Pics");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        if (UserPhone != null)
            userdataReference = firebaseDatabase.getReference().child("Users").child(UserPhone);

        showUserDetail();

    }

    private void showUserDetail() {

        userdataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User = dataSnapshot.getValue(UserModel.class);

                strUserName = User.getUsername();
                strEmailID = User.getEmailID();
                strPhoneNo = User.getPhoneNumber();
                strDOB = User.getDateOfBirth();
                strCity = User.getCity();
                strGender = User.getGender();
                strImage = User.getImage();

                setValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setValues() {

        if (strUserName.isEmpty())
            editName.setText(R.string.username);
        else
            editName.setText(strUserName);

        if (strPhoneNo.isEmpty())
            editPhoneNo.setText(R.string.phone);
        else
            editPhoneNo.setText(strPhoneNo);

        if (strDOB.isEmpty() || strDOB.equalsIgnoreCase(""))
            txt_dob.setText(R.string.date_hint);
        else
            txt_dob.setText(strDOB);

        if (strGender.isEmpty() || strGender.equalsIgnoreCase(""))
            txt_gender.setText(R.string.gender_hint);
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

        Log.e(TAG, "Image Str : " + strImage);

        if (strImage.isEmpty() || strImage.equalsIgnoreCase("")) {
            Uri uri = new Uri.Builder().scheme("res") // "res"
                    .path(String.valueOf(R.drawable.profile_pic_home)).build();
            imgUserProfile.setImageURI(uri);
        } else {
            imgUserProfile.setImageURI(strImage);
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
        (dialog.findViewById(R.id.btnCross)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        (dialog.findViewById(R.id.btnMale)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                txt_gender.setText(R.string.dialog_male);
            }
        });
        (dialog.findViewById(R.id.btnFemale)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                txt_gender.setText(R.string.dialog_female);
            }
        });
        (dialog.findViewById(R.id.btnDialogOther)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                txt_gender.setText(R.string.dialog_other);
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

    public static String getDefaultImagePath() {
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Hisaab/";
        new File(dir).mkdirs();
        try {
            new File(dir + TEMP_PHOTO_FILE_NAME).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Hisaab" + File.separator + "hisaab.jpg";
    }

    private void capturePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = Uri.fromFile(new File(getDefaultImagePath()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        }
    }

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
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

    private void updateDatabase() {

        progress = new ProgressDialog(this);
        progress.setMessage("Uploading Details...\nPlease Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

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
        Log.e(TAG, "strImage : " + strImage);
        if (strImage != null && !strImage.equalsIgnoreCase("")) {
            userdataReference.child("image").setValue(strImage);
            storeRef.child(User.getUserID()).child("Images").putFile(Uri.parse(strImage)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri uri = taskSnapshot.getDownloadUrl();
                    Log.e(TAG, "Image URI : " + uri);
                    User.setImage(String.valueOf(uri));
                }
            });
        }

        userdataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG,"All Done");
                progress.dismiss();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            strImage = null;
            try {
                Log.e(TAG,"onActivityResult : outputFileUri : "+outputFileUri);
                profileImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),outputFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImage = HelperClass.scaleDown(profileImage,500,true);
            outputFileUri = HelperClass.getImageUri(getApplicationContext(),profileImage);
            imgUserProfile.setImageURI(outputFileUri);
            strImage = String.valueOf(outputFileUri);
        }

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            strImage = null;
            Uri galleryUri = data.getData();
            try {
                profileImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), galleryUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImage = HelperClass.scaleDown(profileImage,500,true);
            galleryUri = HelperClass.getImageUri(getApplicationContext(),profileImage);
            imgUserProfile.setImageURI(galleryUri);
            strImage = String.valueOf(galleryUri);
        }
    }
}
