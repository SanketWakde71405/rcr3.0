package com.example.rcr30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class UpdateComponentAdapter extends RecyclerView.Adapter<UpdateComponentAdapter.MyViewHolder> {

    Context context;
    ArrayList<Component> ComponentList;

    public UpdateComponentAdapter(Context context, ArrayList<Component> componentList) {
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.upadate_component_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Component component = ComponentList.get(position);

        holder.update_ComponentName.setText(component.getCompName());
        holder.update_Working.setText(Integer.toString(component.getWork()));
        holder.update_Available.setText(Integer.toString(component.getAvail()));
        holder.update_Non_Working.setText(Integer.toString(component.getNon_work()));
        Picasso.get().setLoggingEnabled(true);
//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(component.getImageURL()).getContent());
//            holder.update_ComponentImage.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//          Picasso.get().load(component.getImageURL()).resize(50,50).into(holder.update_ComponentImage);
        Glide.with(holder.update_ComponentImage).load(component.getImageURL()).placeholder(R.drawable.placeholder).dontAnimate().into(holder.update_ComponentImage);
//        Glide.with(context).load(component.getImageURL()).dontAnimate().into(holder.update_ComponentImage);

        holder.updateComponentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update= new Intent(context,UpdateComponent1.class);
                update.putExtra("Component Name",component.getCompName());
                update.putExtra("Available",component.getAvail());
                update.putExtra("Working",component.getWork());
                update.putExtra("Not Working",component.getNon_work());
                update.putExtra("Specifications",component.getSpecs());
                update.putExtra("Component Image",component.getImageURL());
                context.startActivity(update);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ComponentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView update_Available, update_Working, update_Non_Working, update_ComponentName;
        ImageView update_ComponentImage;
        CardView updateComponentCard;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            updateComponentCard=itemView.findViewById(R.id.Update_ComponentCard);
            update_ComponentName = itemView.findViewById(R.id.Update_ComponentName);
            update_Available = itemView.findViewById(R.id.Update_Available);
            update_Working = itemView.findViewById(R.id.Update_Working);
            update_Non_Working = itemView.findViewById(R.id.Update_Non_Working);
            update_ComponentImage = itemView.findViewById(R.id.Update_ComponentImage);


        }
    }
}