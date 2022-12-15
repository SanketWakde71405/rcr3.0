package com.example.rcr30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Team extends AppCompatActivity {

    RecyclerView finalYearList,thirdYearList,secondYearList,firstYearList;
    ImageView backToIndex;
    ArrayList<User> finalUsers,thirdUsers,secondUsers,firstUsers;
    DatabaseReference reference;
    TeamAdapter teamAdapter,thirdAdapter,secondAdapter,firstAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        /** Recycler view initialization **/
        finalYearList= findViewById(R.id.finalYearView);
        thirdYearList= findViewById(R.id.thirdYearView);
        secondYearList=findViewById(R.id.secondYearView);
        firstYearList=findViewById(R.id.firstYearView);
        backToIndex=findViewById(R.id.back_to_index);
        /** Fixing the size of a recycler view **/
        finalYearList.setHasFixedSize(true);
        thirdYearList.setHasFixedSize(true);
        secondYearList.setHasFixedSize(true);
        firstYearList.setHasFixedSize(true);

        /** setting Layout Manager **/
        finalYearList.setLayoutManager(new LinearLayoutManager(this));
        thirdYearList.setLayoutManager(new LinearLayoutManager(this));
        secondYearList.setLayoutManager(new LinearLayoutManager(this));
        firstYearList.setLayoutManager(new LinearLayoutManager(this));

        reference= FirebaseDatabase.getInstance().getReference("Users");

        /** Initializing array list **/
        finalUsers= new ArrayList<>();
        thirdUsers=new ArrayList<>();
        secondUsers=new ArrayList<>();
        firstUsers=new ArrayList<>();

        /** initializing adapters for different recyclerViews **/
        teamAdapter= new TeamAdapter(Team.this,finalUsers);
        thirdAdapter= new TeamAdapter(Team.this,thirdUsers);
        secondAdapter= new TeamAdapter(Team.this,secondUsers);
        firstAdapter = new TeamAdapter(Team.this,firstUsers);

        /** setting adapters for different recyclerViews **/
        finalYearList.setAdapter(teamAdapter);
        thirdYearList.setAdapter(thirdAdapter);
        secondYearList.setAdapter(secondAdapter);
        firstYearList.setAdapter(firstAdapter);

        /** Finishes the current activity to go back to the previous one **/
        backToIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** Adding users to final Year list **/
        reference.orderByChild("Year").equalTo("Final Year").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                   finalUsers.add(user);
                }
                teamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /** Adding users to Third Year list **/
        reference.orderByChild("Year").equalTo("TY").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                     User user = dataSnapshot.getValue(User.class);
                     thirdUsers.add(user);
                }
                thirdAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /** Adding users to Second Year List **/
        reference.orderByChild("Year").equalTo("SY").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    secondUsers.add(user);
                }
                secondAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /** Adding users to the first Year list **/
        reference.orderByChild("Year").equalTo("FY").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    firstUsers.add(user);
                }
               firstAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}