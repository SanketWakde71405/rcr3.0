package com.example.rcr30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Starting Login Activity
        ImageButton button=findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        });


        // Starting signup Activity
        ImageButton button2=findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            Intent sign= new Intent(MainActivity.this,SignupActivity.class);
            startActivity(sign);
            finish();
        });
    }
}