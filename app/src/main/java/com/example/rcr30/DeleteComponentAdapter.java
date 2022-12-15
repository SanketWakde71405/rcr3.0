package com.example.rcr30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeleteComponentAdapter extends RecyclerView.Adapter<DeleteComponentAdapter.MyViewHolder> {

    Context context;
    DatabaseReference reference;
    ArrayList<Component> ComponentList;


    public DeleteComponentAdapter(Context context, ArrayList<Component> componentList) {
        this.context = context;
        ComponentList = componentList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<Component> list){
        this.ComponentList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeleteComponentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delete_component_item, parent, false);
        return new DeleteComponentAdapter.MyViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DeleteComponentAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Component component = ComponentList.get(position);

        holder.deleteComponentName.setText(component.getCompName());
        holder.deleteAvailable.setText(Integer.toString(component.getAvail()));
        holder.deleteWorking.setText(Integer.toString(component.getWork()));
        holder.deleteNonWorking.setText(Integer.toString(component.getNon_work()));
        Glide.with(holder.deleteImage).load(component.getImageURL()).placeholder(R.drawable.placeholder).dontAnimate().into(holder.deleteImage);

        holder.deleter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference= FirebaseDatabase.getInstance().getReference("Components");


                reference.child("Electronics").child(component.getCompName()).removeValue(new DatabaseReference.CompletionListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        ComponentList.remove(position);
                        notifyDataSetChanged();
                        notifyItemRemoved(position);
                    }
                });

                reference.child("Mechanical").child(component.getCompName()).removeValue(new DatabaseReference.CompletionListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        ComponentList.remove(position);
                        notifyDataSetChanged();
                        notifyItemRemoved(position+1);
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return ComponentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView deleter;
        ImageView deleteImage;
        TextView deleteComponentName;
        TextView deleteWorking;
        TextView deleteNonWorking;
        TextView deleteAvailable;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            deleter=itemView.findViewById(R.id.delete);
            deleteImage=itemView.findViewById(R.id.DeleteComponentImage);
            deleteComponentName=itemView.findViewById(R.id.DeleteComponentName);
            deleteAvailable=itemView.findViewById(R.id.delete_available);
            deleteWorking=itemView.findViewById(R.id.delete_working);
            deleteNonWorking=itemView.findViewById(R.id.delete_non_working);



        }
    }
}
