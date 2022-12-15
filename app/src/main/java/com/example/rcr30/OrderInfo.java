package com.example.rcr30;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class OrderInfo extends AppCompatActivity {

    CircularImageView circularImageView;
    TextView UserName;
    TextView UserMail;
    TextView UserBranch;
    TextView UserYear;
    RecyclerView cartOrder;
    CardView approval;
    Dialog dateDialog;
    EditText approvalDateEt,returnDateEt;
    CardView dateSubmitter;
    MaterialCardView dateDialogCloser;

    ArrayList<CartItem> cartItems;
    CartAdapter cartAdapter;
    User user;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference reference,userReference,orderReference,decrementReference,decrementReference2;

    String UID;
    String approvalDate,returnDate;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        /** Initializations **/
        circularImageView=findViewById(R.id.orderingUser);
        UserName=findViewById(R.id.nameViewer);
        UserMail=findViewById(R.id.mailViewer);
        UserBranch=findViewById(R.id.branchViewer);
        UserYear=findViewById(R.id.yearViewer);
        cartOrder=findViewById(R.id.orderViewer);
        approval=findViewById(R.id.approvalButton);

        /** Dialog box to add approval date and return date in the order **/
        dateDialog= new Dialog(OrderInfo.this);

        dateDialog.setContentView(R.layout.datedialog);
        dateDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        approvalDateEt=dateDialog.findViewById(R.id.approvalDate);
        returnDateEt=dateDialog.findViewById(R.id.returnDate);
        dateSubmitter=dateDialog.findViewById(R.id.dateConfirm);
        dateDialogCloser=dateDialog.findViewById(R.id.cancelNegativeButton);

        UID=getIntent().getStringExtra("UserID");

        cartOrder.setHasFixedSize(true);
        linearLayoutManager=new GridLayoutManager(this,2);
        cartOrder.setLayoutManager(linearLayoutManager);
        cartItems = new ArrayList<>();
        cartAdapter= new CartAdapter(OrderInfo.this,cartItems);
        cartOrder.setAdapter(cartAdapter);

        reference= FirebaseDatabase.getInstance().getReference("Carts").child(UID);
        userReference=FirebaseDatabase.getInstance().getReference("Users").child(UID);
        orderReference=FirebaseDatabase.getInstance().getReference("Orders");



        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    CartItem cartItem1= dataSnapshot.getValue(CartItem.class);
                    cartItems.add(cartItem1);
                }
                cartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        /** Getting user's details who have applied for components **/
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user=snapshot.getValue(User.class);

//                assert user != null;
                UserName.setText(user.getName());
                UserYear.setText(user.getYear());
                UserBranch.setText(user.getBranch());
                UserMail.setText(user.getEmail());

                if (user.getProfilePic()!=null){
                    Glide.with(circularImageView).load(user.getProfilePic()).placeholder(R.drawable.robot1).dontAnimate().into(circularImageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog.show();
            }
        });

        dateSubmitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approvalDate=approvalDateEt.getText().toString();
                returnDate=returnDateEt.getText().toString();
                ArrayList<String> componentNames = new ArrayList<>();

                for (CartItem cartItem : cartItems){
                    componentNames.add(cartItem.getComponentName());
                }
                Orders order= new Orders(cartItems,approvalDate,returnDate,componentNames,UID,"NO");

                orderReference.child(UID).child(approvalDate).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dateDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                    }
                });


                for(CartItem cartItem: order.getCartItems()){
                    String name=cartItem.getComponentName();



                    decrementReference=FirebaseDatabase.getInstance().getReference("Components");

                    decrementReference.child("Electronics").child(name).child("work").setValue(ServerValue.increment(-1*cartItem.getQuantity()));
                    decrementReference.child("Mechanical").child(name).child("work").setValue(ServerValue.increment(-1*cartItem.getQuantity()));
                    decrementReference.child("Electronics").child(name).child("avail").setValue(ServerValue.increment(-1*cartItem.getQuantity()));
                    decrementReference.child("Mechanical").child(name).child("avail").setValue(ServerValue.increment(-1*cartItem.getQuantity()));

                    /** Following two queries are added to delete the elements that are not important products of above queries **/
                    decrementReference.child("Electronics").orderByChild("compName").equalTo(null).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                dataSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    decrementReference.child("Mechanical").orderByChild("compName").equalTo(null).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                dataSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
        dateDialogCloser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog.dismiss();
            }
        });




    }
}