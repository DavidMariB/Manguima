package com.dmb.testriotapi.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dmb.testriotapi.DetailedChampActivity;
import com.dmb.testriotapi.Models.Skin;
import com.dmb.testriotapi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by davidmari on 15/2/18.
 */

public class SlideSkinsAdapter extends PagerAdapter {

    private static final String TAG = "ImageViewPage";
    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<Skin> skins;
    private DetailedChampActivity dca = new DetailedChampActivity();
    private String champName,skinNumber;

    public SlideSkinsAdapter(Context context,String champName, List<Skin> skins) {
        mContext = context;
        this.champName = champName;
        this.skinNumber = skinNumber;
        this.skins = skins;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return skins.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_skin, container, false);

        final ImageView imgSkin = itemView.findViewById(R.id.imgSlideSkin);

        Picasso.with(mContext).load("http://ddragon.leagueoflegends.com/" +
                "cdn/img/champion/splash/"+champName+"_"+skins.get(position).getNumber()+".jpg").into(imgSkin);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }


}
