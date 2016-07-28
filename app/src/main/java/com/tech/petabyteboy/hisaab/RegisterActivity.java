package com.tech.petabyteboy.hisaab;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 2;
    public static final String TEMP_PHOTO_FILE_NAME = "hisaab.jpg";

    public static String strPhoneNo;
    public static String strUserName;

    private EditText editName;
    private EditText editPhoneNo;
    private SimpleDraweeView imgUserProfile;

    private String TAG = "RegisterActivity";

    public static final String username_key = "UserName";
    public static final String imgProfile_key = "ProfilePic";
    public static final String userID_key = "UserID";

    private Uri outputFileUri = null;
    private Uri galleryUri = null;

    DigitsAuthButton digitsButton;

    private String UserID;

    public static final String PREF_NAME = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imgUserProfile = (SimpleDraweeView) findViewById(R.id.imgUserProfile);
        editPhoneNo = (EditText) findViewById(R.id.editPhone);
        editName = (EditText) findViewById(R.id.editName);

        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        imgUserProfile.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        hideKeyBoard(RegisterActivity.this);
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
            case R.id.imgUserProfile:
                showSelectProfilePicDialog(this);
                break;

            case R.id.btn_register:

                strPhoneNo = editPhoneNo.getText().toString();
                strUserName = editName.getText().toString();

                if (strUserName.isEmpty() || strUserName.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Kindly enter all details", Toast.LENGTH_SHORT).show();
                    return;
                } else if (strPhoneNo.isEmpty() || strPhoneNo.length() != 10) {
                    Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SharedPreferences user_detail = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor user_edit = user_detail.edit();
                    user_edit.putString("phone", strPhoneNo);
                    Log.e(TAG, "Phone No : " + strPhoneNo);
                    user_edit.apply();
                    showVerifyOTPDialog(this);
                }
                break;
        }
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

        File mFileTemp;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if ("mounted".equals(Environment.getExternalStorageState())) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        outputFileUri = Uri.fromFile(mFileTemp);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            imgUserProfile.setImageURI(outputFileUri);
        }

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            galleryUri = data.getData();
            imgUserProfile.setImageURI(galleryUri);
        }

    }

    public void showVerifyOTPDialog(Context c) {

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_verify_otp);
        dialog.show();
        (dialog.findViewById(R.id.btnCross)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        digitsButton = (DigitsAuthButton) dialog.findViewById(R.id.auth_button);
        digitsButton.setCallback(createAuthCallback());
        digitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Digits.authenticate(createAuthCallback(), R.style.CustomDigitsTheme, "+91" + strPhoneNo, false);
                dialog.dismiss();
            }
        });
    }

    private AuthCallback createAuthCallback() {
        return new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                UserID = String.valueOf(session.getId());
                doIfSuccessfulOtpVerification();
            }

            @Override
            public void failure(DigitsException exception) {
                doIfNotSuccessfulOtpVerification();
            }
        };
    }

    private void doIfSuccessfulOtpVerification() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this, RegisterProfileActivity.class);
        intent.putExtra(username_key, strUserName);
        intent.putExtra(userID_key, UserID);

        if (outputFileUri != null)
            intent.putExtra(imgProfile_key, outputFileUri.toString());
        else if (galleryUri != null)
            intent.putExtra(imgProfile_key, galleryUri.toString());
        else
            intent.putExtra(imgProfile_key, "");

        startActivity(intent);
        finish();

    }

    private void doIfNotSuccessfulOtpVerification() {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
    }

}


