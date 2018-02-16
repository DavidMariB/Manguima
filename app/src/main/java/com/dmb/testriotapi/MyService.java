package com.dmb.testriotapi;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mUser.getUid());
        }
        mUserRef.child("online").setValue("true");
        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        System.out.println("onTaskRemoved called");
        super.onTaskRemoved(rootIntent);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mUser.getUid());
        }
        mUserRef.child("online").setValue("false");
        this.stopSelf();
    }
}