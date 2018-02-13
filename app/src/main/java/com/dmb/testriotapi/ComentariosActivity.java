package com.dmb.testriotapi;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.Adapters.ComentariosAdapter;
import com.dmb.testriotapi.Models.Forum.Comentario;
import com.dmb.testriotapi.Models.Forum.Forum;
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

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComentariosActivity extends AppCompatActivity {

    private String key;
    private RecyclerView rv_Comments;
    private ArrayList<Comentario> comentario = new ArrayList<>();
    DatabaseReference bbdd, bbddTema;
    ComentariosAdapter adaptador;
    String fu;
    private EditText et_Comentario;
    private ImageButton btn_Send;
    private ImageView img_Profile;
    private TextView txt_mensaje, txt_titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/

        key = getIntent().getStringExtra("key");

        rv_Comments = (RecyclerView) findViewById(R.id.rv_Comments);
        et_Comentario = (EditText) findViewById(R.id.et_Comentario);
        btn_Send = (ImageButton) findViewById(R.id.btn_Send);
        img_Profile = (ImageView) findViewById(R.id.img_Profile);
        txt_mensaje = (TextView) findViewById(R.id.txt_mensaje);
        txt_titulo = (TextView) findViewById(R.id.txt_titulo);

        bbddTema = FirebaseDatabase.getInstance().getReference().child("forum").child(key);

        bbddTema.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bindForum(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bbdd = FirebaseDatabase.getInstance().getReference().child("forum").child(key).child("comentarios");

        bbdd.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comentario.clear();
                for (DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                    Comentario c = datasnapshot.getValue(Comentario.class);
                    comentario.add(c);


                    if (datasnapshot.getValue(User.class).getProfileImage()!= null){
                        StorageReference sR = FirebaseStorage.getInstance().getReference().child(dataSnapshot.getValue(User.class).getProfileImage());
                        Toast.makeText(ComentariosActivity.this, sR.toString(), Toast.LENGTH_SHORT).show();
                        Glide.with(getApplicationContext())
                                .using(new FirebaseImageLoader())
                                .load(sR)
                                .into(img_Profile);
                    }
                }
                adaptador = new ComentariosAdapter(comentario);
                rv_Comments.setAdapter(adaptador);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        rv_Comments.setLayoutManager(new LinearLayoutManager(ComentariosActivity.this, LinearLayoutManager.VERTICAL, false));

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmt = et_Comentario.getText().toString();
                fu = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Comentario cmt2 = new Comentario();
                cmt2.setMensaje(cmt);
                cmt2.setUid(fu);
                bbdd.child(bbdd.push().getKey()).setValue(cmt2);

            }
        });
    }

    private void bindForum(DataSnapshot dataSnapshot) {
        Forum f = dataSnapshot.getValue(Forum.class);
        txt_titulo.setText(f.getTitulo());
        txt_mensaje.setText(f.getMensaje());
    }
}
