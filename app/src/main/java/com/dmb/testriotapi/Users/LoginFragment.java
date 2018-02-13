package com.dmb.testriotapi.Users;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dmb.testriotapi.MainActivity;
import com.dmb.testriotapi.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText etLogEmail,etLogPassword;
    private TextView tvNotRegistered;
    private TextView tvResetPassword;
    private Button btnSignIn;

    private View mLoginFormView;

    private Boolean checkFields;

    private String getLogEmail,getLogPassword;

    private FirebaseAuth mAuth;

    private FragmentManager fm;
    private FragmentTransaction ft;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        etLogEmail = v.findViewById(R.id.etLoginEmail);
        etLogPassword = v.findViewById(R.id.etLoginPassword);
        mLoginFormView = v.findViewById(R.id.login_form);
        tvNotRegistered = v.findViewById(R.id.tvNotRegistered);
        btnSignIn = v.findViewById(R.id.btnSignIn);
        tvResetPassword = v.findViewById(R.id.tvForgetPassword);

        tvNotRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRegisterFragment();
            }
        });

        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordForget();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkLogFields()){
                    logInUser();
                }
            }
        });

        return v;
    }

    public void callRegisterFragment(){
        fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
        ft = fm.beginTransaction();
        ft.add(R.id.loginFragment,RegisterFragment.newInstance("",""));
        ft.addToBackStack(null);
        ft.commit();
    }

    public void passwordForget(){
        fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
        ft = fm.beginTransaction();
        ft.add(R.id.loginFragment,PasswordFragment.newInstance("",""));
        ft.addToBackStack(null);
        ft.commit();
    }

    public void logInUser(){
        getLogEmail = etLogEmail.getText().toString();
        getLogPassword = etLogPassword.getText().toString();


        mAuth.signInWithEmailAndPassword(getLogEmail, getLogPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            Toast.makeText(getContext(), R.string.SesionIniciada, Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            getActivity().getIntent().putExtra("userUID",user.getUid());
                            getActivity().setResult(RESULT_OK,getActivity().getIntent());
                            getActivity().finish();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), R.string.FalloLogin,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public boolean checkLogFields(){
        getLogEmail = etLogEmail.getText().toString();
        getLogPassword = etLogPassword.getText().toString();

        if (TextUtils.isEmpty(getLogEmail)){
            etLogEmail.setError(getText(R.string.CheckEmail));
            checkFields = false;
        }else if(TextUtils.isEmpty(getLogPassword)){
            etLogPassword.setError(getText(R.string.CheckContraseña));
            checkFields = false;

        }else {
            checkFields = true;
        }
        return checkFields;
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
