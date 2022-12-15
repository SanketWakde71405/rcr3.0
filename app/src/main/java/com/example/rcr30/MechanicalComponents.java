package com.example.rcr30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import java.util.Locale;

public class MechanicalComponents extends AppCompatActivity {

    RecyclerView Mechanical;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar MechanicalProgress;
    ImageView back2Index;
    SearchView mechanicalSearch;
    DatabaseReference reference;
    ItemAdapter adapter;
    ArrayList<Component> components;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanical_components);

        /** Initializations **/
        back2Index=findViewById(R.id.back_to_index2);
        MechanicalProgress=findViewById(R.id.progressbarMechanical);
        Mechanical=findViewById(R.id.MechanicalRecycler);
        mechanicalSearch=findViewById(R.id.searchMechanical);
        reference= FirebaseDatabase.getInstance().getReference("Components");

        /** Setting up layout manager, arraylist and adapter of the recyclerView **/
        Mechanical.setHasFixedSize(true);
        layoutManager= new GridLayoutManager(MechanicalComponents.this,2);
        Mechanical.setLayoutManager(layoutManager);
        components= new ArrayList<>();

        adapter= new ItemAdapter(MechanicalComponents.this,components);
        Mechanical.setAdapter(adapter);

        /** Finishes the current activity to finish the previous one **/
        back2Index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** Getting components from the mechanical section of the firebase database and add it to the recyclerView **/
        reference.child("Mechanical").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Component component= dataSnapshot.getValue(Component.class);
                    components.add(component);

                }
                adapter.notifyDataSetChanged();
                MechanicalProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mechanicalSearch.clearFocus();

        mechanicalSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void filterList(String text) {
        ArrayList<Component> filteredList = new ArrayList<>();

        for (Component component: components){
            if (component.getCompName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(component);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(MechanicalComponents.this, "No Component found", Toast.LENGTH_SHORT).show();
        }

        else{
            adapter.setFilteredList(filteredList);
        }
    }
}