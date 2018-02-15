package com.dmb.testriotapi.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dmb.testriotapi.Models.Forum.Mensaje;
import com.dmb.testriotapi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final static int MESSAGE_SENT = 1;
    private final static int MESSAGE_RECEIVED = 2;

    private ArrayList<Mensaje> messages;
    private FirebaseUser currentUser;

    public ChatAdapter(ArrayList<Mensaje> messages){
        this.messages = messages;
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
    public ChatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        i = 1;
        if (i == 1) {
             v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensaje_enviado, viewGroup, false);
            Log.d("chat inflater", "inflando layout enviado " + i);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensaje_recibido, viewGroup, false);
            Log.d("chat inflater", "inflando layout recibido " + i);
        }

        ChatViewHolder cvh = new ChatViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder chatViewHolder, int position) {

        Mensaje message = messages.get(position);
        if (message.getUid().equals(currentUser.getUid())) {

            chatViewHolder.tCurrentUser.setText(message.getText());
        } else {

            chatViewHolder.tTargetUser.setText(message.getText());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tTargetUser;
        TextView tCurrentUser;

        ChatViewHolder(View itemView) {
            super(itemView);
            tTargetUser = itemView.findViewById(R.id.tTargetUserChat);
            tCurrentUser = itemView.findViewById(R.id.tCurrentUserChat);
        }
    }
}
