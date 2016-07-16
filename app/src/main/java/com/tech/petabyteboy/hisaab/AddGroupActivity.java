package com.tech.petabyteboy.hisaab;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

public class AddGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView anim_img;
    private RelativeLayout anim_layout;
    private ImageButton btnCross;
    private Button btnDone;
    private EditText editComment;
    private EditText editGroupName;

    private String strGroupComment;
    private String strGroupId;
    private String strGroupImage;
    private String strGroupStatus;
    private String strGroupName;

    private GridView gridSelectedContatcts;

    private SimpleDraweeView imgGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        imgGroup = (SimpleDraweeView) findViewById(R.id.imageGroup);
        imgGroup.setOnClickListener(this);

        editGroupName = (EditText) findViewById(R.id.edit_group_name);
        editComment = (EditText) findViewById(R.id.editComment);

        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        btnCross = (ImageButton) findViewById(R.id.btn_cross);
        btnCross.setOnClickListener(this);

        gridSelectedContatcts = (GridView) findViewById(R.id.gridSelectedContatcts);

        getScale();
    }

    public void getScale() {
        getWindowManager().getDefaultDisplay().getSize(new Point());
        gridSelectedContatcts.setNumColumns(4);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_cross:
                finish();
                break;

            case R.id.btn_done:
                AddGroup();
                break;

            case R.id.imageGroup:
                // Implement showSelectImageDialog() function from register activity
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void AddGroup(){

        strGroupName = editGroupName.getText().toString();
        strGroupComment = editComment.getText().toString();

        if (TextUtils.isEmpty(strGroupName)){
            Toast.makeText(this,"Please add title of an Event",Toast.LENGTH_SHORT).show();
            return;
        }
        else{

            finish();
        }

    }
}
