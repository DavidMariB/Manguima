package com.dmb.testriotapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import java.util.Locale;
import android.content.res.Configuration;
import android.widget.Toast;

import com.dmb.testriotapi.R;

public class ConfigActivity extends MainActivity {

    ImageButton btnEspanol, btnCatalan, btnIngles;

    Button btnWeb, btnSalir;

    Integer idioma;

    Locale locale;

    Configuration config = new Configuration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        idioma = 0;

        btnEspanol = (ImageButton) findViewById(R.id.tvEspanol);
        btnCatalan = (ImageButton) findViewById(R.id.tvCatalan);
        btnIngles = (ImageButton) findViewById(R.id.tvIngles);

        btnWeb = (Button) findViewById(R.id.btWeb);
        btnSalir = (Button) findViewById(R.id.btSalir);


        btnEspanol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idioma = 1;
                CambiarIdioma();

            }
        });

        btnCatalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idioma = 2;
                CambiarIdioma();

            }
        });

        btnIngles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idioma = 3;
                CambiarIdioma();
            }
        });
    }

    private void CambiarIdioma(){

        if(idioma == 1){

            locale = new Locale("es");
            config.locale = locale;

            Toast.makeText(this, "Idioma cambiado al Español", Toast.LENGTH_SHORT).show();

        }else if (idioma ==2){

            locale = new Locale("ca");
            config.locale =locale;

            Toast.makeText(this, "Idioma cambiado al Valenciano", Toast.LENGTH_SHORT).show();

        }else if(idioma == 3){

            locale = new Locale("en");
            config.locale =locale;

            Toast.makeText(this, "Idioma cambiado al Ingles", Toast.LENGTH_SHORT).show();

        }

        getResources().updateConfiguration(config, null);
        Intent refresh = new Intent(ConfigActivity.this, ConfigActivity.class);
        startActivity(refresh);
        finish();
    }
}
