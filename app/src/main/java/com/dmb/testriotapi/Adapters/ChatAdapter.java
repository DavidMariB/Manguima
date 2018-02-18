package com.dmb.testriotapi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmb.testriotapi.Models.Forum.Mensaje;
import com.dmb.testriotapi.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter {

    private final static int MESSAGE_SENT = 1;
    private final static int MESSAGE_RECEIVED = 2;

    private ArrayList<Mensaje> messages;
    private FirebaseUser currentUser;
    private String userPic, targetPic, userName, targetName;
    private Context context;

    public ChatAdapter(ArrayList<Mensaje> messages, String userPic, String targetPic, String userName,
                       String targetName, Context context){
        this.messages = messages;
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.userPic = userPic;
        this.targetPic = targetPic;
        this.userName = userName;
        this.targetName = targetName;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje m = messages.get(position);

        if (m.getUid().equals(currentUser.getUid())) {

            return MESSAGE_SENT;
        } else {

            return MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        if (i == MESSAGE_SENT) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensaje_enviado, viewGroup, false);
            Log.d("chat inflater", "inflando layout enviado " + i);
            return new ChatViewHolder(v);
        } else if (i == MESSAGE_RECEIVED){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensaje_recibido, viewGroup, false);
            Log.d("chat inflater", "inflando layout recibido " + i);
            return new RecibedMessageHolder(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Mensaje m = messages.get(position);

        switch (viewHolder.getItemViewType()) {
            case MESSAGE_SENT:
                ((ChatViewHolder) viewHolder).bindView(m, userName, userPic, getTime(), context);
                break;
            case MESSAGE_RECEIVED:
                ((RecibedMessageHolder) viewHolder).bindView(m, targetName, targetPic, getTime(), context);
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public String getTime() {

        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate=dateFormat.format(date);

        return formattedDate;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tCurrentUser;
        TextView tCurrentName;
        TextView time;
        CircleImageView userPic;

        public ChatViewHolder(View itemView) {
            super(itemView);
            tCurrentUser = itemView.findViewById(R.id.tCurrentUserChat);
            tCurrentName = itemView.findViewById(R.id.txt_userU);
            time = itemView.findViewById(R.id.text_message_timeU);
            userPic = itemView.findViewById(R.id.img_ProfileU);
        }

        public void bindView (Mensaje m, String name, String pic, String time, Context context) {

            tCurrentName.setText(name);
            tCurrentUser.setText(m.getText());
            this.time.setText(time);

            if (pic != null) {

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(pic);
                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(userPic);
            } else {

                Picasso.with(context).load(R.mipmap.default_avatar).into(userPic);
            }

        }
    }

    public static class RecibedMessageHolder extends RecyclerView.ViewHolder {
        TextView tTargetUser;
        TextView tTargetName;
        TextView time;
        CircleImageView targetPic;

        public RecibedMessageHolder(View itemView) {
            super(itemView);
            tTargetUser = itemView.findViewById(R.id.tTargetUserChat);
            tTargetName = itemView.findViewById(R.id.txt_user);
            time = itemView.findViewById(R.id.text_message_time);
            targetPic = itemView.findViewById(R.id.img_Profile);
        }

        public void bindView (Mensaje m, String name, String pic, String time, Context context) {

            tTargetUser.setText(m.getText());

            if (!m.getUid().equals("ElBuenoDeManguima")) {
                this.time.setText(time);
                tTargetName.setText(name);

                if (pic != null) {

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(pic);
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(targetPic);
                } else {

                    Picasso.with(context).load(R.mipmap.default_avatar).into(targetPic);
                }
            } else {

                this.time.setText(time);
                tTargetName.setText("Manguima");
                Picasso.with(context).load(R.drawable.icono).into(targetPic);
            }
        }
    }
}