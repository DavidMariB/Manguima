package com.dmb.testriotapi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.LeagueOfLegends.RecentMatchesFragment;
import com.dmb.testriotapi.LeagueOfLegends.SummonerInfoFragment;
import com.dmb.testriotapi.Models.Champion;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SummonerInfoFragment.OnFragmentInteractionListener,
        RecentMatchesFragment.OnFragmentInteractionListener{

    private FragmentManager fm;
    private FragmentTransaction ft;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;

    private ArrayList<Champion> champions = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();

    private String apiKey,gameVersion;

    private TextView tvUser,tvEmail;
    private ImageView profileIcon;

    private DatabaseReference dbr;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        tvUser = headerLayout.findViewById(R.id.tvUser);
        tvEmail = headerLayout.findViewById(R.id.tvMail);
        profileIcon = headerLayout.findViewById(R.id.imgUser);

        mAuth = FirebaseAuth.getInstance();

        apiKey = "RGAPI-1b9cdf8b-b160-47e6-8f5e-96ecdaca9100";
        gameVersion = "8.2.1";

        getUserData();

    }

    public void getUserData(){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        dbr = FirebaseDatabase.getInstance().getReference("usuarios");

        Query q = dbr.orderByKey().equalTo(mUser.getUid());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren() ){
                    tvUser.setText(dataSnapshot1.getValue(User.class).getUserName());
                    tvEmail.setText(dataSnapshot1.getValue(User.class).getEmail());
                    if(dataSnapshot1.getValue(User.class).getProfileImage() != null){
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionSignOut) {
            mAuth.signOut();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navLol) {
            fm = getSupportFragmentManager();
            fm.popBackStack();
            ft = fm.beginTransaction();
            ft.add(R.id.mainFragment,SummonerInfoFragment.newInstance("",""));
            ft.addToBackStack(null);
            ft.commit();
            Snackbar.make(findViewById(R.id.mainFragment),"League of Legends",Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.navSettings) {

        } else if (id == R.id.navAboutApp) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public ArrayList<Champion> getChampions() {
        return this.champions;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getGameVersion() {
        return gameVersion;
    }

    @Override
    public ArrayList<Match> getMatches() {
        return this.matches;
    }
}
