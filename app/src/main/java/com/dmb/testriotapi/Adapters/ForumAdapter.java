package com.dmb.testriotapi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmb.testriotapi.Models.Forum.Forum;
import com.dmb.testriotapi.R;

import java.util.List;

/**
 * Created by Ricardo Borrull on 07/02/2018.
 */

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> implements View.OnClickListener {
    private List<Forum> items;
    private View.OnClickListener listener;

    public ForumAdapter(List<Forum> items) {
        this.items = items;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ForumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cv_post, viewGroup, false);
        v.setOnClickListener(this);
        return new ForumViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ForumViewHolder viewHolder, int i) {
        Forum item = items.get(i);
        viewHolder.bindProducto(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ForumViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView txt_mensaje, txt_comment, txt_like;
        public ImageView img_Profile;


        public ForumViewHolder(View v) {
            super(v);
            txt_mensaje = (TextView) v.findViewById(R.id.txt_mensaje);
            txt_comment = (TextView) v.findViewById(R.id.txt_comment);
            txt_like = (TextView) v.findViewById(R.id.txt_like);
        }

        public void bindProducto(Forum item) {
            txt_mensaje.setText(item.getTitulo());
        }
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}
