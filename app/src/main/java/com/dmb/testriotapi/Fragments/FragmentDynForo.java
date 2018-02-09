package com.dmb.testriotapi.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dmb.testriotapi.Adapters.ForumAdapter;
import com.dmb.testriotapi.Models.Forum.Comentario;
import com.dmb.testriotapi.Models.Forum.Forum;
import com.dmb.testriotapi.Models.Forum.Like;
import com.dmb.testriotapi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentDynForo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DatabaseReference bbdd, bbddComentario, bbddLike;
    private RecyclerView rv_Forum;
    private ArrayList<Comentario> comentario = new ArrayList<>();
    private ArrayList<Like> like = new ArrayList<>();
    private ArrayList<Forum> foro = new ArrayList<>();
    ForumAdapter adaptador;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentDynForo() {
        // Required empty public constructor
    }

    public static FragmentDynForo newInstance(String param1, String param2) {
        FragmentDynForo fragment = new FragmentDynForo();
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
        View v = inflater.inflate(R.layout.fragment_fragment_dyn_foro, container, false);

        rv_Forum = (RecyclerView) v.findViewById(R.id.rv_Forum);

        bbdd = FirebaseDatabase.getInstance().getReference().child("forum");
        bbddComentario = bbdd.child("comentarios");
        bbddLike = bbdd.child("likes");

        bbdd.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                    Forum f = datasnapshot.getValue(Forum.class);
                    foro.add(f);
                }
                adaptador = new ForumAdapter(foro);
                rv_Forum.setAdapter(adaptador);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        rv_Forum.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.cosasDelForo(uri);
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
                    + " must implement OnFragmentInteractionListener2");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void cosasDelForo(Uri uri);
    }
}
