package com.dmb.testriotapi.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dmb.testriotapi.Adapters.ChampsAdapter;
import com.dmb.testriotapi.DetailedChampActivity;
import com.dmb.testriotapi.Models.Champion;
import com.dmb.testriotapi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FragmentDynInfo extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private String champName,champKey,champTitle,champImg;

    private ProgressDialog progressDialog;

    private ChampsAdapter champsAdapter;

    private OnFragmentInteractionListener mListener;

    public FragmentDynInfo() {
        // Required empty public constructor
    }

    public static FragmentDynInfo newInstance(String param1, String param2) {
        FragmentDynInfo fragment = new FragmentDynInfo();
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
        View v = inflater.inflate(R.layout.fragment_fragment_dyn_info, container, false);

        requestAllChamps();

        recyclerView = v.findViewById(R.id.recyclerChampList);

        champsAdapter = new ChampsAdapter(mListener.getChampions(), new ChampsAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(final View v, final int position) {
                Snackbar.make(v, mListener.getChampions().get(position).getName(),Snackbar.LENGTH_LONG).show();
                mListener.champDetails(position);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(champsAdapter);

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.cosasDelInfo(uri);
        }
    }

    public void requestAllChamps(){

        String url = "http://ddragon.leagueoflegends.com/cdn/8.3.1/data/es_ES/champion.json";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseAllChamps(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), getText(R.string.FalloRecuperar), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    public void parseAllChamps(String jsonString){
        try{
            JSONObject obj = new JSONObject(jsonString);
            JSONObject object = obj.getJSONObject("data");
            Iterator<String> it = object.keys();
            while(it.hasNext()){
                JSONObject champion = (JSONObject) object.get(it.next());

                    champName = champion.optString("name");
                    champKey = champion.optString("key");
                    champTitle = champion.optString("title");

                    JSONObject object1 = champion.getJSONObject("image");

                        champImg = object1.optString("full");
                        Champion champ = new Champion(champName, champKey, champTitle, champImg);
                        mListener.addChampion(champ);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        champsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener4");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void cosasDelInfo(Uri uri);
        ArrayList<Champion> getChampions();
        void addChampion(Champion champion);
        Champion getSingleChampion(int position);
        void champDetails(int position);
    }
}
