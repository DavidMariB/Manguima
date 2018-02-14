package com.dmb.testriotapi.Adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmb.testriotapi.Models.Champion;
import com.dmb.testriotapi.R;

import java.util.ArrayList;

/**
 * Created by davidmari on 14/2/18.
 */

public class ChampsAdapter extends RecyclerView.Adapter<ChampsAdapter.ChampViewHolder> {

    private ArrayList<Champion> champions;
    private static ChampsAdapter.RecyclerViewOnItemClickListener recyclerViewOnClickItemListener;

    public ChampsAdapter (ArrayList<Champion> champions, @NonNull ChampsAdapter.RecyclerViewOnItemClickListener recyclerViewOnItemClickListener){
        this.champions = champions;
        this.recyclerViewOnClickItemListener = recyclerViewOnItemClickListener;
    }


    @Override
    public int getItemCount() {
        return champions.size();
    }

    @Override
    public ChampsAdapter.ChampViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_champion, viewGroup, false);
        ChampsAdapter.ChampViewHolder cvh = new ChampsAdapter.ChampViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ChampsAdapter.ChampViewHolder champViewHolder, final int position) {
        Champion champion = champions.get(position);
        champViewHolder.champName.setText(champion.getName());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ChampViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView champName;

        ChampViewHolder(View itemView) {
            super(itemView);
            champName = itemView.findViewById(R.id.tvChampName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            recyclerViewOnClickItemListener.onClick(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewOnItemClickListener{
        void onClick(View v, int position);
    }

}

