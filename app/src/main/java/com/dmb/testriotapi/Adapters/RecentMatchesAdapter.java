package com.dmb.testriotapi.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmb.testriotapi.Models.Match;
import com.dmb.testriotapi.R;

import java.util.List;

/**
 * Created by davidmari on 6/2/18.
 */

public class RecentMatchesAdapter extends RecyclerView.Adapter<RecentMatchesAdapter.MatchViewHolder> {

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView champName;
        TextView matchLane;
        TextView matchResult;
        TextView matchScore;
        ImageView champIcon;

        MatchViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.matchInfoCard);
            champName = itemView.findViewById(R.id.tvChampName);
            matchLane = itemView.findViewById(R.id.tvMatchLane);
            matchResult = itemView.findViewById(R.id.tvMatchResult);
            matchScore = itemView.findViewById(R.id.tvMatchScore);
            champIcon = itemView.findViewById(R.id.imgChampIcon);
        }
    }

    List<Match> matches;

    public RecentMatchesAdapter(List<Match> matches){
        this.matches = matches;
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_matches, viewGroup, false);
        MatchViewHolder mvh = new MatchViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MatchViewHolder matchViewHolder,final int position) {
        Match match = matches.get(position);
        matchViewHolder.champName.setText(match.getChampName());
        matchViewHolder.matchLane.setText(match.getLane());
        matchViewHolder.matchResult.setText(match.getResult());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
