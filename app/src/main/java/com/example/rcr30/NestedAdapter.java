package com.example.rcr30;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.MyViewHolder> {

    ArrayList<String> componentNames;
    ArrayList<CartItem> componentArrayList;

    public NestedAdapter(ArrayList<String> componentNames, ArrayList<CartItem> componentArrayList) {
        this.componentNames = componentNames;
        this.componentArrayList = componentArrayList;
    }

    @NonNull
    @Override
    public NestedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nested, parent, false);
        return new NestedAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NestedAdapter.MyViewHolder holder, int position) {

        CartItem cartItem= componentArrayList.get(position);
        String ComponentName= componentNames.get(position);

        Glide.with(holder.circularImageView).load(cartItem.getImageUrl()).placeholder(R.drawable.placeholder).dontAnimate().into(holder.circularImageView);
        holder.name.setText(ComponentName);
        holder.qty.setText(Integer.toString(cartItem.getQuantity()));



    }

    @Override
    public int getItemCount() {
        return componentArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CircularImageView circularImageView;
        TextView name;
        TextView qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circularImageView=itemView.findViewById(R.id.imagePic);
            name=itemView.findViewById(R.id.nameComponent);
            qty=itemView.findViewById(R.id.QTY);
        }
    }
}
