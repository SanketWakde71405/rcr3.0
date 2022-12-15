package com.example.rcr30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    ImageView moreFragment;
    TextView returnedComponents;
    TextView toReturnComponents;
    RecyclerView HistoryRecycler;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    int yesQueryNumber,noQueryNumber;
    String UID;
    ArrayList<Orders> ordersArrayList;
    HistoryAdapter historyAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        moreFragment=findViewById(R.id.moreFragmentOpener);
        returnedComponents=findViewById(R.id.historyReturn);
        toReturnComponents=findViewById(R.id.historyToReturn);
        HistoryRecycler=findViewById(R.id.historyRecycler);


        returnedComponents.setText("0");
        toReturnComponents.setText("0");

        ordersArrayList= new ArrayList<>();
        HistoryRecycler.setHasFixedSize(true);
        HistoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        reference= FirebaseDatabase.getInstance().getReference("Orders");
        mAuth=FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        assert user != null;
        UID=user.getUid();

        reference.child(UID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Orders order= dataSnapshot.getValue(Orders.class);
                    ordersArrayList.add(order);
                }
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        historyAdapter= new HistoryAdapter(History.this,ordersArrayList);
        HistoryRecycler.setAdapter(historyAdapter);


        reference.child(UID).orderByChild("returningStatus").equalTo("YES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                      Orders order= dataSnapshot.getValue(Orders.class);
                      if (order!= null){
                          yesQueryNumber+=order.getCartItems().size();
                      }
                  }
                returnedComponents.setText(Integer.toString(yesQueryNumber));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.child(UID).orderByChild("returningStatus").equalTo("NO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Orders order= dataSnapshot.getValue(Orders.class);
                    if (order!= null){
                        noQueryNumber+=order.getCartItems().size();
                    }
                }
                toReturnComponents.setText(Integer.toString(noQueryNumber));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        moreFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}