package com.example.rcr30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<Orders> ordersArrayList;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date= new Date();


    public HistoryAdapter(Context context, ArrayList<Orders> ordersArrayList) {
        this.context = context;
        this.ordersArrayList = ordersArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {

        Orders order = ordersArrayList.get(position);

        if(holder.approvalDate.getText().toString().compareTo(formatter.format(date)) == 0){
            holder.approvalDate.setText("Today, "+ order.getOrderDate());
        }
        else{
            holder.approvalDate.setText(order.getOrderDate());
        }
        holder.returnDate.setText(order.getReturningDate());
        holder.statusOfReturn.setText(order.getReturningStatus());


        if(order.getReturningStatus().equals("YES")){
            holder.returnCard.setVisibility(View.GONE);
            holder.statusSymbolDisplay.setImageResource(R.drawable.check);
            holder.statusOfReturn.setTextColor(Color.parseColor("#168A22"));
        }

        if (context instanceof Return){
            holder.returnCard.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
        }
        holder.returnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth=FirebaseAuth.getInstance();
                user= mAuth.getCurrentUser();
                assert user != null;
                String UID= user.getUid();
                Intent intent= new Intent(context.getApplicationContext(),QRcode.class);
                intent.putExtra("User ID",UID);
                context.startActivity(intent);
            }
        });

        holder.nestedView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(context.getApplicationContext());
        holder.nestedView.setLayoutManager(layoutManager);
        NestedAdapter nestedAdapter= new NestedAdapter(order.getComponentList(), order.getCartItems());
        holder.nestedView.setAdapter(nestedAdapter);

    }

    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView approvalDate;
        TextView returnDate;
        TextView statusOfReturn;
        ImageView statusSymbolDisplay;
        RecyclerView nestedView;
        CardView returnCard;
        View line;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            approvalDate=itemView.findViewById(R.id.date);
            returnDate=itemView.findViewById(R.id.returnDateDisplay);
            statusOfReturn=itemView.findViewById(R.id.statusReturn);
            statusSymbolDisplay=itemView.findViewById(R.id.symbolStatus);
            nestedView=itemView.findViewById(R.id.rcViewInRcView);
            returnCard=itemView.findViewById(R.id.returnButton);
            line=itemView.findViewById(R.id.linearView);
        }
    }
}
