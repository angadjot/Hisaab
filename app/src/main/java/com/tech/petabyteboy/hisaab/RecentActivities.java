package com.tech.petabyteboy.hisaab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RecentActivities extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar_recent;
    private ImageButton btn_back;
    private Button btn_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_activities);

        toolbar_recent = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_recent);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;

            case R.id.btn_done:
                finish();
                break;
        }
    }
}
