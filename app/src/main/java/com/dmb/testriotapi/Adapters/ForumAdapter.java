package com.dmb.testriotapi.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.DynamicActivity;
import com.dmb.testriotapi.Models.Forum.Forum;
import com.dmb.testriotapi.Models.User;
import com.dmb.testriotapi.R;
import com.facebook.FacebookSdk;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

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
        viewHolder.bindForum(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ForumViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView txt_mensaje, txt_comment, txt_like, txt_user2;
        public ImageView img_Profile;


        public ForumViewHolder(View v) {
            super(v);
            txt_mensaje = (TextView) v.findViewById(R.id.txt_mensaje);
            txt_comment = (TextView) v.findViewById(R.id.txt_comment);
            txt_like = (TextView) v.findViewById(R.id.txt_like);
            txt_user2 = (TextView) v.findViewById(R.id.txt_user2);
            img_Profile = (ImageView) v.findViewById(R.id.img_Profile);
        }

        public void bindForum(Forum item) {
            //txt_user2.setText(item.get);
            txt_mensaje.setText(item.getTitulo());
            String cmt = String.valueOf(item.getComentarios().size());
            txt_comment.setText("(" + cmt + ")");
            String lk = String.valueOf(item.getLikes().size());
            txt_like.setText("(" + lk + ")");
            final Context c = img_Profile.getContext();

            DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference().child("usuarios").child(item.getUid());

            bbdd.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue(User.class).getProfileImage() != null) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(dataSnapshot.getValue(User.class).getProfileImage());
                        Glide.with(c)
                                .using(new FirebaseImageLoader())
                                .load(storageReference)
                                .into(img_Profile);
                    } else {
                        Picasso.with(c).load(R.mipmap.default_avatar).into(img_Profile);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}

