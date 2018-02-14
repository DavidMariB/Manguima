package com.dmb.testriotapi.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.ComentariosActivity;
import com.dmb.testriotapi.Models.Forum.Comentario;
import com.dmb.testriotapi.Models.Forum.Forum;
import com.dmb.testriotapi.Models.User;
import com.dmb.testriotapi.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Ricardo Borrull on 13/02/2018.
 */

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ComentarioViewHolder> implements View.OnClickListener {

    private List<Comentario> items;
    private View.OnClickListener listener;

    public ComentariosAdapter(List<Comentario> items) {
        this.items = items;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ComentariosAdapter.ComentarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cv_comment, viewGroup, false);
        v.setOnClickListener(this);
        return new ComentariosAdapter.ComentarioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ComentariosAdapter.ComentarioViewHolder viewHolder, int i) {
        Comentario item = items.get(i);
        viewHolder.bindProducto(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView txt_comentario, txt_user, txt_fecha;
        public ImageView img_Profile;


        public ComentarioViewHolder(View v) {
            super(v);
            txt_comentario = (TextView) v.findViewById(R.id.txt_comentario);
            txt_user = (TextView) v.findViewById(R.id.txt_user);
            txt_fecha = (TextView) v.findViewById(R.id.txt_fecha);
            img_Profile = (ImageView) v.findViewById(R.id.img_Profile);
        }

        public void bindProducto(Comentario item) {
            DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference().child("usuarios").child(item.getUid());
            bbdd.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User u = dataSnapshot.getValue(User.class);
                    String user = u.getUserName();
                    txt_user.setText(user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            txt_fecha.setText(item.getFecha());
            txt_comentario.setText(item.getMensaje());
        }
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}
