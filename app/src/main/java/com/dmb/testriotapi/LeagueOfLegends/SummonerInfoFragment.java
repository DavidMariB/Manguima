package com.dmb.testriotapi.LeagueOfLegends;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmb.testriotapi.Models.Champion;
import com.dmb.testriotapi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Iterator;

public class SummonerInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText etSummonerName;
    private Button btnSearchSummoner,btnRecentMatches;
    private String champName,champKey,champImg,selectedRegion,summonerName,
            summonerLevel,summonerID,summonerProfileIcon,summonerTier,summonerRank,accountID;
    private ProgressDialog progressDialog;
    private Spinner spSelectRegion;
    private CardView summonerInfoCard;
    private TextView tvSummonerName,tvSummonerLevel,tvSummonerTier;
    private ImageView imgSummonerIcon,imgSummonerTier;

    private FragmentManager fm;
    private FragmentTransaction ft;

    public SummonerInfoFragment() {
        // Required empty public constructor
    }

    public static SummonerInfoFragment newInstance(String param1, String param2) {
        SummonerInfoFragment fragment = new SummonerInfoFragment();
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
        View v = inflater.inflate(R.layout.fragment_summoner_info, container, false);

        requestAllChamps();

        etSummonerName = v.findViewById(R.id.etSummonerName);
        btnSearchSummoner = v.findViewById(R.id.btnSearchSummoner);
        spSelectRegion = v.findViewById(R.id.spSelectRegion);
        summonerInfoCard = v.findViewById(R.id.summonerInfoCard);
        tvSummonerName = v.findViewById(R.id.tvSummonerName);
        tvSummonerLevel = v.findViewById(R.id.tvSummonerLevel);
        tvSummonerTier = v.findViewById(R.id.tvSummonerTier);
        imgSummonerIcon = v.findViewById(R.id.imgSummonerIcon);
        imgSummonerTier = v.findViewById(R.id.imgSummonerTier);
        btnRecentMatches = v.findViewById(R.id.btnRecentMatches);

        selectRegion();

        btnSearchSummoner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSummonerInfo();
            }
        });

        btnRecentMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRecentMatchesFragment();
            }
        });

        return v;
    }

    public void selectRegion(){
        String[] region = {"EUW","NA","LAN","LAS"};
        spSelectRegion.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, region));

        spSelectRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                selectedRegion = parent.getItemAtPosition(position).toString();
                switch (selectedRegion){
                    case "EUW":
                        selectedRegion="euw1";
                        break;
                    case "NA":
                        selectedRegion="na1";
                        break;
                    case "LAN":
                        selectedRegion="la1";
                        break;
                    case "LAS":
                        selectedRegion="la2";
                        break;
                    default:
                        System.out.print("Lo mismo no funciona");
                        break;
                }
                mListener.setRegion(selectedRegion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void requestAllChamps(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getText(R.string.ObtnCampeones));
        progressDialog.show();

        String url = "http://ddragon.leagueoflegends.com/cdn/"+mListener.getGameVersion()+"/data/es_ES/champion.json";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseAllChamps(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), getText(R.string.FalloRecuperar), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
                champImg = champion.optString("full");
                Champion champ = new Champion(champName,champKey,champImg);
                mListener.getChampions().add(champ);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    public void requestSummonerInfo() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getText(R.string.BuscandoSummoner));
        progressDialog.show();

        String url = "https://"+selectedRegion+".api.riotgames.com/lol/summoner/v3/summoners/by-name/"+
                etSummonerName.getText().toString().replace(" ","")+"?api_key="+mListener.getApiKey();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseSummonerInfo(string);
                requestSummonerLeague();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), getText(R.string.FalloInvocador), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

    }

    public void parseSummonerInfo(String jsonString) {

        try {
            JSONObject object = new JSONObject(jsonString);
            summonerName = object.optString("name");
            summonerLevel = object.optString("summonerLevel");
            summonerID = object.optString("id");
            accountID = object.optString("accountId");
            mListener.setAccountID(accountID);
            summonerProfileIcon = object.optString("profileIconId");
            summonerInfoCard.setVisibility(View.VISIBLE);
            tvSummonerName.setText(summonerName);
            tvSummonerLevel.setText(getText(R.string.Nivel)+" "+summonerLevel);
            Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/"+mListener.getGameVersion()+
                    "/img/profileicon/"+summonerProfileIcon+".png").into(imgSummonerIcon);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.dismiss();
    }

    public void requestSummonerLeague(){
        String url = "https://"+selectedRegion+".api.riotgames.com/lol/league/v3/positions/by-summoner/"+summonerID+"?api_key="+mListener.getApiKey();


        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                if(string.length()>2){
                    parseSummonerLeague(string);

                }else{
                    tvSummonerTier.setText("Unranked");
                    imgSummonerTier.setImageResource(R.mipmap.unranked_icon);
                }
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

    public void parseSummonerLeague(String jsonString){

        try{
            JSONArray arr = new JSONArray(jsonString);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if(obj.getString("queueType").contains("SOLO") && obj != null){
                    summonerTier = obj.getString("tier");
                    summonerRank = obj.getString("rank");

                    if(obj.getString("tier").contains("BRONZE")){
                        imgSummonerTier.setImageResource(R.mipmap.bronze_icon);
                        tvSummonerTier.setText( getText(R.string.Bronce)+" "+summonerRank);
                    }else if(obj.getString("tier").contains("SILVER")){
                        imgSummonerTier.setImageResource(R.mipmap.silver_icon);
                        tvSummonerTier.setText(getText(R.string.Plata) + " " +summonerRank);
                    }else if(obj.getString("tier").contains("GOLD")){
                        imgSummonerTier.setImageResource(R.mipmap.gold_icon);
                        tvSummonerTier.setText(getText(R.string.Oro) + " " +summonerRank);
                    }else if(obj.getString("tier").contains("PLATINUM")){
                        imgSummonerTier.setImageResource(R.mipmap.platinum_icon);
                        tvSummonerTier.setText(getText(R.string.Platino) + " "+summonerRank);
                    }else if(obj.getString("tier").contains("DIAMOND")){
                        imgSummonerTier.setImageResource(R.mipmap.diamond_icon);
                        tvSummonerTier.setText(getText(R.string.Diamante)+ " "+summonerRank);
                    }else if(obj.getString("tier").contains("MASTER")){
                        imgSummonerTier.setImageResource(R.mipmap.master_icon);
                        tvSummonerTier.setText(summonerTier+" "+summonerRank);
                    }else if(obj.getString("tier").contains("CHALLENGER")){
                        imgSummonerTier.setImageResource(R.mipmap.challenger_icon);
                        tvSummonerTier.setText(summonerTier+" "+summonerRank);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        btnRecentMatches.setVisibility(View.VISIBLE);
    }

    public void callRecentMatchesFragment(){

        // He comentado esta clase por tema de errores. Paco
        /*
        fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
        ft = fm.beginTransaction();
        ft.add(R.id.mainFragment,RecentMatchesFragment.newInstance("",""));
        ft.addToBackStack(null);
        ft.commit();*/
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        ArrayList<Champion> getChampions();
        String getApiKey();
        String getGameVersion();
        void setRegion(String region);
        void setAccountID(String account);
    }

}
