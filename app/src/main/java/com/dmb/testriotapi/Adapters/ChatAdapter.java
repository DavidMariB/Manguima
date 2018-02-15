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

public class ChatAdapter extends RecyclerView.Adapter {

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        if (i == MESSAGE_SENT) {
             v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensaje_recibido, viewGroup, false);
            Log.d("chat inflater", "inflando layout enviado " + i);
            return new ChatViewHolder(v);
        } else if (i == MESSAGE_RECEIVED){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mensaje_enviado, viewGroup, false);
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
                ((ChatViewHolder) viewHolder).bindView(m);
                break;
            case MESSAGE_RECEIVED:
                ((RecibedMessageHolder) viewHolder).bindView(m);
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tCurrentUser;

        public ChatViewHolder(View itemView) {
            super(itemView);
            tCurrentUser = itemView.findViewById(R.id.tTargetUserChat);
        }

        public void bindView (Mensaje m) {

                tCurrentUser.setText(m.getText());
        }
    }

    public static class RecibedMessageHolder extends RecyclerView.ViewHolder {
        TextView tTargetUser;

        public RecibedMessageHolder(View itemView) {
            super(itemView);
            tTargetUser = itemView.findViewById(R.id.tCurrentUserChat);
        }

        public void bindView (Mensaje m) {

            tTargetUser.setText(m.getText());
        }
    }
}
