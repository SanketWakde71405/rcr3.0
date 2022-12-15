package com.example.rcr30;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;


public class Login extends AppCompatActivity {
    EditText editTextTextEmailAddress, editTextTextPassword;
    ImageButton button3;
    String username,password;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressBar progressBar;
    int counter=0;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialization
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        editTextTextEmailAddress=findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword=findViewById(R.id.editTextTextPassword);
        button3 = findViewById(R.id.imageButton2);
        progressBar=findViewById(R.id.progress);


        // Login button
        button3.setOnClickListener(v -> {

            username = editTextTextEmailAddress.getText().toString();
            password = editTextTextPassword.getText().toString();



            if (TextUtils.isEmpty(username)) {
                editTextTextEmailAddress.setError("Please enter email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                editTextTextPassword.setError("Please enter password");
            } else {
//                making user sign in if it enters correct email and password

                progressBar.setVisibility(View.VISIBLE);
                progressBar.bringToFront();
                Timer timer= new Timer();
                TimerTask timerTask= new TimerTask() {
                    @Override
                    public void run() {
                        progressBar.setProgress(counter);
                        counter++;

                        if (counter==100){
                            timer.cancel();
                            mAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener(authResult -> {

                                progressBar.setVisibility(View.GONE);
                                Intent intent3 = new Intent(Login.this, Index.class);
                                intent3.putExtra("User",username);
                                intent3.putExtra("Password",password);
                                startActivity(intent3);
                                finish();
                            }).addOnFailureListener(e ->{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Login.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            );
                        }
                    }

                };

                timer.schedule(timerTask,100 ,100);
            }
        });


    }


}
