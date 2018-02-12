package com.dmb.testriotapi.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmb.testriotapi.Adapters.ChatAdapter;
import com.dmb.testriotapi.Adapters.FriendsAdapter;
import com.dmb.testriotapi.Models.Friend;
import com.dmb.testriotapi.Models.User;
import com.dmb.testriotapi.R;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String friendName,friendUsername,friendSurname,friendConectado;
    private ProgressDialog progressDialog;

    private Bundle bundle;

    private RecyclerView recyclerView;

    private FriendsAdapter fa;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        bundle = this.getArguments();

        recyclerView = v.findViewById(R.id.recyclerFriends);

        fa = new FriendsAdapter(mListener.getFriends(), new FriendsAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(final View v, final int position) {
                Log.e("AMIGOS: ",""+mListener.getFriends().size());
                AlertDialog.Builder alertBox = new AlertDialog.Builder(v.getRootView().getContext());
                alertBox.setMessage(getText(R.string.SeguroEliminar) + " "+
                        mListener.getFriends().get(position).getUsername()+" " + " "+ getText(R.string.DeAmigos))
                        .setPositiveButton(getText(R.string.Si), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Snackbar.make(v,mListener.getFriends().get(position).getUsername()+" "+
                                        getText(R.string.EliminarAmigos),Snackbar.LENGTH_LONG).show();
                                mListener.getFriends().remove(position);
                                recyclerView.getAdapter().notifyDataSetChanged();
                                Log.e("AMIGOS: ",""+mListener.getFriends().size());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                alertBox.show();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(fa);

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        ArrayList<Friend> getFriends();
    }
}
