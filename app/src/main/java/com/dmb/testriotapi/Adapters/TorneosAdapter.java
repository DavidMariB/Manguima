package com.dmb.testriotapi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dmb.testriotapi.Models.Torneo;
import com.dmb.testriotapi.R;

import java.util.List;

/**
 * Created by Ricardo Borrull on 14/02/2018.
 */

public class TorneosAdapter extends RecyclerView.Adapter<TorneosAdapter.TorneoViewHolder> implements View.OnClickListener {
    private List<Torneo> items;
    private View.OnClickListener listener;

    public TorneosAdapter(List<Torneo> items) {
        this.items = items;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public TorneosAdapter.TorneoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cv_post, viewGroup, false);
        v.setOnClickListener(this);
        return new TorneosAdapter.TorneoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TorneosAdapter.TorneoViewHolder viewHolder, int i) {
        Torneo item = items.get(i);
        viewHolder.bindTorneo(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class TorneoViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView txt_titulo, txt_p1, txt_p2, txt_p3, txt_p4;


        public TorneoViewHolder(View v) {
            super(v);
            txt_titulo = (TextView) v.findViewById(R.id.txt_mensaje);
            txt_p1 = (TextView) v.findViewById(R.id.txt_p1);
            txt_p2 = (TextView) v.findViewById(R.id.txt_p2);
            txt_p3 = (TextView) v.findViewById(R.id.txt_p3);
            txt_p4 = (TextView) v.findViewById(R.id.txt_p4);

        }

        public void bindTorneo(Torneo item) {
            txt_titulo.setText(item.getTitulo());

        }
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}
