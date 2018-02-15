package com.dmb.testriotapi;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dmb.testriotapi.Models.Forum.Forum;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class NewPostActivity extends AppCompatActivity {

    private EditText etNombre, etMensaje;
    private Button btnNuevoTema;
    private String getNombre,getMensaje, getUser, getKey;
    boolean checkFields;
    private FirebaseAuth mAuth;
    DatabaseReference bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etMensaje = (EditText) findViewById(R.id.etMensaje);
        btnNuevoTema = (Button) findViewById(R.id.btnNuevoTema);
        bbdd = (FirebaseDatabase.getInstance().getReference("forum"));


        btnNuevoTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTemaFields();
                crearTema();
            }
        });


        Toast.makeText(NewPostActivity.this, "¡Tema creado!", Toast.LENGTH_SHORT).show();

    }

    public boolean checkTemaFields(){

        if (TextUtils.isEmpty(getNombre)){
            etNombre.setError("Introduce un nombre de tema");
            checkFields = false;
        }else if(TextUtils.isEmpty(getMensaje)){
            etMensaje.setError("¡El tema no puede estar vacío!");
            checkFields = false;
        }else {
            checkFields = true;
        }
        return checkFields;
    }

    public void crearTema() {
        getNombre = etNombre.getText().toString();
        getMensaje = etMensaje.getText().toString();
        final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        getUser = user.getUid();

        getKey = bbdd.push().getKey();

        Forum f = new Forum(getNombre, getUser, getMensaje, currentDate, getKey);

        bbdd.child(getKey).setValue(f);

        finish();

    }
}
