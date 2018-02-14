package com.dmb.testriotapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmb.testriotapi.Models.Champion;
import com.squareup.picasso.Picasso;

public class DetailedChampActivity extends AppCompatActivity {

    private ImageView imgChampIcon;
    private TextView tvChampName,tvChampTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_champ);

        imgChampIcon = findViewById(R.id.detailedChampIcon);
        tvChampName = findViewById(R.id.detailedChampName);
        tvChampTitle = findViewById(R.id.detailedChampTitle);

        getChampionData();
    }

    public void getChampionData(){
        Champion champion = getIntent().getParcelableExtra("champion");
        Log.e("TAG",""+champion.getImage());
        tvChampName.setText(champion.getName());
        tvChampTitle.setText(champion.getTitle());
        Picasso.with(getApplicationContext()).load("https://ddragon.leagueoflegends.com/cdn/8.3.1/img/champion/"+champion.getImage()).into(imgChampIcon);
    }
}
