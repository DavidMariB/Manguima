package com.dmb.testriotapi.LeagueOfLegends;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.dmb.testriotapi.Adapters.RecentMatchesAdapter;
import com.dmb.testriotapi.Models.Match;
import com.dmb.testriotapi.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecentMatchesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String championID,gameID,lane,champImg,champName,matchResult,kills,deaths,assists,checkChampID;
    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;

    private RecentMatchesAdapter rma;

    public RecentMatchesFragment() {
        // Required empty public constructor
    }

    public static RecentMatchesFragment newInstance(String param1, String param2) {
        RecentMatchesFragment fragment = new RecentMatchesFragment();
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
        View v = inflater.inflate(R.layout.fragment_recent_matches, container, false);

        requestLastMatches();

        recyclerView = v.findViewById(R.id.recyclerRecentMatches);

        rma = new RecentMatchesAdapter(mListener.getMatches());

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(rma);

        return v;
    }

    public void requestLastMatches(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Recuperando Partidas...");
        progressDialog.show();

        String url = "https://"+mListener.getRegion()+".api.riotgames.com/lol/match/v3/matchlists/by-account/"+mListener.getAccountID()+"/recent?api_key="+mListener.getApiKey();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseLastMatches(string);
                requestChampByID();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "No se ha podido recuperar la informacion", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    public void parseLastMatches(String jsonString){

        try{
            JSONObject object = new JSONObject(jsonString);

            JSONArray array = object.getJSONArray("matches");

            for (int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                championID = obj.getString("champion");
                gameID = obj.getString("gameId");
                lane = obj.getString("lane");

            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    public void requestChampByID(){

        String url = "https://"+mListener.getRegion()+".api.riotgames.com/lol/static-data/v3/champions/"+championID+"?locale=en_US&tags=image&api_key="+mListener.getApiKey();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseChampID(string);
                requestMatchInfo();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "No se ha podido recuperar la informacion", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    public void parseChampID(String jsonString){

        try{
            JSONObject object = new JSONObject(jsonString);
            champImg = object.getJSONObject("image").optString("full");
            champName = object.optString("name");

            Log.e("CHAMP IMG: ",""+champImg);
            Log.e("CHAMP IMG: ",""+champName);
            //Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/"+mListener.getGameVersion()+"/img/champion/"+champImg).into(champIcon);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void requestMatchInfo(){

        String url = "https://"+mListener.getRegion()+".api.riotgames.com/lol/match/v3/matches/"+gameID+"?api_key="+mListener.getApiKey();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseMatchInfo(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "No se ha podido recuperar la informacion", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }

    public void parseMatchInfo(String jsonString){
        try{
            JSONObject object = new JSONObject(jsonString);
            JSONArray array = object.getJSONArray("teams");

            for (int x=0;x<array.length();x++){
                JSONObject obj = array.getJSONObject(0);
                matchResult = obj.optString("win");

                if (matchResult.equals("Fail")){
                    matchResult = "DERROTA";
                }else {
                    matchResult = "VICTORIA";
                }
            }


            JSONArray array1 = object.getJSONArray("participants");

            for(int i=0;i<array1.length();i++){
                JSONObject obj1 = array1.getJSONObject(i);
                checkChampID = obj1.optString("championId");
                if(checkChampID.equals(championID)){
                    JSONObject object1 = obj1.getJSONObject("stats");
                    kills = object1.optString("kills");
                    deaths = object1.optString("deaths");
                    assists = object1.optString("assists");
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

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
        String getApiKey();
        ArrayList<Match> getMatches();
        String getRegion();
        String getAccountID();
        void addMatch(Match match);
    }
}
