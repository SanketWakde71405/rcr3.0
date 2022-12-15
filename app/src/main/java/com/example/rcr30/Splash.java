package com.example.rcr30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class Splash extends AppCompatActivity {
    private static final int SPLASH_SCREEN=5000;
    Animation top_anim,bottom_anim;
    ImageView imageView;
    TextView slogan,hello_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        slogan=findViewById(R.id.slogan);
        hello_message=findViewById(R.id.helloMessage);
        imageView=findViewById(R.id.imageView3);
        top_anim= AnimationUtils.loadAnimation(this,R.anim.top_to_bottom);
        bottom_anim=AnimationUtils.loadAnimation(this,R.anim.bottom_to_top);

        imageView.setAnimation(top_anim);
        slogan.setAnimation(bottom_anim);
        hello_message.setAnimation(bottom_anim);

        new Handler().postDelayed(() -> {


            Intent intent=new Intent(Splash.this,MainActivity.class);
            startActivity(intent);
            finish();
        },SPLASH_SCREEN);
    }
}