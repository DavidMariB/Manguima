package com.dmb.testriotapi;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.dmb.testriotapi.Users.LoginFragment;
import com.dmb.testriotapi.Users.PasswordFragment;
import com.dmb.testriotapi.Users.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener, PasswordFragment.OnFragmentInteractionListener {

    private FragmentManager fm;
    private FragmentTransaction ft;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setNavigationBarColor(getResources().getColor(R.color.negro));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkIfSignedIn();
    }

    public void callLoginFragment(){
        fm = getSupportFragmentManager();
        fm.popBackStack();
        ft = fm.beginTransaction();
        ft.add(R.id.loginFragment,LoginFragment.newInstance("",""));
        ft.addToBackStack(null);
        ft.commit();
    }

    public void checkIfSignedIn(){
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser!=null){
            Intent intent = new Intent(this,DynamicActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            callLoginFragment();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}