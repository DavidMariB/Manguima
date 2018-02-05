package com.dmb.testriotapi.Users;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dmb.testriotapi.Models.User;
import com.dmb.testriotapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

public class RegisterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText etRegUsername,etRegName,etRegSurname,etRegAge,etRegEmail,etRegPassword;
    private Button btnConfirmRegister;
    private TextView tvAlreadyRegistered;

    private String getRegUsername,getRegName,getRegSurname,getRegAge,getRegEmail,getRegPassword;

    private FragmentManager fm;
    private FragmentTransaction ft;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

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

    public void userRegister(){
        getRegUsername = etRegUsername.getText().toString();
        getRegName = etRegName.getText().toString();
        getRegSurname = etRegSurname.getText().toString();
        getRegAge = etRegAge.getText().toString();
        getRegEmail = etRegEmail.getText().toString();
        getRegPassword = etRegPassword.getText().toString();

        checkFields();
    }

    public void checkFields(){
        if (getRegUsername.isEmpty() || getRegName.isEmpty() || getRegSurname.isEmpty() ||getRegAge.isEmpty() ||
                getRegEmail.isEmpty() || getRegPassword.isEmpty()){
            Toast.makeText(getContext(), "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            createAccount();
        }
    }

    public void createUser(String key){
        User user = new User(getRegUsername,getRegName,getRegSurname,getRegAge,getRegEmail,"user");
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
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.e("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "No se ha podido crear el usuario.", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                }else{
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
