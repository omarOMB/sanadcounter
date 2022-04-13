package com.example.streaming;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class UserOfStreaming {

    SharedPreferences userOfStreaming;
    SharedPreferences.Editor editor;
    Context context;

    public static final String ID_USER = "id";


    public  UserOfStreaming(Context contextx){
        context = contextx;
        userOfStreaming = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userOfStreaming.edit();
    }

    public void createLoginSession(String ID_user){
        editor.putString(ID_USER,ID_user);
        editor.commit();
    }


    public HashMap<String , String> getUserDetailsFromSession(){
        HashMap<String , String> userData  = new HashMap<String , String>();

        userData.put(ID_USER,userOfStreaming.getString(ID_USER,null));

        return userData;
    }


    public void logout(){
        editor.clear();
        editor.commit();
    }

}


