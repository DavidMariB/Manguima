package com.dmb.testriotapi.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmb.testriotapi.Models.Forum.Mensaje;
import com.dmb.testriotapi.Models.Friend;
import com.dmb.testriotapi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

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
    public ChatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false);
        ChatViewHolder cvh = new ChatViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder chatViewHolder, final int position) {
        Mensaje message = messages.get(position);
        if (message.getUid().equals(currentUser.getUid())) {

            chatViewHolder.tTargetUser.setVisibility(View.INVISIBLE);
            chatViewHolder.tCurrentUser.setVisibility(View.VISIBLE);
            chatViewHolder.tCurrentUser.setText(message.getText());

        } else {

            chatViewHolder.tCurrentUser.setVisibility(View.INVISIBLE);
            chatViewHolder.tTargetUser.setVisibility(View.VISIBLE);
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
            tTargetUser = itemView.findViewById(R.id.tTargetUser);
            tCurrentUser = itemView.findViewById(R.id.tCurrentUser);
        }

    }
}
