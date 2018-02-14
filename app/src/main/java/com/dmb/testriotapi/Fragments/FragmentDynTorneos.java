package com.dmb.testriotapi.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmb.testriotapi.Adapters.TorneosAdapter;
import com.dmb.testriotapi.Models.Torneo;
import com.dmb.testriotapi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentDynTorneos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference bbdd;
    private RecyclerView rv_Torneo;
    private ArrayList<Torneo> torneo = new ArrayList<>();
    TorneosAdapter adaptador;

    private OnFragmentInteractionListener mListener;

    public FragmentDynTorneos() {
        // Required empty public constructor
    }

    public static FragmentDynTorneos newInstance(String param1, String param2) {
        FragmentDynTorneos fragment = new FragmentDynTorneos();
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
        View v = inflater.inflate(R.layout.fragment_fragment_dyn_torneos, container, false);

        rv_Torneo = (RecyclerView) v.findViewById(R.id.rv_Torneo);
        cargarTorneos();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.cosasDelTorneo(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener3");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void cosasDelTorneo(Uri uri);
    }

    public void cargarTorneos() {
        bbdd = FirebaseDatabase.getInstance().getReference().child("torneos");

        bbdd.limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                torneo.clear();
                for (DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                    Torneo t = datasnapshot.getValue(Torneo.class);
                    torneo.add(t);
                }
                adaptador = new TorneosAdapter(torneo);
                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*Intent i = new Intent(getContext(), ComentariosActivity.class);
                        //Cogemos la posición, elegimos la key de esta y la mandamos a info activity
                        i.putExtra("key",foro.get(rv_Forum.getChildAdapterPosition(v)).getKey().toString());
                        i.putExtra("uid", foro.get(rv_Forum.getChildAdapterPosition(v)).getUid().toString());
                        startActivity(i);*/

                    }
                });
                rv_Torneo.setAdapter(adaptador);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        rv_Torneo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }
}
