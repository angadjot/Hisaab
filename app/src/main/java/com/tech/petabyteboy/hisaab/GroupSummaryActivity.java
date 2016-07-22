package com.tech.petabyteboy.hisaab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class GroupSummaryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack;
    private Button btnDone;

    private ListView listGroupSummary;

    String Extra;
    String strTotalAmount;
    String strUserAmount;

    TextView textTotalGroupDue;
    TextView textYouPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_summary);

        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        textTotalGroupDue = (TextView) findViewById(R.id.textTotalGroupDue);
        textYouPaid = (TextView) findViewById(R.id.textYouPaid);
        listGroupSummary = (ListView) findViewById(R.id.listGroupSummary);
        getGroupSummary();
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

    public void getGroupSummary() {

    }
}
