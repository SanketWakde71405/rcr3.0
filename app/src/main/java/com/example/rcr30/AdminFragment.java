package com.example.rcr30;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class AdminFragment extends Fragment {

    View view;
    CardView add_component;
    CardView update_component;
    CardView delete_component;
    CardView explore_component;
    CardView new_component;
    CardView qrCodeScanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_admin, container, false);
        add_component=view.findViewById(R.id.AddCard);
        update_component=view.findViewById(R.id.UpdateComponents);
        delete_component=view.findViewById(R.id.DeleteComponents);
        explore_component=view.findViewById(R.id.ExploreComponents);
        new_component=view.findViewById(R.id.NewComponents);
        qrCodeScanner=view.findViewById(R.id.scanQR);

        /** Starts the activity AddComponent **/
        add_component.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.AddCard:
                        Intent intent= new Intent(view.getContext(),AddComponent.class);
                        view.getContext().startActivity(intent);
                        break;
                }

            }
        });

        /** Starts the activity UpdateComponent **/
        update_component.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.UpdateComponents:
                        Intent intent= new Intent(view.getContext(),UpdateComponent.class);
                        view.getContext().startActivity(intent);
                        break;
                }
            }
        });

        /** Starts the activity DeleteComponent **/
        delete_component.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.DeleteComponents:
                        Intent intent=new Intent(view.getContext(),DeleteComponent.class);
                        view.getContext().startActivity(intent);
                        break;
                }
            }
        });

        /** Starts the activity AdminExplore **/
        explore_component.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent= new Intent(getContext(),AdminExplore.class);
//                startActivity(intent);
            }
        });

        /** Starts the activity AdminNew **/
        new_component.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent= new Intent(getContext(),AdminNew.class);
//                startActivity(intent);
            }
        });

        qrCodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ScanActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
