package com.dmb.testriotapi;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.net.Uri;

import android.os.Bundle;
import android.util.Log;

import com.dmb.testriotapi.Users.LoginFragment;
import com.dmb.testriotapi.Users.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener {

    private FragmentManager fm;
    private FragmentTransaction ft;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //mAuth.signOut();

        if(mUser!=null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }else{
            callLoginFragment();
        }
    }

    @Override
    protected void onResume() {
        callLoginFragment();
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}