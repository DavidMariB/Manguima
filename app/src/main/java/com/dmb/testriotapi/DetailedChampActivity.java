package com.dmb.testriotapi;

import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dmb.testriotapi.Adapters.SlideSkinsAdapter;
import com.dmb.testriotapi.Models.Champion;
import com.dmb.testriotapi.Models.Skin;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailedChampActivity extends AppCompatActivity {

    private ImageView imgChampIcon;
    private TextView tvChampName,tvChampTitle;
    private Button btnChampLore;
    private ViewPager viewPager;

    private String apiKey,gameVersion,getChampKey,getChampLore,getChampName,getSkin,getSkinName;

    private ArrayList<Skin> skins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_champ);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.negro));
        getWindow().setStatusBarColor(getResources().getColor(R.color.negro));

        imgChampIcon = findViewById(R.id.detailedChampIcon);
        tvChampName = findViewById(R.id.detailedChampName);
        tvChampTitle = findViewById(R.id.detailedChampTitle);
        btnChampLore = findViewById(R.id.detailedChampLore);
        viewPager = findViewById(R.id.skinsSlider);

        apiKey = "RGAPI-29f00e71-10ee-4816-9dd8-f7f5429b6b34";
        gameVersion = "8.3.1";

        getChampionData();

        btnChampLore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailedChampActivity.this);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialog_champ_lore, null))
                        .setMessage(getChampLore)
                        .setTitle(tvChampName.getText().toString()+" Lore")
                        // Add action buttons
                        .setPositiveButton(getText(R.string.VolverCampeon), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.show();
            }
        });
    }

    public void getChampionData(){
        Champion champion = getIntent().getParcelableExtra("champion");
        tvChampName.setText(champion.getName());
        tvChampTitle.setText(champion.getTitle());
        getChampKey = champion.getKey();
        getChampName = champion.getName();
        Picasso.with(getApplicationContext()).load("https://ddragon.leagueoflegends.com/cdn/"+gameVersion+"/img/champion/"+champion.getImage()).into(imgChampIcon);
        getChampionLore();
        getChampionSkins();
    }

    public void getChampionLore(){
        String url = "https://euw1.api.riotgames.com/lol/static-data/v3/champions/" +
                getChampKey+"?locale=es_ES&champData=lore&version="+gameVersion+"&api_key="+apiKey;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseChampionLore(string);
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

    public void parseChampionLore(String jsonString){
        try {
            JSONObject object = new JSONObject(jsonString);
            getChampLore = object.optString("lore");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getChampionSkins(){
        String url = "https://euw1.api.riotgames.com/lol/static-data/v3/champions/" +
                getChampKey+"?locale=es_ES&champData=skins&version="+gameVersion+"&tags=skins&api_key="+apiKey;


        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseChampionSkins(string);
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

    public void parseChampionSkins(String jsonString){
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray array = object.getJSONArray("skins");
            for (int i=0;i<array.length();i++){
                JSONObject object1 = array.getJSONObject(i);
                getSkinName = object1.optString("name");
                getSkin = object1.optString("num");

                Skin skin = new Skin(getSkinName,getSkin);
                skins.add(skin);

                SlideSkinsAdapter slideSkinsAdapter = new SlideSkinsAdapter(getApplicationContext(),getChampName,skins);
                viewPager.setAdapter(slideSkinsAdapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
