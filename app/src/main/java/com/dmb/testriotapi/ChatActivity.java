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
    private String uid2;
    String keyChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ref = (FirebaseDatabase.getInstance().getReference("chats"));
        listaMensajes = new ArrayList<Mensaje>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        send = (Button) findViewById(R.id.btnSendChat);
        etMensaje = (EditText) findViewById(R.id.etMessageChat);
        uid2 = "necesito que alguien me pase una uid";

        send.setOnClickListener(this);

        recycler = (RecyclerView) findViewById(R.id.recycler_view_chat);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        String etMensaje = this.etMensaje.getText().toString();
        String uid1 = currentUser.getUid();

        if (keyChat == null) {
            keyChat = ref.push().getKey();
        }
        Chat chat = new Chat(uid1, uid2, keyChat);

        ref.child(keyChat).setValue(chat);
        ref.child(keyChat).child("messages").child("recordatorio")
                .setValue(new Mensaje("Recuerda no dar contraseñas", "ElBuenoDeManguima"));
        Query q = ref.child(keyChat).child("messages");

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

    @Override
    public void onClick(View view) {

        if (!etMensaje.getText().toString().equals("")) {
            String keyMensaje = ref.push().getKey();
            ref.child(keyChat).child("messages").child(keyMensaje)
                    .setValue(new Mensaje(etMensaje.getText().toString(), currentUser.getUid()));
            etMensaje.setText("");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ref.child(keyChat).removeValue();
    }
}
