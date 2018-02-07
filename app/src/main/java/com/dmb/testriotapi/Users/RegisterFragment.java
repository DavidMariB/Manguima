package com.dmb.testriotapi.Users;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dmb.testriotapi.Fragments.DatePickerFragment;
import com.dmb.testriotapi.LoginActivity;
import com.dmb.testriotapi.Models.User;
import com.dmb.testriotapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

public class RegisterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText etRegUsername,etRegName,etRegSurname,etRegEmail,etRegPassword;
    private Button btnConfirmRegister;
    private TextView tvAlreadyRegistered, etRegAge;
    private ImageButton btnPickImage, btnDate;

    private String getRegUsername,getRegName,getRegSurname,getRegAge,getRegEmail,getRegPassword;

    boolean checkFields;

    private FragmentManager fm;
    private FragmentTransaction ft;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private FirebaseStorage fs = FirebaseStorage.getInstance();
    private StorageReference sr = fs.getReference();

    static final int PICK_IMAGE_REQUEST = 71;

    private Uri filePath;

    private String imgRef;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        etRegUsername = v.findViewById(R.id.etRegUsername);
        etRegName = v.findViewById(R.id.etRegName);
        etRegSurname = v.findViewById(R.id.etRegSurname);
        etRegAge = v.findViewById(R.id.etRegAge);
        etRegEmail = v.findViewById(R.id.etRegEmail);
        etRegPassword = v.findViewById(R.id.etRegPassword);
        tvAlreadyRegistered = v.findViewById(R.id.tvAlreadyRegistered);
        btnConfirmRegister = v.findViewById(R.id.btnConfirmRegister);
        btnPickImage = v.findViewById(R.id.btnPickProfileImage);
        btnDate = v.findViewById(R.id.btnDate);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        tvAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoginFragment();
            }
        });

        btnConfirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
            }
        });

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        return v;
    }

    public void callLoginFragment(){
        fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
        ft = fm.beginTransaction();
        ft.add(R.id.loginFragment,LoginFragment.newInstance("",""));
        ft.addToBackStack(null);
        ft.commit();
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Subiendo Imagen...");
            progressDialog.show();

            StorageReference ref = sr.child("imagenes/"+ UUID.randomUUID().toString());
            imgRef = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Imagen Subida", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Fallo al subir la imagen "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Subiendo: "+(int)progress+"%");
                        }
                    });
        }
    }

    public void userRegister(){
        getRegUsername = etRegUsername.getText().toString();
        getRegName = etRegName.getText().toString();
        getRegSurname = etRegSurname.getText().toString();
        getRegAge = etRegAge.getText().toString();
        getRegEmail = etRegEmail.getText().toString();
        getRegPassword = etRegPassword.getText().toString();

        if(checkRegFields()){

            createAccount();

        }
    }


    public boolean checkRegFields(){

        if (TextUtils.isEmpty(getRegUsername)){
            etRegUsername.setError("Introduce un usuario");
            checkFields = false;
        }else if(TextUtils.isEmpty(getRegName)){
            etRegName.setError("Introduce un nombre");
            checkFields = false;
        }else if(TextUtils.isEmpty(getRegSurname)){
            etRegSurname.setError("Introduce un apellido");
            checkFields = false;
        }else if(TextUtils.isEmpty(getRegAge)){
            etRegAge.setError("Introduce una edad");
            checkFields = false;
        }else if(TextUtils.isEmpty(getRegEmail)){
            etRegEmail.setError("Introduce un email");
            checkFields = false;
        }else if(TextUtils.isEmpty(getRegPassword)){
            etRegPassword.setError("Introduce una contraseña");
            checkFields = false;
        }else {
            checkFields = true;
        }
        return checkFields;
    }

    public void createUser(String key){
        User user = new User(getRegUsername,getRegName,getRegSurname,getRegAge,getRegEmail,imgRef);
        databaseReference.child(key).setValue(user);
    }

    public void createAccount(){
        DatabaseReference ddbb = FirebaseDatabase.getInstance().getReference();
        ddbb.child("usuarios").child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    mAuth.createUserWithEmailAndPassword(getRegEmail, getRegPassword)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.e("TAG", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                createUser(user.getUid());
                                getActivity().getIntent().putExtra("userUID",user.getUid());
                                getActivity().setResult(RESULT_OK,getActivity().getIntent());
                                Toast.makeText(getContext(), "Usuario Añadido", Toast.LENGTH_SHORT).show();
                                callLoginFragment();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), "No se ha podido crear el usuario.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    Toast.makeText(getContext(), "Debes escoger un nombre de usuario o correo diferentes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anyo, int mes, int dia) {
                //Aquí recogemos la información (Añadimos 1 al mes porque enero sale como 0)
                final String fechaSelecionada = dia + " / " + (mes+1) + " / " + anyo;
                etRegAge.setText(fechaSelecionada);
            }
        });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
