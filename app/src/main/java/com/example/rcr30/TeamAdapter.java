package com.example.rcr30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;

    public TeamAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public TeamAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teammember, parent, false);
        return new TeamAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.MyViewHolder holder, int position) {
        User user= userArrayList.get(position);

        holder.profileName.setText(user.getName());
        holder.memberRole.setText(user.getDomain());
        Glide.with(holder.profilePic).load(user.getProfilePic()).placeholder(R.drawable.placeholder).dontAnimate().into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView profileName;
        TextView memberRole;
        CircularImageView profilePic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profileName=itemView.findViewById(R.id.memberName);
            memberRole=itemView.findViewById(R.id.memberRole);
            profilePic=itemView.findViewById(R.id.proPic);
        }
    }
}
