package me.pj.travel.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;


/*UNTUK MENYIMPAN STATUS USER YANG LOGIN*/
public class Preferences {

    public static String ID;
    public static String NAMA;
    public static String USERNAME;
    public static String AKSES;
    public static String HP;
    public static String ALAMAT;
    public static Boolean IS_LOGIN;
    public static Boolean IS_ACTION_OK;
    public static ArrayList<String> DATA;

    public static void loadPreferences(final Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        Preferences.ID = preferences.getString("ID", "");
        Preferences.NAMA = preferences.getString("NAMA", "");
        Preferences.AKSES = preferences.getString("AKSES", "");
        Preferences.ALAMAT = preferences.getString("ALAMAT", "");
        Preferences.HP = preferences.getString("HP", "");
        Preferences.USERNAME = preferences.getString("USERNAME", "");
        Preferences.IS_LOGIN = preferences.getBoolean("IS_LOGIN",false );
        Preferences.IS_ACTION_OK = preferences.getBoolean("IS_ACTION_OK",false );


    }

    public static void saveUserPreferences(final Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("NAMA", NAMA);
        editor.putString("USERNAME", USERNAME);
        editor.putString("AKSES", AKSES);
        editor.putString("HP", HP);
        editor.putString("ALAMAT", ALAMAT);
        editor.putString("ID", ID);
        editor.putBoolean("IS_LOGIN", IS_LOGIN);
        editor.putBoolean("IS_ACTION_OK", IS_ACTION_OK);

        editor.commit();
        loadPreferences(context);
    }

    public static void resetAction(final Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IS_ACTION_OK", false);
        editor.commit();

        Preferences.IS_ACTION_OK = preferences.getBoolean("IS_ACTION_OK",false );
    }

    public static void saveAction(final Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IS_ACTION_OK", IS_ACTION_OK);
        editor.commit();

        Preferences.IS_ACTION_OK = preferences.getBoolean("IS_ACTION_OK",false );
    }


    public static void resetUserPreferences(final Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("NAMA", "");
        editor.putString("USERNAME", "");
        editor.putString("AKSES", "");
        editor.putString("HP", "");
        editor.putString("ALAMAT", "");
        editor.putString("ID", "");
        editor.putBoolean("IS_LOGIN", false);
        editor.putBoolean("IS_ACTION_OK", false);


        editor.commit();

        loadPreferences(context);
    }


}