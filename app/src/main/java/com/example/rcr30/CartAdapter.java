package com.example.rcr30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    ArrayList<CartItem> cartItemArrayList;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String UID;

    public CartAdapter(Context context, ArrayList<CartItem> cartItemArrayList) {
        this.context = context;
        this.cartItemArrayList = cartItemArrayList;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cartitem, parent, false);
        return new CartAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CartItem cartItem= cartItemArrayList.get(position);

        Glide.with(holder.CartImage).load(cartItem.getImageUrl()).placeholder(R.drawable.placeholder).
                dontAnimate().into(holder.CartImage);

        holder.Quantity.setText("Qty: "+ cartItem.getQuantity());
        holder.CartItemName.setText(cartItem.getComponentName());

        if (context instanceof OrderInfo){
            holder.CartItemDeleter.setVisibility(View.GONE);
        }

        holder.CartItemDeleter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference= FirebaseDatabase.getInstance().getReference("Carts");
                mAuth=FirebaseAuth.getInstance();
                user=mAuth.getCurrentUser();
                assert user != null;
                UID= user.getUid();

                reference.child(UID).child(cartItem.getComponentName()).removeValue(new DatabaseReference.CompletionListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        cartItemArrayList.remove(position);
                        notifyDataSetChanged();
                        notifyItemRemoved(position);
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView CartImage;
        TextView Quantity;
        TextView CartItemName;
        ImageView CartItemDeleter;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            CartImage=itemView.findViewById(R.id.CartComponentImage);
            Quantity=itemView.findViewById(R.id.Borrowed);
            CartItemName=itemView.findViewById(R.id.AddedComponent);
            CartItemDeleter=itemView.findViewById(R.id.deleteCartItem);
        }
    }
}
