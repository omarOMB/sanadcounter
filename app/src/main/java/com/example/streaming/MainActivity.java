package com.example.streaming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {

    //HANDLER FOR COOL SPLASH SCREEN !
    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //THE LOGO
        ImageView image = findViewById(R.id.logo);

        //JUST FOR ANIMATION
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        Animation fadeDown = AnimationUtils.loadAnimation(this, R.anim.fade_down);

        //set the animation..
        image.startAnimation(scale);
        image.startAnimation(fadeDown);

        UserOfStreaming user = new UserOfStreaming(MainActivity.this);
        HashMap<String , String> userDetails = user.getUserDetailsFromSession();
        String id_user = userDetails.get(UserOfStreaming.ID_USER);

        if(id_user != null){
            Intent intent = new Intent(MainActivity.this, Streaming.class);
            startActivity(intent);
        }

        //move to the next activity..
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Connection.class);
                startActivity(intent);
            }
        }, 2000); // 2 and half seconds.. the animation will take 2 s and we will add a half :)


    }//on create



}