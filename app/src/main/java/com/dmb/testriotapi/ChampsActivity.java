package com.dmb.testriotapi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.dmb.testriotapi.Adapters.ChampsAdapter;

public class ChampsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String champName,champKey,champTitle,champImg;

    private ProgressDialog progressDialog;

    private ChampsAdapter champsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champs);


    }
}
