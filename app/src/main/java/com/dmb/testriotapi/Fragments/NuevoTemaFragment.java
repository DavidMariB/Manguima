package com.dmb.testriotapi.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dmb.testriotapi.Models.Forum.Forum;
import com.dmb.testriotapi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class NuevoTemaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText etNombre, etMensaje;
    private Button btnNuevoTema;
    private String getNombre,getMensaje, getUser, getHora;
    boolean checkFields;
    private FirebaseAuth mAuth;
    DatabaseReference bbdd;

    private OnFragmentInteractionListener mListener;

    public NuevoTemaFragment() {
        // Required empty public constructor
    }

    public static NuevoTemaFragment newInstance(String param1, String param2) {
        NuevoTemaFragment fragment = new NuevoTemaFragment();
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
        View v = inflater.inflate(R.layout.fragment_nuevo_tema, container, false);

        etNombre  = v.findViewById(R.id.etNombre);
        etMensaje = v.findViewById(R.id.etMensaje);
        btnNuevoTema = v.findViewById(R.id.btnNuevoTema);
        bbdd = (FirebaseDatabase.getInstance().getReference("forum"));

        btnNuevoTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTemaFields();
                crearTema();
                Toast.makeText(getContext(), "¡Tema creado!", Toast.LENGTH_SHORT).show();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.foro_container,FragmentDynForo.newInstance("",""));
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        getHora = Calendar.getInstance().getTime().toString();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        getUser = user.getUid();

        Forum f = new Forum(getNombre, getUser, getMensaje, getHora);

        bbdd.child(bbdd.push().getKey()).setValue(f);

        getActivity().getFragmentManager().popBackStack();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
