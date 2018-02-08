package com.dmb.testriotapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseListAdapter;


public class ChatActivity extends AppCompatActivity {


    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<MensajeChat> adapter;
    RelativeLayout activity_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



    }
}
