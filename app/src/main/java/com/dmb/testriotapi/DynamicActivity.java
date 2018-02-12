package com.dmb.testriotapi;

import android.graphics.SweepGradient;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dmb.testriotapi.Fragments.FragmentDynForo;
import com.dmb.testriotapi.Fragments.FragmentDynInfo;
import com.dmb.testriotapi.Fragments.FragmentDynNoticias;
import com.dmb.testriotapi.Fragments.FragmentDynTorneos;
import com.dmb.testriotapi.Fragments.NuevoTemaFragment;

import java.util.ArrayList;

public class DynamicActivity extends MainActivity implements NuevoTemaFragment.OnFragmentInteractionListener,
FragmentDynForo.OnFragmentInteractionListener{

    private ViewPager viewPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        //Creacion del ViewPager
        viewPager = (ViewPager) findViewById(R.id.dynamicPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void creaVentanaNuevoTema() {

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        NuevoTemaFragment f = NuevoTemaFragment.newInstance(null, null);
        ft.add(f, "fragment_tema");
        ft.addToBackStack(null);
        ft.commit();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
            implements FragmentDynNoticias.OnFragmentInteractionListener, FragmentDynForo.OnFragmentInteractionListener,
            FragmentDynTorneos.OnFragmentInteractionListener, FragmentDynInfo.OnFragmentInteractionListener{

        private Fragment currentFragment;

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
        public void cosasDelInfo(Uri uri) {

        }

        @Override
        public void creaVentanaNuevoTema() {

        }
    }
}
