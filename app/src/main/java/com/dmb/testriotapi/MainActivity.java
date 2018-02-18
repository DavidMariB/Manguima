package com.dmb.testriotapi;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.Fragments.FriendsFragment;
import com.dmb.testriotapi.LeagueOfLegends.RecentMatchesFragment;
import com.dmb.testriotapi.LeagueOfLegends.SummonerInfoFragment;
import com.dmb.testriotapi.Models.Champion;
import com.dmb.testriotapi.Models.Friend;
import com.dmb.testriotapi.Models.Match;
import com.dmb.testriotapi.Models.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;

    private String apiKey,gameVersion,selectedRegion,accountID;

    private LinearLayout holderLayout;

    private TextView tvUser,tvEmail;
    private ImageView profileIcon;

    private static NavigationView navigationView;
    private String currentUserPic, currentUserName;

    private DatabaseReference dbr;

    private User user;

    private DatabaseReference mUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        holderLayout = (LinearLayout) findViewById(R.id.holderLayout);

        tvUser = headerLayout.findViewById(R.id.tvUser);
        profileIcon = headerLayout.findViewById(R.id.imgUser);

        if (mUser != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mUser.getUid());
        }

        getUserData();
        final DatabaseReference lastOnlineRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mUser.getUid()).child("lastTime");

        final DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");

        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    DatabaseReference con = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mUser.getUid()).push();

                    // when this device disconnects, remove it
                    con.onDisconnect().removeValue();
                    mUserRef.onDisconnect().setValue("false");
                    // when I disconnect, update the last time I was seen online
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);

                    // add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    con.setValue(Boolean.TRUE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navUsers) {

            Intent intent = new Intent(this,UsersActivity.class);
            startActivity(intent);
        } else if (id==R.id.navFriends){

            //Esto cambialo cuando este disponible la lista de amigos
            /*Intent ref = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(ref);
            finish();*/
        } else if (id == R.id.navSettings) {

            Intent refresh = new Intent(MainActivity.this, ConfigActivity.class);
            startActivity(refresh);
        } else if (id == R.id.navLogOut) {

            mAuth.signOut();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if(currentUser != null) {
                mUserRef.child("online").setValue("false");
            }
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        } else if (id == R.id.navForum) {


            DynamicActivity.setCurrentPage(1);

        } else if (id == R.id.navInfo) {

            DynamicActivity.setCurrentPage(3);

        } else if (id == R.id.navTournament) {

            DynamicActivity.setCurrentPage(2);
        } else if(id == R.id.navAboutUs) {

            alertInfo();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getUserData() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        dbr = FirebaseDatabase.getInstance().getReference("usuarios");

        Query q = dbr.orderByKey().equalTo(mUser.getUid());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    tvUser.setText(dataSnapshot1.getValue(User.class).getUserName());
                    currentUserName = dataSnapshot1.getValue(User.class).getUserName();
                    if (dataSnapshot1.getValue(User.class).getProfileImage() != null) {
                        currentUserPic = dataSnapshot1.getValue(User.class).getProfileImage();
                        storageReference = FirebaseStorage.getInstance().getReference().child(dataSnapshot1.getValue(User.class).getProfileImage());
                        Glide.with(getApplicationContext())
                                .using(new FirebaseImageLoader())
                                .load(storageReference)
                                .into(profileIcon);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        if (holderLayout != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, holderLayout, false);
            holderLayout.addView(stubView, lp);
        }
    }

    public void alertInfo() {

        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.infoTitle)
                .setMessage(R.string.thx)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.negro);
            }
        });

        dialog.show();

    }

    public void setIsInDynamicTrue() {

        showItems();
    }

    public void setIsInDynamicFalse() {

        hideItems();
    }

    public void hideItems() {

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.navForum).setVisible(false);
        menu.findItem(R.id.navTournament).setVisible(false);
        menu.findItem(R.id.navInfo).setVisible(false);
    }

    public void showItems() {

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.navForum).setVisible(true);
        menu.findItem(R.id.navTournament).setVisible(true);
        menu.findItem(R.id.navInfo).setVisible(true);
    }

    public String getCurrentUserPic() {

        return currentUserPic;
    }

    public String getCurrentUserName() {

        return currentUserName;
    }
}

