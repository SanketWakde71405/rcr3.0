package com.example.rcr30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Final extends AppCompatActivity {


    ImageView Back2EMI;
    ImageView FinalImage;
    TextView FinalName;
    TextView FinalRem;
    TextView FinalSpecs;
    CircularImageView Decrementer;
    TextView demander;
    CircularImageView Incrementer;
    CardView Return;
    CardView CartAdder;
    ProgressDialog pdFinal;

    String CompName;
    String Remaining;
    int Num_remaining;
    String CompSpecs;
    String URL;
    int Demand;

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        /** Firebase and intent initializations **/
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Carts");
        CompName= getIntent().getStringExtra("Component Name");
        CompSpecs= getIntent().getStringExtra("Component Specifications");
        Remaining=getIntent().getStringExtra("Remaining Components");
        Num_remaining= Integer.parseInt(Remaining);
        URL= getIntent().getStringExtra("Component image");

        /** View Initializations **/
        Back2EMI=findViewById(R.id.back_to_itemList);
        FinalImage=findViewById(R.id.ComponentImageFinal);
        FinalName=findViewById(R.id.ComponentNameFinal);
        FinalSpecs=findViewById(R.id.SpecificationsFinal);
        FinalRem=findViewById(R.id.RemainingFinal);
        Decrementer=findViewById(R.id.decrement);
        Incrementer=findViewById(R.id.increment);
        demander=findViewById(R.id.demand);
        Return=findViewById(R.id.ReturnButtonFinal);
        CartAdder=findViewById(R.id.CartButtonFinal);
        pdFinal= new ProgressDialog(Final.this);

        /** Setting up textViews and images with the data obtained from intent initialization **/
        Glide.with(FinalImage).load(URL).placeholder(R.drawable.placeholder).dontAnimate().into(FinalImage);
        FinalName.setText(CompName);
        FinalSpecs.setText(CompSpecs);
        FinalRem.setText(Remaining);
        demander.setText("0");
        Demand= Integer.parseInt(demander.getText().toString());

        /** Increments the number by 1 on each click that is stored in the textview **/
        Incrementer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Demand>=Num_remaining){
                    demander.setText(Remaining);
                }

                else {
                    Demand++;
                    demander.setText(""+ Demand);
                }

            }
        });

        /** Decrements the number by 1 on each click that is stored in the textview **/
        Decrementer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Demand<=0){
                    demander.setText("0");

                }

                else {
                    Demand--;
                    demander.setText(""+ Demand);
                }

            }
        });


        /** Finishes the current activity to go back to the previous one **/
         Back2EMI.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 finish();
             }
         });

        /** Adding data to the user personal cart in the firebase database **/
        CartAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdFinal.setTitle("Adding to cart");
                pdFinal.show();


                CartItem cartItem= new CartItem(CompName,URL,Demand);

                if(demander.getText().toString().equals("0")){
                    Toast.makeText(getApplicationContext(), "Select the quantity", Toast.LENGTH_SHORT).show();
                }
                else{

                    reference.child(user.getUid()).child(cartItem.getComponentName()).setValue(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            pdFinal.dismiss();
                            Toast.makeText(Final.this, "Item added to the cart", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(Final.this,Cart.class);
                            startActivity(intent);
                        }
                    });

                }

            }
        });


    }
}