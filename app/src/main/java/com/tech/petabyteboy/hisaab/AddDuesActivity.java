package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tech.petabyteboy.hisaab.Global.HelperClass;

import java.io.File;
import java.io.IOException;

public class AddDuesActivity extends AppCompatActivity implements View.OnClickListener {


    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 2;
    public static final String TEMP_PHOTO_FILE_NAME = "hisaab.jpg";

    private SimpleDraweeView imageDue;

    private EditText editDueName;
    private EditText editComment;

    private Bitmap DuesImage;
    private Uri outputFileUri = null;
    private Uri galleryUri = null;

    private static String TAG = "AddDuesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dues);

        ImageButton btn_cross = (ImageButton) findViewById(R.id.btn_cross);
        btn_cross.setOnClickListener(this);

        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        imageDue = (SimpleDraweeView) findViewById(R.id.imageDue);
        imageDue.setOnClickListener(this);

        editDueName = (EditText) findViewById(R.id.edit_due_name);

        editComment = (EditText) findViewById(R.id.input_comment);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_cross:
                finish();
                break;

            case R.id.btn_next:

                String strDuesName = editDueName.getText().toString();
                String strComment = editComment.getText().toString();

                Log.e(TAG, "Due Name : " + strDuesName + " Comment : " + strComment);

                if (TextUtils.isEmpty(strDuesName)) {
                    Toast.makeText(getApplicationContext(), "Please add Due Name and then Click Next !!", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent intent = new Intent(this, AddDuesDetailActivity.class);
                intent.putExtra(AddDuesDetailActivity.dueName_key, strDuesName);

                if (outputFileUri != null)
                    intent.putExtra(AddDuesDetailActivity.dueImage_key, outputFileUri.toString());
                else if (galleryUri != null)
                    intent.putExtra(AddDuesDetailActivity.dueImage_key, galleryUri.toString());
                else
                    intent.putExtra(AddDuesDetailActivity.dueImage_key, "");

                intent.putExtra(AddDuesDetailActivity.comment_key, strComment);
                startActivity(intent);
                break;

            case R.id.imageDue:
                showSelectProfilePicDialog(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            galleryUri = null;
            try {
                DuesImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DuesImage = HelperClass.scaleDown(DuesImage,500,true);
            outputFileUri = HelperClass.getImageUri(getApplicationContext(),DuesImage);
            imageDue.setImageURI(outputFileUri);
        }

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            galleryUri = null;
            outputFileUri = null;
            galleryUri = data.getData();
            try {
                DuesImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), galleryUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DuesImage = HelperClass.scaleDown(DuesImage,500,true);
            galleryUri = HelperClass.getImageUri(getApplicationContext(),DuesImage);
            imageDue.setImageURI(galleryUri);
        }

    }
}
