package com.dmb.testriotapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dmb.testriotapi.Adapters.ChatAdapter;
import com.dmb.testriotapi.Models.Forum.Mensaje;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class ChatActivity extends MainActivity {


    private static int SIGN_IN_REQUEST_CODE = 1;
    private RecyclerView recycler;
    private ChatAdapter chatAdapter;
    private GenericTypeIndicator<ArrayList<Mensaje>> genericaListaMensajes;
    private ArrayList<Mensaje> listaMensajes;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recycler = (RecyclerView) findViewById(R.id.recycler_view_chat);
        chatAdapter = new ChatAdapter(listaMensajes);

        ref = FirebaseDatabase.getInstance().getReference("chats").child("chat").child("messages");
        genericaListaMensajes = new GenericTypeIndicator<ArrayList<Mensaje>>(){};

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaMensajes = dataSnapshot.getValue(genericaListaMensajes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
