package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.petabyteboy.hisaab.Adapters.DuesSharedWithListAdapter;
import com.tech.petabyteboy.hisaab.Global.GlobalVariables;
import com.tech.petabyteboy.hisaab.Models.DuesSharedWithModel;
import com.tech.petabyteboy.hisaab.Models.UserModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class AddGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar_add_group;

    private EditText editComment;
    private EditText editGroupName;

    private String strGroupComment;
    private String strGroupId;
    private String strGroupImage;
    private String strGroupName;

    private File mFileTemp;

    private GridView gridSelectedContatcts;

    private SimpleDraweeView imageGroup;

    public int REQUEST_SELECT_CONTACT = 0;

    public static final int RESPONSE_CODE_ADDGROUP = 0;
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    public static final String TEMP_PHOTO_FILE_NAME = "hisaab.jpg";

    // Custom BaseAdapter for GridView (Contacts)
    private DuesSharedWithListAdapter adapter;
    private AdapterView.OnItemClickListener mItemMultiClickListener;

    private static UserModel User;
    private static String UserID;
    private String strUserImage;

    private String TAG = "AddGroupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences userPref = getSharedPreferences(RegisterActivity.PREF_NAME, MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + userPref.getString("phone", null));
        UserID = userPref.getString("phone", null);

        DatabaseReference groupsdataRef = firebaseDatabase.getReference().child("Groups").child(UserID);

        DatabaseReference userdataReference = firebaseDatabase.getReference().child("Users").child(UserID);

        userdataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User = dataSnapshot.getValue(UserModel.class);
                strUserImage = User.getImage();
                Log.e(TAG, "Image : " + strUserImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        toolbar_add_group = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_add_group);

        Button btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        ImageButton btnCross = (ImageButton) findViewById(R.id.btn_cross);
        btnCross.setOnClickListener(this);

        editGroupName = (EditText) findViewById(R.id.edit_group_name);
        editComment = (EditText) findViewById(R.id.input_comment);

        imageGroup = (SimpleDraweeView) findViewById(R.id.imageGroup);
        imageGroup.setOnClickListener(this);

        mItemMultiClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, REQUEST_SELECT_CONTACT);
                }
            }
        };

        gridSelectedContatcts = (GridView) findViewById(R.id.gridSelectedContatcts);

        getScale();

        init();

        editGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (editGroupName.getText().toString().length() > 0) {
                    editGroupName.setTypeface(editGroupName.getTypeface(), 1);
                } else {
                    editGroupName.setTypeface(editGroupName.getTypeface(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        if ("mounted".equals(Environment.getExternalStorageState())) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }

    private void init() {

        if (adapter == null) {
            GlobalVariables.data = initGridButton();
            getScale();
            adapter = new DuesSharedWithListAdapter(getApplicationContext());
            gridSelectedContatcts.setOnItemClickListener(mItemMultiClickListener);
            gridSelectedContatcts.setAdapter(adapter);
            return;
        }
        adapter = new DuesSharedWithListAdapter(getApplication());
        gridSelectedContatcts.setOnItemClickListener(mItemMultiClickListener);
        gridSelectedContatcts.setAdapter(adapter);

    }

    private ArrayList<DuesSharedWithModel> initGridButton() {

        ArrayList<DuesSharedWithModel> galleryList = new ArrayList<>();

        DuesSharedWithModel model = new DuesSharedWithModel();
        model.name = "Select Contact";
        model.number = "Add";
        model.image = "Add";
        model.addBtn = "yes";
        model.isSeleted = false;
        galleryList.add(model);

        Collections.reverse(galleryList);

        return galleryList;
    }

    public void getScale() {
        getWindowManager().getDefaultDisplay().getSize(new Point());
        gridSelectedContatcts.setNumColumns(4);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_cross:
                GlobalVariables.groupContactName.clear();
                GlobalVariables.groupContactImage.clear();
                GlobalVariables.groupContactNumber.clear();
                finish();
                break;

            case R.id.btn_done:
                AddGroupIntent();
                break;

            case R.id.imageGroup:
                showSelectImageDialog(this);
                break;

        }
    }

    public void showSelectImageDialog(Context c) {

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
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Hisaab" + File.separator + "Hisaab.jpg";
    }

    private void capturePhoto() {

        Uri outputFileUri = Uri.fromFile(new File(getDefaultImagePath()));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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

        Uri imageUri = Uri.parse(getDefaultImagePath());
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageGroup.setImageURI(imageUri);
        }

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            imageGroup.setImageURI(imageUri);
        }

        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {

            String name = null;
            String phoneNo = null;
            String image_thumb = null;

            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, "display_name ASC");
            while (cursor.moveToNext()) {
                String contactid = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                image_thumb = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                if ((Integer.parseInt(hasPhone)) > 0) {
                    Cursor cur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactid, null, null);

                    while (cur.moveToNext()) {
                        phoneNo = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                }
            }
            cursor.close();

            if (phoneNo.contains(" ")) {
                phoneNo = phoneNo.replace(" ", "");
            }
            if (phoneNo.contains("-")) {
                phoneNo = phoneNo.replace("-", "");
            }
            if (phoneNo.length() >= 10) {
                if (phoneNo.contains("-")) {
                    phoneNo = phoneNo.substring(phoneNo.length() - 10, phoneNo.length());
                    if (phoneNo.startsWith("0")) {
                        phoneNo = phoneNo.substring(1);
                    }
                } else {
                    phoneNo = phoneNo.substring(phoneNo.length() - 10, phoneNo.length());
                    if (phoneNo.startsWith("0")) {
                        phoneNo = phoneNo.substring(1);
                    }
                }
            }

            Log.e(TAG, "Phone No : " + phoneNo);

            Boolean isNumberAddedBefore = false;

            DuesSharedWithModel model = new DuesSharedWithModel();

            model.setAddBtn("no");
            model.setName(name);
            model.setNumber(phoneNo);
            model.setImage(image_thumb);
            model.setSeleted(true);

            Log.e(TAG, "Contact Name : " + model.getName() + "\n Contact Number : " + model.getNumber()
                    + "\n Contact Image : " + model.getImage() + "\n Contact AddBtn : " + model.getAddBtn()
                    + "\n Contact Selected : " + model.isSeleted());

            for (int i = 0; i < GlobalVariables.data.size(); i++) {
                DuesSharedWithModel newModel = GlobalVariables.data.get(i);
                if (newModel.number != null && model.number.equalsIgnoreCase(newModel.number)) {
                    Toast.makeText(this, "Contact Already Added!!", Toast.LENGTH_SHORT).show();
                    isNumberAddedBefore = true;
                    break;
                }
            }

            if (!isNumberAddedBefore) {
                GlobalVariables.data.add(model);
            }
            init();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GlobalVariables.groupContactName.clear();
        GlobalVariables.groupContactNumber.clear();
        GlobalVariables.groupContactImage.clear();
        finish();
    }


    public static String trimFront(String input) {
        if (input == null) {
            return input;
        }
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isWhitespace(input.charAt(i))) {
                return input.substring(i);
            }
        }
        return "";
    }

    public void AddGroupIntent() {

        String title;
        strGroupName = editGroupName.getText().toString();
        strGroupComment = editComment.getText().toString();

        if (TextUtils.isEmpty(strGroupName)) {
            Toast.makeText(this, "Please add title of Group", Toast.LENGTH_SHORT).show();
            return;
        } else {
            finish();
        }

        if (strGroupName.contains(" ")) {
            title = strGroupName.replaceAll("\\s", "");
        } else {
            title = strGroupName;
        }

        if (title.length() != 0) {
            strGroupName = trimFront(strGroupName);

            StringBuilder sb = new StringBuilder();

            sb.append(UserID);

            ArrayList<DuesSharedWithModel> selected = adapter.getSelected();

            if (selected.size() > 0) {
                sb.append(",");
            }

            for (int i = 0; i < selected.size(); i++) {
                if ((selected.get(i)).number.length() <= 10) {
                    sb.append((selected.get(i)).number);
                    if (i != selected.size() - 1) {
                        sb.append(",");
                    }
                } else {
                    sb.append((selected.get(i)).number.substring((selected.get(i)).number.length() - 10));
                    if (i != selected.size() - 1) {
                        sb.append(",");
                    }
                }
            }

            Log.e("Add Group Data Size : ", String.valueOf(GlobalVariables.data.size()));

            String strNumber = String.valueOf(sb);

            UserID = strNumber;

            Log.e("Number String ", strNumber);

            String strDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

            if (strNumber.isEmpty() || strNumber.equalsIgnoreCase("")) {
                Toast.makeText(this, "Select the members with whom u want to share amount", Toast.LENGTH_SHORT).show();
                return;
            }

            //add Group in database

            return;
        }
        Toast.makeText(this, "Please add title of Group", Toast.LENGTH_SHORT).show();
    }

}
