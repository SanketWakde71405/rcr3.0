package com.example.rcr30;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Objects;

public class Return extends AppCompatActivity {

    ImageView goBack;
    CircularImageView userPic;
    TextView userName;
    TextView userMail;
    TextView userBranch;
    TextView userYear;
    RecyclerView returnRecycler;
    HistoryAdapter historyAdapter;
    ArrayList<Orders> ordersArrayList;
    CardView returnButtonApproval;

    String UID;
    String date;
    DatabaseReference reference, userReference, incrementReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        /** Initializations **/
        goBack = findViewById(R.id.moreFragmentOpener3);
        userPic = findViewById(R.id.orderingUserReturn);
        userName = findViewById(R.id.nameViewerReturn);
        userMail = findViewById(R.id.mailViewerReturn);
        userBranch = findViewById(R.id.branchViewerReturn);
        userYear = findViewById(R.id.yearViewerReturn);

        returnButtonApproval = findViewById(R.id.returnButtonApproval);



        returnRecycler = findViewById(R.id.returnRecycler);


        /** Setting up recyclerView **/
        returnRecycler.setHasFixedSize(true);
        returnRecycler.setLayoutManager(new LinearLayoutManager(this));
        ordersArrayList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(Return.this, ordersArrayList);
        returnRecycler.setAdapter(historyAdapter);
        UID = getIntent().getStringExtra("User ID");
        date = getIntent().getStringExtra("Approval Date");


        reference = FirebaseDatabase.getInstance().getReference("Orders");

        /** Getting order's details **/
        reference.child(UID).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Orders order = snapshot.getValue(Orders.class);
                if (order != null) {
                    ordersArrayList.add(order);
                    historyAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        /** Getting user's details **/
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        userReference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    userName.setText(user.getName());
                    userYear.setText(user.getYear());
                    userBranch.setText(user.getBranch());
                    userMail.setText(user.getEmail());

                    if (user.getProfilePic() != null) {
                        Glide.with(userPic).load(user.getProfilePic()).placeholder(R.drawable.robot1).dontAnimate().into(userPic);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        incrementReference = FirebaseDatabase.getInstance().getReference("Components");

        /** Approval button **/
        returnButtonApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    reference.child(UID).child(date).child("returningStatus").setValue("YES");


                    for (CartItem cartItem : ordersArrayList.get(0).getCartItems()) {
                        incrementReference.child("Electronics").child(cartItem.getComponentName()).child("avail").
                                setValue(ServerValue.increment(cartItem.getQuantity()));
                        incrementReference.child("Electronics").child(cartItem.getComponentName()).child("work").
                                setValue(ServerValue.increment(cartItem.getQuantity()));
                        incrementReference.child("Mechanical").child(cartItem.getComponentName()).child("avail").
                                setValue(ServerValue.increment(cartItem.getQuantity()));
                        incrementReference.child("Mechanical").child(cartItem.getComponentName()).child("work").
                                setValue(ServerValue.increment(cartItem.getQuantity()));


                    }

                    /** Deletes the extra added fields by above "Electronics" query **/
                    incrementReference.child("Electronics").orderByChild("compName").equalTo(null).
                            addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                dataSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    /** Deletes the extra added fields by above "Mechanical" query **/
                    incrementReference.child("Mechanical").orderByChild("compName").equalTo(null).
                            addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                dataSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
        });



        /** Finishing the current activity **/
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}