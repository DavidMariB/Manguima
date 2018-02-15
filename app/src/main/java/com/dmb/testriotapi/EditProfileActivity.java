package com.dmb.testriotapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dmb.testriotapi.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private String name,surname,username;
    private EditText etName,etSurname,etUsername;
    private Button btnConfirmEdit;

    private Boolean checkFields;

    private DatabaseReference dbr;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.editUserName);
        etSurname = findViewById(R.id.editUserSurname);
        etUsername = findViewById(R.id.editUserUsername);
        btnConfirmEdit = findViewById(R.id.btnConfirmEditProfile);

        dbr = FirebaseDatabase.getInstance().getReference("usuarios");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getProfileData();

        btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields){
                    editProfile();
                }
            }
        });
    }

    public void getProfileData(){
        name = getIntent().getExtras().getString("name");
        surname = getIntent().getExtras().getString("surname");
        username = getIntent().getExtras().getString("username");

        etName.setText(name);
        etSurname.setText(surname);
        etUsername.setText(username);
    }

    public boolean checkEditFields(){
            if (TextUtils.isEmpty(etName.getText().toString())){
                etName.setError(getText(R.string.CheckNombre));
                checkFields = false;
            }else if(TextUtils.isEmpty(etSurname.getText().toString())){
                etSurname.setError(getText(R.string.CheckApellido));
                checkFields = false;
            }else if(TextUtils.isEmpty(etUsername.getText().toString())){
                etUsername.setError(getText(R.string.CheckUsuario));
                checkFields = false;

            }else {
                checkFields = true;
            }
            return checkFields;

    }

    public void editProfile(){
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = mUser.getUid();
                dbr.child(key).child("name").setValue(etName.getText().toString());
                dbr.child(key).child("surname").setValue(etSurname.getText().toString());
                dbr.child(key).child("userName").setValue(etUsername.getText().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
