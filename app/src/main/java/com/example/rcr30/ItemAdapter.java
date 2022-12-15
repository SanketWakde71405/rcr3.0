package com.example.rcr30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<Component> ComponentList;
    public ItemAdapter(Context context, ArrayList<Component> componentList) {
        this.context = context;
        this.ComponentList = componentList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<Component> filteredList){
        this.ComponentList=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cardview, parent, false);
        return new ItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        Component component = ComponentList.get(position);

        holder.TextItem.setText(component.getCompName());
        Glide.with(holder.ImageItem).load(component.getImageURL()).placeholder(R.drawable.placeholder).dontAnimate().into(holder.ImageItem);
        holder.Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,Final.class);
                intent.putExtra("Component Name",component.getCompName());
                intent.putExtra("Component Specifications",component.getSpecs());
                intent.putExtra("Remaining Components",Integer.toString(component.getWork()));
                intent.putExtra("Component image",component.getImageURL());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() { return ComponentList.size(); }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ImageItem;
        TextView TextItem;
        CardView Item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageItem= itemView.findViewById(R.id.ItemImage);
            TextItem=itemView.findViewById(R.id.itemName);
            Item=itemView.findViewById(R.id.item);
        }
    }
}
