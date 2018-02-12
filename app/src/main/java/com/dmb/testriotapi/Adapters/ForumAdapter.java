package com.dmb.testriotapi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.Models.Forum.Forum;
import com.dmb.testriotapi.Models.User;
import com.dmb.testriotapi.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Ricardo Borrull on 07/02/2018.
 */

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> implements View.OnClickListener {
    private List<Forum> items;
    private View.OnClickListener listener;
    static DatabaseReference bbdd;
    static StorageReference storageReference;

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
            img_Profile = (ImageView) v.findViewById(R.id.img_Profile);
        }

        public void bindProducto(Forum item) {
            txt_mensaje.setText(item.getTitulo());
            /*String uid = item.getUid();
            bbdd = FirebaseDatabase.getInstance().getReference().child("usuario");
            Query q = bbdd.orderByKey().equalTo(uid);
            q.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                        if (dataSnapshot.getValue(User.class).getProfileImage() != null) {
                            storageReference = FirebaseStorage.getInstance().getReference().child(dataSnapshot.getValue(User.class).getProfileImage());
                            Glide.with(getApplicationContext())
                                    .using(new FirebaseImageLoader())
                                    .load(storageReference)
                                    .into(img_Profile);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });*/
        }
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}

