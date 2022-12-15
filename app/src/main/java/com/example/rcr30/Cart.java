package com.example.rcr30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Cart extends AppCompatActivity {

    RecyclerView userCart;
    ImageView  Back;
    CardView PlaceOrder;
    ProgressBar pBCart;
    LinearLayoutManager linearLayoutManager;

    ArrayList<CartItem> cartItems;
    CartAdapter cartAdapter;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String UID;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        /** Initializing different views **/
        userCart=findViewById(R.id.UserCart);
        Back=findViewById(R.id.back_to_Finale);
        PlaceOrder=findViewById(R.id.OrderPlacement);
        pBCart=findViewById(R.id.CartProgress);
        pBCart.setVisibility(View.VISIBLE);

        /** Firebase initializations **/
        mAuth=FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Carts").child(user.getUid());

        /** Setting up recyclerView for userCart **/
        userCart.setHasFixedSize(true);
        linearLayoutManager= new GridLayoutManager(this,2);
        userCart.setLayoutManager(linearLayoutManager);
        cartItems = new ArrayList<>();
        cartAdapter= new CartAdapter(Cart.this,cartItems);
        userCart.setAdapter(cartAdapter);

        /** Finishes the current activity to go back to the previous one **/
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** adding CartItem to the arraylist passed to the adapter **/
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    CartItem cartItem1= dataSnapshot.getValue(CartItem.class);
                    cartItems.add(cartItem1);
                }
                cartAdapter.notifyDataSetChanged();
                pBCart.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UID=user.getUid();

        PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cart.this,QRcode.class);
                intent.putExtra("User ID",UID);
                startActivity(intent);
            }
        });



    }
}