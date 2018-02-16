package com.dmb.testriotapi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.Adapters.ComentariosAdapter;
import com.dmb.testriotapi.Models.Forum.Comentario;
import com.dmb.testriotapi.Models.Forum.Forum;
import com.dmb.testriotapi.Models.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ricardo Borrull on 07/02/2018.
 */

public class ComentariosActivity extends MainActivity {

    private String key, fu, uid;
    private RecyclerView rv_Comments;
    private ArrayList<Comentario> comentario = new ArrayList<>();
    DatabaseReference bbdd, bbddFoto, bbddTema, bbddFotoPropia;
    ComentariosAdapter adaptador;
    private FloatingActionButton fab_Options;
    private EditText et_Comentario;
    private ImageView img_ProfileT, btn_Send, img_ProfileC;
    private TextView txt_mensaje, txt_titulo, txt_user2;
    private ArrayList<String> opciones = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        fu = FirebaseAuth.getInstance().getCurrentUser().getUid();

        key = getIntent().getStringExtra("key");
        uid = getIntent().getStringExtra("uid");

        getWindow().setNavigationBarColor(getResources().getColor(R.color.negro));
        getWindow().setStatusBarColor(getResources().getColor(R.color.negro));

        rv_Comments = (RecyclerView) findViewById(R.id.rv_Comments);
        fab_Options = (FloatingActionButton) findViewById(R.id.fab_Options);
        et_Comentario = (EditText) findViewById(R.id.et_Comentario);
        btn_Send = (ImageView) findViewById(R.id.btn_Send);
        img_ProfileT = (ImageView) findViewById(R.id.img_ProfileT);
        img_ProfileC = (ImageView) findViewById(R.id.img_ProfileC);
        txt_mensaje = (TextView) findViewById(R.id.txt_mensaje);
        txt_titulo = (TextView) findViewById(R.id.txt_titulo);
        txt_user2 = (TextView) findViewById(R.id.txt_user2);

        fab_Options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MostrarDialogo();
            }
        });

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

        bbddFoto = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid);

        bbddFoto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                //String foto = u.getProfileImage();
                String user = u.getUserName();
                txt_user2.setText(user);

                if (dataSnapshot.getValue(User.class).getProfileImage() != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(dataSnapshot.getValue(User.class).getProfileImage());
                    Glide.with(getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(img_ProfileT);
                } else {
                    Picasso.with(getApplicationContext()).load(R.mipmap.default_avatar).into(img_ProfileT);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bbddFotoPropia = FirebaseDatabase.getInstance().getReference().child("usuarios").child(fu);

        bbddFotoPropia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(User.class).getProfileImage() != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(dataSnapshot.getValue(User.class).getProfileImage());
                    Glide.with(getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(img_ProfileC);
                } else {
                    Picasso.with(getApplicationContext()).load(R.mipmap.default_avatar).into(img_ProfileC);
                }
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
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    Comentario c = datasnapshot.getValue(Comentario.class);
                    comentario.add(c);
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
                if (TextUtils.isEmpty(cmt)) {
                    et_Comentario.setError(getText(R.string.FalloComentario));
                } else {
                    Comentario cmt2 = new Comentario();
                    cmt2.setMensaje(cmt);
                    cmt2.setUid(fu);
                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
                    cmt2.setFecha(currentDate);
                    bbdd.child(bbdd.push().getKey()).setValue(cmt2);
                    et_Comentario.setText("");
                }


            }
        });
    }

    private void bindForum(DataSnapshot dataSnapshot) {
        Forum f = dataSnapshot.getValue(Forum.class);
        txt_titulo.setText(f.getTitulo());
        txt_mensaje.setText(f.getMensaje());
    }

    /*public Dialog MostrarDialogo() {
        ArrayList<String> opciones = new ArrayList<String>();
        opciones.add("Hola");
        AlertDialog.Builder builder = new AlertDialog.Builder(ComentariosActivity.this);
        builder.setTitle("Opciones")
                .setItems(1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }*/
}
