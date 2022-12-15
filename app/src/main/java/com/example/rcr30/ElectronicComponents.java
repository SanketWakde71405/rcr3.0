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

public class ElectronicComponents extends AppCompatActivity {

    RecyclerView Electronics;
    ProgressBar ElectronicProgress;
    SearchView electronicSearch;
    RecyclerView.LayoutManager layoutManager;
    ImageView back_to_index;
    DatabaseReference reference;
    ItemAdapter adapter;
    ArrayList<Component> components;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronic_components);

        /** Initializations **/
        back_to_index=findViewById(R.id.back_to_index4);
        ElectronicProgress=findViewById(R.id.progressbarElectronic);
        ElectronicProgress.setVisibility(View.VISIBLE);
        electronicSearch=findViewById(R.id.searchElectronics);
        Electronics=findViewById(R.id.electronicRecycler);
        reference= FirebaseDatabase.getInstance().getReference("Components");

        /** Setting up arraylist,layoutManager and adapter of the recyclerView **/
        Electronics.setHasFixedSize(true);
        layoutManager= new GridLayoutManager(this,2);
        Electronics.setLayoutManager(layoutManager);
        components= new ArrayList<>();
        adapter= new ItemAdapter(ElectronicComponents.this,components);
        Electronics.setAdapter(adapter);

        /** Finishes the current activity to go back to the previous one **/
        back_to_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** Getting components from electronics section from the firebase database and adding it to the recyclerView **/
        reference.child("Electronics").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Component component= dataSnapshot.getValue(Component.class);
                    components.add(component);

                }
                adapter.notifyDataSetChanged();
                ElectronicProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        electronicSearch.clearFocus();
        
        electronicSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        
        for (Component component:components){
            if (component.getCompName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(component);
            }
        }
        
        if (filteredList.isEmpty()){
            Toast.makeText(ElectronicComponents.this, "No Component found", Toast.LENGTH_SHORT).show();
        }
        else {
            adapter.setFilteredList(filteredList);
        }
    }
}