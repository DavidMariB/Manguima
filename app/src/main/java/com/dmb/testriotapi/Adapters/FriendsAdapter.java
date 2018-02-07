package com.dmb.testriotapi.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmb.testriotapi.Models.Friend;
import com.dmb.testriotapi.R;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView friendName;
        TextView friendUsername;
        TextView friendSurname;
        TextView friendConected;
        ImageView imgFriend;
        ImageView imgFriendConected;

            FriendViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.friendInfoCard);
            friendName = itemView.findViewById(R.id.tvFriendName);
            friendUsername = itemView.findViewById(R.id.tvFriendUserName);
            friendSurname = itemView.findViewById(R.id.tvFriendSurname);
            friendConected = itemView.findViewById(R.id.tvFriendConected);
            imgFriend = itemView.findViewById(R.id.imgFriendIcon);
            imgFriendConected = itemView.findViewById(R.id.imgFriendConected);
        }
    }

    List<Friend> friends;

    public FriendsAdapter(List<Friend> friends){
        this.friends = friends;
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_friends, viewGroup, false);
        FriendViewHolder fvh = new FriendViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder friendViewHolder,final int position) {
        Friend friend = friends.get(position);
        friendViewHolder.friendName.setText(friend.getName());
        friendViewHolder.friendUsername.setText(friend.getUsername());
        friendViewHolder.friendSurname.setText(friend.getSurname());
        friendViewHolder.friendConected.setText(friend.getConectado());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
