package com.dmb.testriotapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dmb.testriotapi.Adapters.ChampsAdapter;
import com.dmb.testriotapi.Models.Champion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ChampsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String champName,champKey,champTitle,champImg;

    private ProgressDialog progressDialog;

    private ChampsAdapter champsAdapter;

    private ArrayList<Champion> champions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champs);

        recyclerView = findViewById(R.id.recyclerChampList);

        requestAllChamps();

        champsAdapter = new ChampsAdapter(champions, new ChampsAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(final View v, final int position) {
                Snackbar.make(v, champions.get(position).getName(),Snackbar.LENGTH_LONG).show();
                Champion champion = champions.get(position);

                Intent intent = new Intent(ChampsActivity.this,DetailedChampActivity.class);
                intent.putExtra("champion",champion);
                startActivity(intent);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        GridLayoutManager gm = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gm);

        recyclerView.setAdapter(champsAdapter);

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
                Toast.makeText(getApplicationContext(), getText(R.string.FalloRecuperar), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
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
                champions.add(champ);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        champsAdapter.notifyDataSetChanged();

    }
}
