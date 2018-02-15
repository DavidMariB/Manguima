package com.dmb.testriotapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dmb.testriotapi.Adapters.ChatAdapter;
import com.dmb.testriotapi.Models.Forum.Mensaje;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends MainActivity implements View.OnClickListener{

    private RecyclerView recycler;
    private ChatAdapter chatAdapter;
    FirebaseUser currentUser;
    private ArrayList<Mensaje> listaMensajes;
    private DatabaseReference ref;
    private Button send;
    private EditText etMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        listaMensajes = new ArrayList<Mensaje>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        send = (Button) findViewById(R.id.btnSendChat);
        etMensaje = (EditText) findViewById(R.id.etMessageChat);

        send.setOnClickListener(this);

        recycler = (RecyclerView) findViewById(R.id.recycler_view_chat);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ref = FirebaseDatabase.getInstance().getReference("chats");
        Query q = ref.child("chat").child("messages");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    listaMensajes.add(child.getValue(Mensaje.class));
                }
                chatAdapter = new ChatAdapter(listaMensajes);
                recycler.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View view) {

        String etMensaje = this.etMensaje.getText().toString();
        String uid = currentUser.getUid();

        /*Usuario u = usuariosListObject.get(i);
        bbdd = FirebaseDatabase.getInstance().getReference("usuarios").child(u.getUser());
        bbdd.child("seguidores").child("uid").setValue(usuarioActual.getUid());*/
    }
}
