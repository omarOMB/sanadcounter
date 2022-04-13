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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Connection extends AppCompatActivity {

    public Button connect;
    private io.socket.client.Socket mSocket;
    private Handler handler = new Handler();
    private ImageView logo;
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        connect = (Button) findViewById(R.id.connect);
        logo = (ImageView) findViewById(R.id.logo);
        name = (TextView) findViewById(R.id.myname);

        Animation fadeDown = AnimationUtils.loadAnimation(this, R.anim.moving_down_slow);

        //set the animation..
        connect.startAnimation(fadeDown);
        logo.startAnimation(fadeDown);
        name.startAnimation(fadeDown);

        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Connection.this, "Connecting ...", Toast.LENGTH_SHORT).show();
                call();
                mSocket.connect();
                connect.startAnimation(AnimationUtils.loadAnimation(Connection.this, R.anim.rotation) );

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Connection.this, Login.class);
                        startActivity(intent);
                    }
                }, 3000);
            }
        });


    }


    public void call() {
        SocketIoClient socketIoClient = new SocketIoClient();
        mSocket = socketIoClient.getInstance(Connection.this);
    }
}
