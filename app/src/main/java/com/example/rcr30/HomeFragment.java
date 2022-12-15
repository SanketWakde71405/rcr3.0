package com.example.rcr30;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    ItemAdapter itemAdapter,itemAdapter2;
    ArrayList<Component> components,componentArrayList;

    RecyclerView.LayoutManager layoutManager;
    CardView Electronic,Mechanical;
    RecyclerView exploreSection,likeSection;
    View view;

    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home, container, false);
        Electronic=view.findViewById(R.id.ElectronicComponents);
        Mechanical=view.findViewById(R.id.MechanicalComponents);
        exploreSection=view.findViewById(R.id.exploreRecycler);
        likeSection=view.findViewById(R.id.likeRecycler);
        components= new ArrayList<>();
        componentArrayList= new ArrayList<>();

        /** tarts activity ElectronicComponents **/
        Electronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),ElectronicComponents.class);
                startActivity(intent);
            }
        });

        /** Starts the activity MechanicalComponents **/
        Mechanical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(),MechanicalComponents.class);
                startActivity(intent);
            }
        });

        reference= FirebaseDatabase.getInstance().getReference("Components");

        /** setting the explore section recyclerView **/
        exploreSection.setHasFixedSize(true);
        layoutManager= new GridLayoutManager(getContext(),2);
        exploreSection.setLayoutManager(layoutManager);

        reference.child("Electronics").limitToLast(4).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Component component= dataSnapshot.getValue(Component.class);
                    components.add(component);
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        itemAdapter= new ItemAdapter(getContext(),components);
        exploreSection.setAdapter(itemAdapter);

        /** setting the like section recyclerView **/
        likeSection.setHasFixedSize(true);
        likeSection.setLayoutManager(new GridLayoutManager(getContext(),2));

        reference.child("Mechanical").limitToLast(4).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Component component= dataSnapshot.getValue(Component.class);
                    componentArrayList.add(component);
                }
                itemAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        itemAdapter2= new ItemAdapter(getContext(),componentArrayList);
        likeSection.setAdapter(itemAdapter2);

        return view;
    }
}