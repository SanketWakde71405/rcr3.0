package com.example.rcr30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateComponent extends AppCompatActivity {

    RecyclerView updateComponentRecycler;
    DatabaseReference reference;
    UpdateComponentAdapter upAdapter;
    ArrayList<Component> components;
    ProgressBar progressBar;
    ImageView back2Admin;
    SearchView updateSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_component);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /** Initializations **/
        progressBar=findViewById(R.id.progressBarUpdate);
        updateSearch=findViewById(R.id.searchUpdate);
        updateComponentRecycler=findViewById(R.id.recycler_update_component);
        back2Admin=findViewById(R.id.back_to_admin2);
        reference= FirebaseDatabase.getInstance().getReference("Components");

        /** Setting up updateComponentRecycler **/
        updateComponentRecycler.setHasFixedSize(true);
        updateComponentRecycler.setLayoutManager(new LinearLayoutManager(this));
        components= new ArrayList<>();
        upAdapter= new UpdateComponentAdapter(this,components);
        updateComponentRecycler.setAdapter(upAdapter);

        /** Finishes the current activity to got to the previous one **/
        back2Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** Adding components from Electronics section of firebase to the recyclerView **/
        reference.child("Electronics").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Component component=dataSnapshot.getValue(Component.class);
                    components.add(component);

                }
                progressBar.setVisibility(View.GONE);
                upAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /** Adding components from Mechanical section of firebase to the recyclerView **/
        reference.child("Mechanical").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Component component=dataSnapshot.getValue(Component.class);
                    components.add(component);

                }
                progressBar.setVisibility(View.GONE);
                upAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateSearch.clearFocus();

        updateSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void filterList(String six) {
        ArrayList<Component> filteredList= new ArrayList<>();

        for (Component component:components){
            if (component.getCompName().toLowerCase().contains(six.toLowerCase())){
                filteredList.add(component);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(UpdateComponent.this, "No Component found", Toast.LENGTH_SHORT).show();
        }
        else{
            upAdapter.setFilteredList(filteredList);
        }
    }
}