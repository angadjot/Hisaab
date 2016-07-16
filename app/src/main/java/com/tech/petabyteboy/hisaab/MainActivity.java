package com.tech.petabyteboy.hisaab;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.models.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, View.OnClickListener {

    private Toolbar toolBar;
    private NavigationView navDrawer;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedItem;
    private FragmentManager FragmentManager;
    private FragmentTransaction FragmentTransaction;

    private SimpleDraweeView imgProfile;
    private TextView txtUserName;

    private View navDrawerHeaderView;
    private ImageButton btn_edit_profile;
    private TextView navUsername;
    private TextView navEmailID;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userdataReference;
    private DatabaseReference duesdataReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private String Username;
    private String EmailID;
    private String phoneno;

    Users usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FragmentManager = getSupportFragmentManager();
        FragmentTransaction = FragmentManager.beginTransaction();
        FragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navDrawer = (NavigationView) findViewById(R.id.menu_drawer);
        navDrawer.setNavigationItemSelectedListener(this);

        navDrawerHeaderView = navDrawer.getHeaderView(0);

        btn_edit_profile = (ImageButton) navDrawerHeaderView.findViewById(R.id.btn_edit_profile);
        btn_edit_profile.setOnClickListener(this);

        navUsername = (TextView) navDrawerHeaderView.findViewById(R.id.username);
        navEmailID = (TextView) navDrawerHeaderView.findViewById(R.id.user_mail);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        selectedItem = savedInstanceState == null ? R.id.home : savedInstanceState.getInt("selectedItem");

        imgProfile = (SimpleDraweeView) findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String uid = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();

        userdataReference = firebaseDatabase.getReference().child("Users").child(uid);
        duesdataReference = firebaseDatabase.getReference().child("Dues").child(uid);

        userdataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usr = dataSnapshot.getValue(Users.class);

                Username = usr.getUsername();
                EmailID = usr.getEmailID();
                phoneno = usr.getPhoneNumber();
                setValues();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setValues(){

        if (Username.isEmpty()) {
            navUsername.setText("Username");
            txtUserName.setText("Username");
        }
        else {
            navUsername.setText(Username);
            txtUserName.setText(Username);
        }

        if (EmailID.isEmpty()){
            navEmailID.setText("emailid@domain.com");
        }
        else {
            navEmailID.setText(EmailID);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        item.setChecked(true);
        selectedItem = item.getItemId();

        switch (selectedItem) {
            case R.id.home:
                Toast.makeText(this, "Home is clicked !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.invite_friends:
                Toast.makeText(this, "Invite Friends is clicked !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.recent_activity:
                startActivity(new Intent(this,RecentActivities.class));
                break;
            case R.id.favorites:
                Toast.makeText(this, "Favorite is clicked !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings is clicked !", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.search:

                break;

            case R.id.history:
                Intent intent = new Intent(this,RecentActivities.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt("selectedItem", selectedItem);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgProfile:
            case R.id.txtUserName:

                Intent intent = new Intent(this,ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_edit_profile:
                startActivity(new Intent(getApplication(),ProfileActivity.class));
                break;
        }
    }
}
