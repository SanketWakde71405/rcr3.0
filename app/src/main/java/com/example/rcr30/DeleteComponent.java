package com.example.rcr30;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class DeleteComponent extends AppCompatActivity {

    RecyclerView delete_recycler;
    ImageView Back2Admin;
    ProgressBar progressBarDelete;
    SearchView deleteSearch;
    DatabaseReference reference;
    DeleteComponentAdapter componentAdapter;
    ArrayList<Component> componentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_component);

        /** Initializations **/
        Back2Admin=findViewById(R.id.back_to_admin3);
        progressBarDelete=findViewById(R.id.progressBarDelete);
        delete_recycler=findViewById(R.id.recycler_delete_component);
        deleteSearch=findViewById(R.id.searchComponentDelete);
        reference= FirebaseDatabase.getInstance().getReference("Components");
        delete_recycler.setHasFixedSize(true);
        delete_recycler.setLayoutManager(new LinearLayoutManager(this));

        /** Setting up arraylist and adapter for the recyclerView **/
        componentList= new ArrayList<>();
        componentAdapter=new DeleteComponentAdapter(this,componentList);
        delete_recycler.setAdapter(componentAdapter);

        /** Finishes the current activity to go back to the previous one **/
        Back2Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** Adding components from Electronics section of the firebase to the recyclerView **/
        reference.child("Electronics").addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Component component=dataSnapshot.getValue(Component.class);
                    componentList.add(component);
                }
                componentAdapter.notifyDataSetChanged();
                progressBarDelete.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /** Adding the components from the Mechanical section of the firebase to the recyclerView **/
        reference.child("Mechanical").addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Component component=dataSnapshot.getValue(Component.class);
                    componentList.add(component);
                }
                componentAdapter.notifyDataSetChanged();
                progressBarDelete.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        deleteSearch.clearFocus();

        deleteSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });

    }

    private void filterList(String s) {
        ArrayList<Component> filteredList = new ArrayList<>();

        for (Component component: componentList){
            if (component.getCompName().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(component);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(DeleteComponent.this, "No component found ", Toast.LENGTH_SHORT).show();
        }

        else {
           componentAdapter.setFilteredList(filteredList);
        }
    }
}