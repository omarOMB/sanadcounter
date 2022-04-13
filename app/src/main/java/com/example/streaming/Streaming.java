package com.example.streaming;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import io.socket.emitter.Emitter;

import static com.example.streaming.App.CHANNEL_1_ID;

public class Streaming extends AppCompatActivity {

    public TextView text;
    public SwitchCompat mySwitch;
    public TextView user;
    public ImageView logo;
    public TextView name;

    private io.socket.client.Socket mSocket;


    static List<Message> MESSAGES = new ArrayList<>();
    private CharSequence SeqSender ;
    private CharSequence SeqMessage ;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        text =  (TextView) findViewById(R.id.text);
        mySwitch = (SwitchCompat) findViewById(R.id.switch1);
        user = (TextView) findViewById(R.id.user);
        name = (TextView) findViewById(R.id.myname);
        logo = (ImageView) findViewById(R.id.logo);
        SocketIoClient socketIoClient = new SocketIoClient();
        mSocket = socketIoClient.getInstance(Streaming.this);


        notificationManager = NotificationManagerCompat.from(this);

        UserOfStreaming userOfTheApp = new UserOfStreaming(Streaming.this);
        HashMap<String , String> userDetails = userOfTheApp.getUserDetailsFromSession();
        String id_user = userDetails.get(UserOfStreaming.ID_USER);

        if(id_user != null){
            user.setText(id_user);
        }else{
            String id = getIntent().getStringExtra("userId");
            if(id.length() != 0){
                user.setText(id);
            }else{
                user.setText("No ID !! ");
            }
        }


        Animation fadeDown = AnimationUtils.loadAnimation(this, R.anim.moving_down_slow);

        //set the animation..
        mySwitch.startAnimation(fadeDown);
        text.startAnimation(fadeDown);
        user.startAnimation(fadeDown);
        name.startAnimation(fadeDown);
        logo.startAnimation(fadeDown);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(Streaming.this, "Listening ...", Toast.LENGTH_SHORT).show();
                    mSocket.emit("register", user.getText().toString().trim());
                    listen();
                }else{
                    Toast.makeText(Streaming.this, "Unregistration ...", Toast.LENGTH_SHORT).show();
                    mSocket.emit("unregister", "");
                    text.setText("0");
                }
            }
        });


    }//on create




    //Send notification
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public static void sendNotification1(Context context) {
        Intent activityIntent = new Intent(context, Streaming.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0);

        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                .setLabel("Your answer...")
                .build();


        NotificationCompat.MessagingStyle messagingStyle =
                new NotificationCompat.MessagingStyle("Me");
        messagingStyle.setConversationTitle("Group Chat");

        for (Message chatMessage : MESSAGES) {
            NotificationCompat.MessagingStyle.Message notificationMessage =
                    new NotificationCompat.MessagingStyle.Message(
                            chatMessage.getText(),
                            chatMessage.getTimestamp(),
                            chatMessage.getSender()
                    );
            messagingStyle.addMessage(notificationMessage);
        }

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)
                .setStyle(messagingStyle)
                .setColor(Color.rgb(240, 129, 48))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, notification);
    }

    public void listen(){
        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String messageText = args[0].toString();
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
                    @Override
                    public void run() {
                        text.setText(messageText);
                        SeqMessage = new StringBuffer(messageText);
                        SeqSender = new StringBuffer("Counter");
                        Message message = new Message(SeqMessage,SeqSender);
                        MESSAGES.add(message);
                        sendNotification1(Streaming.this);
                    }
                });
            }
        });
    }


}
