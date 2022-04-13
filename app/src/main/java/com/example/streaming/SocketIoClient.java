package com.example.streaming;

import android.app.Activity;
import java.net.URISyntaxException;

import io.socket.client.IO;

import java.net.Socket;

public class SocketIoClient {

    private static io.socket.client.Socket mSocket;

    private static void initSocket(Activity activity) {
        try {
            mSocket = IO.socket("https://mobile-technical-test.herokuapp.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static io.socket.client.Socket getInstance(Activity activity) {
        if (mSocket != null) {
            return mSocket;
        } else {
            initSocket(activity);
            return mSocket;
        }
    } }
