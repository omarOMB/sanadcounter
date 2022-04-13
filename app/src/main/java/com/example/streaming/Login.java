package com.example.streaming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    public Button register;
    private io.socket.client.Socket mSocket;
    private Handler handler = new Handler();
    private EditText userid;
    private ImageView logo;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        SocketIoClient socketIoClient = new SocketIoClient();
        mSocket = socketIoClient.getInstance(Login.this);

        register = (Button) findViewById(R.id.register);
        userid = (EditText) findViewById(R.id.userid);
        name = (TextView)  findViewById(R.id.myname);
        logo = (ImageView)  findViewById(R.id.logo);

        Animation fadeDown = AnimationUtils.loadAnimation(this, R.anim.moving_down_slow);

        //set the animation..
        userid.startAnimation(fadeDown);
        register.startAnimation(fadeDown);
        logo.startAnimation(fadeDown);
        name.startAnimation(fadeDown);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userid.getText().toString().trim().length() != 0)
                {
                    Toast.makeText(Login.this, "registraation ...", Toast.LENGTH_SHORT).show();
                    String userID =  userid.getText().toString().trim();

                    //new login to Shared prefrences
                    UserOfStreaming userOfApp = new UserOfStreaming(Login.this);
                    userOfApp.createLoginSession(userID);


                    Intent intent = new Intent(Login.this, Streaming.class);
                    intent.putExtra("userId", userID);
                    register.startAnimation(AnimationUtils.loadAnimation(Login.this, R.anim.rotation) );
                    mSocket.emit("register", userid.getText().toString().trim());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }
                    }, 3000);
                }else{
                    Toast.makeText(Login.this, "Enter an ID !!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
