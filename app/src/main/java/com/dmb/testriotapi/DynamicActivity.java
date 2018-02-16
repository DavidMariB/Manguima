package com.dmb.testriotapi;

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
import com.dmb.testriotapi.Models.Champion;

import java.util.ArrayList;

public class DynamicActivity extends MainActivity implements
FragmentDynForo.OnFragmentInteractionListener,FragmentDynInfo.OnFragmentInteractionListener{

    private ViewPager viewPager;
    private PagerAdapter mPagerAdapter;

    private ArrayList<Champion> champions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

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
    public void cosasDelInfo(Uri uri) {

    }

    @Override
    public ArrayList<Champion> getChampions() {
        return this.champions;
    }

    @Override
    public void addChampion(Champion champion) {
            this.champions.add(champion);
    }

    @Override
    public Champion getSingleChampion(int position) {
        return this.champions.get(position);
    }

    @Override
    public void champDetails(int position) {
        Champion champion = this.champions.get(position);

        Intent intent = new Intent(this,DetailedChampActivity.class);
        intent.putExtra("champion",champion);
        startActivity(intent);
    }

    @Override
    public void creaVentanaNuevoTema() {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
            implements FragmentDynNoticias.OnFragmentInteractionListener, FragmentDynForo.OnFragmentInteractionListener,
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
                    currentFragment = FragmentDynNoticias.newInstance(null, null);
                    break;
                case 1:
                    currentFragment = FragmentDynForo.newInstance(null, null);
                    break;
                case 2:
                    currentFragment = FragmentDynTorneos.newInstance(null, null);
                    break;
                case 3:
                    currentFragment = FragmentDynInfo.newInstance(null, null);
                    break;
            }
            return currentFragment;

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public void onFragmentInteraction(Uri uri) {

        }

        @Override
        public void cosasDelTorneo(Uri uri) {

        }

        @Override
        public void creaVentanaNuevoTema() {

        }
    }
}
