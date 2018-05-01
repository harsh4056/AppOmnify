package com.task.omnify.appomnify.Utils;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;

public class StoredVariables {

    public static void storeTokenId(String token){
        SharedPreferences.Editor editor = null;
        try {
            editor = getSharedPreferences(AppController.getInstance().getApplicationContext()).edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.putString(Constants.TOKEN, token);
        editor.apply();
    }
    public static String getTokenId(){
        SharedPreferences prefs = null;
        try {
            prefs = getSharedPreferences(AppController.getInstance().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String restoredText = prefs.getString(Constants.TOKEN, null);

        return restoredText;
    }
    public static boolean tokenUploaded(){

        SharedPreferences prefs = null;
        try {
            prefs = getSharedPreferences(AppController.getInstance().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String restoredText = prefs.getString(Constants.TOKEN, null);

        if (restoredText==(null))
        return true;
        else
            return false;
    }

    public static int getSignInMethod(){
        SharedPreferences prefs = null;
        try {
            prefs = getSharedPreferences(AppController.getInstance().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int value = prefs.getInt(Constants.SIGN_IN_MEANS, 0);

        return value;
    }


    public static void storeSignInMethod(int method){
        SharedPreferences.Editor editor = null;
        try {
            editor = getSharedPreferences(AppController.getInstance().getApplicationContext()).edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.putInt(Constants.SIGN_IN_MEANS, method);
        editor.apply();
    }






}
