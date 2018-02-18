package com.dmb.testriotapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.Models.Friend;
import com.dmb.testriotapi.Models.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileActivity extends MainActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mUserRef;
    private ImageView mProfileImage, mStartChat;
    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;
    private Button mProfileSendReqBtn, mDeclineBtn, mChangeProfileImage, mEditUserData;
    private String username,name,surname,birthday;
    private String profilePic;

    private DatabaseReference mUsersDatabase;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mImageReference;

    private DatabaseReference mRootRef;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;

    private StorageReference storageReference;

    static final int PICK_IMAGE_REQUEST = 71;

    private FirebaseStorage fs = FirebaseStorage.getInstance();
    private StorageReference sr = fs.getReference();

    private Uri filePath;
    private String selected_user;

    private String imgRef;

    private ArrayList<Friend> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mUser.getUid());
        }
        final String user_id = getIntent().getStringExtra("user_id");

        //David creo esta variable porque la necesito
        selected_user = user_id;

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user_id);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mImageReference = FirebaseDatabase.getInstance().getReference().child("usuarios");

        mProfileImage = findViewById(R.id.profile_image);
        mProfileName = findViewById(R.id.profile_displayName);
        mProfileSendReqBtn = findViewById(R.id.profile_send_req_btn);
        mDeclineBtn = findViewById(R.id.profile_decline_btn);
        mChangeProfileImage = findViewById(R.id.profile_change_image);
        mEditUserData = findViewById(R.id.profile_edit_user_data);
        mStartChat = findViewById(R.id.pic_start_chat);

        mCurrent_state = "not_friends";

        mDeclineBtn.setVisibility(View.INVISIBLE);
        mDeclineBtn.setEnabled(false);

        if (mCurrent_user.getUid().equals(selected_user)) {

            mStartChat.setVisibility(View.INVISIBLE);
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getText(R.string.CargandoUsuario));
        mProgressDialog.setMessage(getText(R.string.EsperaUsuario));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("userName").getValue().toString();
                name = dataSnapshot.child("name").getValue().toString();
                surname = dataSnapshot.child("surname").getValue().toString();
                birthday = dataSnapshot.child("age").getValue().toString();
                mProfileName.setText(username);
                profilePic = dataSnapshot.getValue(User.class).getProfileImage();

                if (dataSnapshot.getValue(User.class).getProfileImage() != null){
                    storageReference = FirebaseStorage.getInstance().getReference().child(dataSnapshot.getValue(User.class).getProfileImage());
                    Glide.with(getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(mProfileImage);
                }else {
                    Picasso.with(getApplicationContext()).load(R.mipmap.default_avatar).into(mProfileImage);
                }


                if(mCurrent_user.getUid().equals(user_id)){

                    mDeclineBtn.setEnabled(false);
                    mDeclineBtn.setVisibility(View.INVISIBLE);

                    mProfileSendReqBtn.setEnabled(false);
                    mProfileSendReqBtn.setVisibility(View.INVISIBLE);

                }

                //--------------- EDITAR PERFIL ------------------//

                if (mCurrent_user.getUid().equals(mUsersDatabase.getKey())){

                    mChangeProfileImage.setVisibility(View.VISIBLE);
                    mEditUserData.setVisibility(View.VISIBLE);

                    mChangeProfileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chooseImage();
                        }
                    });

                    mEditUserData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editProfile();
                        }
                    });

                }


                //--------------- PETICION DE AMISTAD --------------//

                mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_id)){

                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if(req_type.equals("received")){

                                mCurrent_state = "req_received";
                                mProfileSendReqBtn.setText(getText(R.string.AceptarAmistad));

                                mDeclineBtn.setVisibility(View.VISIBLE);
                                mDeclineBtn.setEnabled(true);


                            } else if(req_type.equals("sent")) {

                                mCurrent_state = "req_sent";
                                mProfileSendReqBtn.setText(getText(R.string.CancelarAmistad));

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            }

                            mProgressDialog.dismiss();


                        } else {


                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.hasChild(user_id)){

                                        mCurrent_state = "friends";
                                        mProfileSendReqBtn.setText(getText(R.string.EliminarAmigo));

                                        mDeclineBtn.setVisibility(View.INVISIBLE);
                                        mDeclineBtn.setEnabled(false);

                                    }

                                    mProgressDialog.dismiss();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    mProgressDialog.dismiss();

                                }
                            });

                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProfileSendReqBtn.setEnabled(false);

                //--------------- SI NO SON AMIGOS -------------//

                if(mCurrent_state.equals("not_friends")){


                    Map requestMap = new HashMap();
                    requestMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id + "/request_type", "sent");
                    requestMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid() + "/request_type", "received");

                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){

                                Toast.makeText(ProfileActivity.this, getText(R.string.ErrorPeticion), Toast.LENGTH_SHORT).show();

                            } else {

                                mCurrent_state = "req_sent";
                                mProfileSendReqBtn.setText(getText(R.string.CancelarAmistad));

                            }

                            mProfileSendReqBtn.setEnabled(true);


                        }
                    });

                }


                //-------------- CANCELAR LA PETICION DE AMISTAD ---------------//

                if(mCurrent_state.equals("req_sent")){

                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    mProfileSendReqBtn.setText(getText(R.string.EnviarAmistad));

                                    mDeclineBtn.setVisibility(View.INVISIBLE);
                                    mDeclineBtn.setEnabled(false);


                                }
                            });

                        }
                    });

                }


                //------------ PETICION RECIBIDA ------------//

                if(mCurrent_state.equals("req_received")){

                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    Map friendsMap = new HashMap();
                    friendsMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id + "/date", currentDate);
                    friendsMap.put("Friends/" + user_id + "/"  + mCurrent_user.getUid() + "/date", currentDate);


                    friendsMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id, null);
                    friendsMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid(), null);


                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                            if(databaseError == null){

                                mProfileSendReqBtn.setEnabled(true);
                                mCurrent_state = "friends";
                                mProfileSendReqBtn.setText(getText(R.string.EliminarAmigo));

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            } else {

                                String error = databaseError.getMessage();

                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();


                            }

                        }
                    });

                }


                //------------ AMIGO ELIMINADO ---------//

                if(mCurrent_state.equals("friends")){

                    Map unfriendMap = new HashMap();
                    unfriendMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id, null);
                    unfriendMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid(), null);

                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                            if(databaseError == null){

                                mCurrent_state = "not_friends";
                                mProfileSendReqBtn.setText(getText(R.string.EnviarAmistad));

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            } else {

                                String error = databaseError.getMessage();

                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();


                            }

                            mProfileSendReqBtn.setEnabled(true);

                        }
                    });

                }


            }
        });

        //-----------LISTENER NUEVO CHAT----------//
        mStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startChat();
            }
        });
    }

    //---------- CAMBIO DE IMAGEN DE PERFIL ------------//

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getText(R.string.SelectImagen)), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), filePath);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage() {

        if(filePath != null){

            /*final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setTitle(getText(R.string.SubiendoImagen));
            progressDialog.show();*/

            StorageReference ref = sr.child("imagenes/"+ UUID.randomUUID().toString());
            imgRef = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), getText(R.string.ImagenSubida), Toast.LENGTH_SHORT).show();
                            setNewProfileImage();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), getText(R.string.FalloImagen)+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            //progressDialog.setMessage(getText(R.string.Subiendo) + " " + (int)progress+"%");
                        }
                    });
        }
    }

    public void setNewProfileImage(){

        mImageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = mCurrent_user.getUid();
                mImageReference.child(key).child("profileImage").setValue(imgRef);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void editProfile(){
        Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("surname",surname);
        intent.putExtra("username",username);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProfileActivity.this,UsersActivity.class);
        startActivity(i);
    }

    //-------------------INICIO NUEVO CHAT-----------------//

    public void startChat() {

        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("userPic", getCurrentUserPic());
        i.putExtra("targetPic", profilePic);
        i.putExtra("uid", selected_user);
        i.putExtra("nombre", username);
        startActivity(i);
    }


}
