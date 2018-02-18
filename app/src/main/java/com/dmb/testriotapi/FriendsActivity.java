package com.dmb.testriotapi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.Models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private DatabaseReference databaseReference;
    private DatabaseReference mUserRef;
    private static DatabaseReference mFriendsDatabase;

    private LinearLayoutManager llm;

    private FirebaseAuth mAuth;
    private static FirebaseUser mUser;

    private static ArrayList<String> friendKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");

        recyclerView = findViewById(R.id.recyclerListFriends);

        llm = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(llm);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mUser.getUid());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(FriendsActivity.this,DynamicActivity.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<User, FriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, FriendsViewHolder>(

                User.class,
                R.layout.users_single_layout,
                FriendsViewHolder.class,
                mFriendsDatabase

        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder, User user, int position) {

                friendsViewHolder.setDisplayName(user.getName());
                friendsViewHolder.setUserName(user.getUserName());
                friendsViewHolder.setUserImage(user.getProfileImage());


                final String user_id = getRef(position).getKey();

                mFriendsDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            friendsViewHolder.setUserOnline(userOnline);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(FriendsActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });

            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDisplayName(final String name){

            DatabaseReference nameReference = FirebaseDatabase.getInstance().getReference().child("usuarios");

            final TextView userNameView = mView.findViewById(R.id.user_single_name);

            mFriendsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Log.e("FRIENDS",""+dataSnapshot1.getKey());
                        String key = dataSnapshot1.getKey();
                        Log.e("KEY",""+key);
                        friendKeys.add(key);
                    }

                    Log.e("ARRAY",""+friendKeys.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /*nameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        Log.e("NAME",""+dataSnapshot1.getValue(User.class).getName());
                        userNameView.setText(dataSnapshot1.getValue(User.class).getName());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/



        }

        public void setUserName(String status){

            TextView userStatusView = mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);


        }

        public void setUserImage(final String profileIcon){

            final CircleImageView userImageView = mView.findViewById(R.id.user_single_image);

            final Context context = userImageView.getContext();

            DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("usuarios");

            dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(User.class).getProfileImage() != null){
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(dataSnapshot.getValue(User.class).getProfileImage());
                        Glide.with(context)
                                .using(new FirebaseImageLoader())
                                .load(storageReference)
                                .into(userImageView);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setUserOnline(String online_status) {
            ImageView imgConection = mView.findViewById(R.id.imgConection);
            if(online_status.equals("true")){
                imgConection.setImageResource(R.mipmap.conected);
            } else {
                imgConection.setImageResource(R.mipmap.disconected);
            }
        }

    }
}
