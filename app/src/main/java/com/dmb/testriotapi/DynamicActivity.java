package com.dmb.testriotapi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.dmb.testriotapi.Fragments.FragmentDynForo;
import com.dmb.testriotapi.Fragments.FragmentDynInfo;
import com.dmb.testriotapi.Fragments.FragmentDynNoticias;
import com.dmb.testriotapi.Fragments.FragmentDynTorneos;
import com.dmb.testriotapi.LeagueOfLegends.SummonerInfoFragment;
import com.dmb.testriotapi.Models.Champion;

import java.util.ArrayList;

public class DynamicActivity extends MainActivity implements
FragmentDynForo.OnFragmentInteractionListener,FragmentDynInfo.OnFragmentInteractionListener ,
        SummonerInfoFragment.OnFragmentInteractionListener{

    private static ViewPager viewPager;
    private PagerAdapter mPagerAdapter;

    private ArrayList<Champion> champions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        setIsInDynamicTrue();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Creacion del ViewPager
        viewPager = (ViewPager) findViewById(R.id.dynamicPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        setIsInDynamicTrue();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setIsInDynamicTrue();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setIsInDynamicFalse();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setIsInDynamicFalse();
    }

    @Override
    public void cosasDelInfo(Uri uri) {

    }

    @Override
    public void creaVentanaNuevoTema() {

    }

    public static void setCurrentPage (int pagina) {

        viewPager.setCurrentItem(pagina);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public ArrayList<Champion> getChampions() {
        return null;
    }

    @Override
    public String getApiKey() {
        return null;
    }

    @Override
    public String getGameVersion() {
        return null;
    }

    @Override
    public void setRegion(String region) {

    }

    @Override
    public void setAccountID(String account) {

    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
            implements FragmentDynForo.OnFragmentInteractionListener,
            FragmentDynTorneos.OnFragmentInteractionListener{

        private Fragment currentFragment;

        private ArrayList<Champion> champions = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //Obtenemos la posicion en la que esta el usuario y en funcion cargamos un fragment o
            //otro.
            switch (position) {

                case 0:
                    currentFragment = FragmentDynForo.newInstance(null, null);
                    break;
                case 1:
                    currentFragment = FragmentDynTorneos.newInstance(null, null);
                    break;
                case 2:
                    currentFragment = FragmentDynInfo.newInstance(null, null);
                    break;
            }
            return currentFragment;

        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public void cosasDelTorneo(Uri uri) {

        }

        @Override
        public void creaVentanaNuevoTema() {

        }
    }
}
