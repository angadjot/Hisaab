package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.petabyteboy.hisaab.Adapters.DuesSharedWithListAdapter;
import com.tech.petabyteboy.hisaab.Global.HelperClass;
import com.tech.petabyteboy.hisaab.Models.DuesSharedWithModel;
import com.tech.petabyteboy.hisaab.Models.DuesModel;
import com.tech.petabyteboy.hisaab.Models.GroupModel;
import com.tech.petabyteboy.hisaab.Models.UserModel;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AddGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editComment;
    private EditText editGroupName;

    private String strGroupComment;
    private String strGroupImage;
    private String strGroupName;

    private Bitmap groupImage;
    private Uri outputFileUri;

    private GridView gridSelectedContacts;

    private SimpleDraweeView imageGroup;

    public int REQUEST_SELECT_CONTACT = 0;

    public static final int RESPONSE_CODE_ADDGROUP = 0;
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 2;
    public static final String TEMP_PHOTO_FILE_NAME = "hisaab.jpg";

    // Custom BaseAdapter for GridView (Contacts)
    private DuesSharedWithListAdapter adapter;
    private AdapterView.OnItemClickListener mItemMultiClickListener;

    private static UserModel User;
    private static String UserID;
    private String strUserName;
    private String strUserImage;

    public ArrayList<String> groupContactImage;
    public ArrayList<String> groupContactName;
    public ArrayList<String> groupContactNumber;

    private String TAG = "AddGroupActivity";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference groupDatRef;
    private DatabaseReference groupListDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        firebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences userPref = getSharedPreferences(RegisterActivity.PREF_NAME, MODE_PRIVATE);
        Log.e(TAG, "Phone No : " + userPref.getString("phone", null));
        UserID = userPref.getString("phone", null);

        groupDatRef = firebaseDatabase.getReference().child("Groups");
        groupListDataRef = firebaseDatabase.getReference().child("GroupList");

        DatabaseReference userdataReference = firebaseDatabase.getReference().child("Users").child(UserID);

        userdataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User = dataSnapshot.getValue(UserModel.class);
                strUserName = User.getUsername();
                strUserImage = User.getImage();
                Log.e(TAG, "Image : " + strUserImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        ImageButton btnCross = (ImageButton) findViewById(R.id.btn_cross);
        btnCross.setOnClickListener(this);

        editGroupName = (EditText) findViewById(R.id.edit_group_name);
        editComment = (EditText) findViewById(R.id.input_comment);

        imageGroup = (SimpleDraweeView) findViewById(R.id.imageGroup);
        imageGroup.setOnClickListener(this);

        groupContactName = new ArrayList<>();
        groupContactNumber = new ArrayList<>();
        groupContactImage = new ArrayList<>();

        mItemMultiClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, REQUEST_SELECT_CONTACT);
                }
            }
        };

        gridSelectedContacts = (GridView) findViewById(R.id.gridSelectedContacts);

        getScale();

        init();
    }

    private void init() {

        if (adapter == null) {
            HelperClass.data = initGridButton();
            getScale();
            adapter = new DuesSharedWithListAdapter(getApplicationContext());
            gridSelectedContacts.setOnItemClickListener(mItemMultiClickListener);
            gridSelectedContacts.setAdapter(adapter);
            return;
        }
        adapter = new DuesSharedWithListAdapter(getApplication());
        gridSelectedContacts.setOnItemClickListener(mItemMultiClickListener);
        gridSelectedContacts.setAdapter(adapter);

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
        gridSelectedContacts.setNumColumns(4);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_cross:
                groupContactName.clear();
                groupContactImage.clear();
                groupContactNumber.clear();
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
            strGroupImage = null;
            try {
                groupImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            groupImage = HelperClass.scaleDown(groupImage,500,true);
            outputFileUri = HelperClass.getImageUri(getApplicationContext(),groupImage);
            strGroupImage = String.valueOf(outputFileUri);
            imageGroup.setImageURI(outputFileUri);
        }

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            strGroupImage = null;
            Uri galleryUri = data.getData();
            try {
                groupImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), galleryUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            groupImage = HelperClass.scaleDown(groupImage,500,true);
            galleryUri = HelperClass.getImageUri(getApplicationContext(),groupImage);
            strGroupImage = String.valueOf(galleryUri);
            imageGroup.setImageURI(galleryUri);
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

            for (int i = 0; i < HelperClass.data.size(); i++) {
                DuesSharedWithModel newModel = HelperClass.data.get(i);
                if (newModel.number != null && model.number.equalsIgnoreCase(newModel.number)) {
                    Toast.makeText(this, "Contact Already Added!!", Toast.LENGTH_SHORT).show();
                    isNumberAddedBefore = true;
                    break;
                }
            }

            if (!isNumberAddedBefore) {
                HelperClass.data.add(model);
            }
            init();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        groupContactName.clear();
        groupContactNumber.clear();
        groupContactImage.clear();
        finish();
    }

    public void AddGroupIntent() {

        strGroupName = editGroupName.getText().toString();
        strGroupComment = editComment.getText().toString();

        if (TextUtils.isEmpty(strGroupName)) {
            Toast.makeText(this, "Please add title of Group", Toast.LENGTH_SHORT).show();
            return;
        }

        groupContactNumber.clear();
        groupContactName.clear();
        groupContactImage.clear();

        groupContactNumber.add(UserID);
        groupContactName.add(strUserName);
        groupContactImage.add(strUserImage);

        if (adapter == null || adapter.getSelected().size() <= 0) {
            Toast.makeText(this, "Select the members with whom u want to share amount", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<DuesSharedWithModel> selected = adapter.getSelected();

        for (int i = 0; i < selected.size(); i++) {
            groupContactName.add(selected.get(i).getName());
            groupContactNumber.add(selected.get(i).getNumber());
            groupContactImage.add(selected.get(i).getImage());

            Log.e(TAG, "Group Contact Name " + i + " : " + groupContactName.get(i) + "\n"
                    + "Group Contact Number " + i + " : " + groupContactNumber.get(i) + "\n"
                    + "Group Contact Image " + i + " : " + groupContactImage.get(i) + "\n"
                    + "Selected Contact \n"
                    + "Name : " + i + " : " + selected.get(i).getName() + "\n"
                    + "Number : " + i + " : " + selected.get(i).getNumber() + "\n"
                    + "Image : " + i + " : " + selected.get(i).getImage() + "\n");
        }

        if (strGroupName.length() != 0) {

            Log.e("Add Group Data Size : ", String.valueOf(HelperClass.data.size()));

            StringBuilder sbNumber = new StringBuilder();
            StringBuilder sbName = new StringBuilder();
            StringBuilder sbImage = new StringBuilder();

            String strNumber;
            String strNames;
            String strImage;

            GroupModel groupModel = new GroupModel();

            for (int i = 0; i < groupContactNumber.size(); i++) {
                if (groupContactNumber.get(i).length() <= 10) {
                    sbNumber.append(groupContactNumber.get(i));
                    if (i != groupContactNumber.size())
                        sbNumber.append(",");
                } else {
                    sbNumber.append(groupContactNumber.get(i).substring(groupContactNumber.get(i).length() - 10));
                    if (i != groupContactNumber.size())
                        sbNumber.append(",");
                }
            }
            strNumber = String.valueOf(sbNumber);
            Log.e(TAG, "strNumber : " + strNumber);

            for (int count = 0; count < groupContactName.size(); count++) {
                if (groupContactName.get(count).equalsIgnoreCase("You")) {
                    sbName.append(strUserName);
                } else {
                    sbName.append(groupContactName.get(count));
                }
                if (count != groupContactName.size() - 1) {
                    sbName.append(",");
                }
            }
            strNames = String.valueOf(sbName);
            Log.e(TAG, "strNames : " + strNames);

            for (int count = 0; count < groupContactImage.size(); count++) {
                sbImage.append(groupContactImage.get(count));
                if (count != groupContactImage.size() - 1) {
                    sbImage.append(",");
                }
            }
            strImage = String.valueOf(sbImage);
            Log.e(TAG, "strImage : " + strImage);

            String Date = DateFormat.getDateInstance().format(new Date());
            String Time = DateFormat.getTimeInstance().format(new Date());

            groupModel.setGroupName(strGroupName);
            groupModel.setGroupImage(strGroupImage);
            if (!strGroupComment.isEmpty())
                groupModel.setGroupComment(strGroupComment);
            groupModel.setDate(Date);
            groupModel.setTime(Time);
            groupModel.setGroupMemberName(strNames);
            groupModel.setGroupMemberNumber(strNumber);
            groupModel.setGroupMemberImage(strImage);

            DatabaseReference groupRef = groupDatRef.push();
            String groupID = groupRef.getKey();
            groupModel.setGroupID(groupID);

            groupRef.setValue(groupModel);

            for (int i = 0; i < groupContactNumber.size(); i++) {
                groupListDataRef.child(groupContactNumber.get(i)).child(groupID).setValue(true);
            }

            startActivity(new Intent(this, MainActivity.class));
            finish();

            return;
        }
        Toast.makeText(this, "Please add title of Group", Toast.LENGTH_SHORT).show();
    }

}
