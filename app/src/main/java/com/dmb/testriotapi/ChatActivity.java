package com.dmb.testriotapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dmb.testriotapi.Adapters.ChatAdapter;
import com.dmb.testriotapi.Models.Forum.Chat;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends MainActivity implements View.OnClickListener{

    private RecyclerView recycler;
    private ChatAdapter chatAdapter;
    private FirebaseUser currentUser;
    private ArrayList<Mensaje> listaMensajes;
    private ArrayList<String> uidKey;
    private ArrayList<Chat> listaChats;
    private DatabaseReference ref;
    private ImageView send;
    private EditText etMensaje;
    private String uid2, uid1;
    private TextView targetUser;
    private String keyChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ref = (FirebaseDatabase.getInstance().getReference("chats"));
        listaMensajes = new ArrayList<Mensaje>();
        listaChats = new ArrayList<Chat>();
        uidKey = new ArrayList<String>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        send = (ImageView) findViewById(R.id.btnSendChat);
        etMensaje = (EditText) findViewById(R.id.etMessageChat);
        targetUser = (TextView) findViewById(R.id.tTargetUser);

        Intent i = getIntent();
        uid2 = i.getStringExtra("uid");
        uid1 = currentUser.getUid();
        uidKey.add(uid1);
        uidKey.add(uid2);
        Collections.sort(uidKey);

        keyChat = uidKey.get(0) + uidKey.get(1);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.morado));
        getWindow().setStatusBarColor(getResources().getColor(R.color.negro));
        targetUser.setText(i.getStringExtra("nombre"));

        send.setOnClickListener(this);

        recycler = (RecyclerView) findViewById(R.id.recycler_view_chat);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Crea el chat
        createChat();
        //Comprueba los mensajes en tiempo real
        checkChat();
    }

    @Override
    public void onClick(View view) {

        //Boton de enviar
        if (view.getId() == R.id.btnSendChat) {
            if (!etMensaje.getText().toString().equals("")) {
                String keyMensaje = ref.push().getKey();
                ref.child(keyChat).child(keyMensaje)
                        .setValue(new Mensaje(etMensaje.getText().toString(), currentUser.getUid()));
                etMensaje.setText("");
            }
        }
    }

    //------------------------Dropeo de chat que seguramente acabare quitando porque no me gusta--//
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //ref.child(keyChat).removeValue();
    }

    //--------------------Metodo para comprobar si ya se ha creado un chat-----------------//
    public void checkChat() {

        Query q = ref.child(keyChat);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaMensajes.clear();
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

    //--------------------Metodo para crear un chat-----------------//
    public void createChat() {

        ref.child(keyChat).child("recordatorio")
                .setValue(new Mensaje(getText(R.string.NoRecuerdaContra).toString(), "ElBuenoDeManguima"));
    }
}
