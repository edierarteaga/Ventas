package com.example.administrador.gestionusuarios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by asus on 02/11/2017.
 */

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref, variables;

    // Editor for Shared preferences
    SharedPreferences.Editor editor, var_editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Sesion";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    public static final String KEY_SPINNER = "comboes";
    public static final String KEY_COBRARDIAS="cobrardias";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Boolean para saber si quiere recordar o no la sesion
    public static final String KEY_RECORDAR = "recordar";

    public static final String KEY_USUARIOID = "usuario_id";
    public static final String KEY_APIKEY = "apikey";
    public static final String KEY_ID_PER = "id_per";
    public static final String KEY_VENDEDOR = "vendedor";
    public static final String KEY_CONDUCTOR = "conductor";
    public static final String KEY_PLACA = "placa";
    public static final String KEY_MAXRES = "max_res";
    public static final String KEY_PWD = "pwd";
    private static final String VAR_NAME = "Supergas";
    private static final String itemsspinner= "CARRO,MOTO,BICICLETA";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        variables = _context.getSharedPreferences(VAR_NAME, PRIVATE_MODE);
        var_editor = variables.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email, Boolean recordar){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // true o false si quiere recordar la sesion
        editor.putBoolean(KEY_RECORDAR, recordar);

        // commit changes
        editor.commit();
    }

    /**
     * Create login session
     * */
    public void createVariables(String nom_usu, String apiKey, String id_per, String ced_con, String nom_con, String placa, int max_res, String pwd){
        // Storing name in variables
        var_editor.putString(KEY_USUARIOID, nom_usu);

        // Storing email in pref
        var_editor.putString(KEY_APIKEY, apiKey);

        // true o false si quiere recordar la sesion
        var_editor.putString(KEY_ID_PER, id_per);

        var_editor.putString(KEY_VENDEDOR, ced_con);
        var_editor.putString(KEY_CONDUCTOR, nom_con);
        var_editor.putString(KEY_PLACA, placa);
        var_editor.putInt(KEY_MAXRES, max_res);
        //var_editor.putString(KEY_PWD, pwd);

        // commit changes
        var_editor.commit();
    }
    public void createVariables2(String itemsspinner){
        // Storing name in variables
        editor.putString(KEY_SPINNER, itemsspinner);
        // commit changes
        editor.commit();
    }
    public void createVariables3(String cobrardias){
        // Storing name in variables
        editor.putString(KEY_COBRARDIAS, cobrardias);
        // commit changes
        editor.commit();
    }
    /**
     * Get stored session data
     * */
    public SharedPreferences getVariables(){
        return variables;
    }
    public SharedPreferences getVariables2(){
        return pref;
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        //si recordo la contraseña lo envia directamente a los pedidos
        if (this.isRecordLogin()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }else{
            //si no recordo la contra se verifica si tiene una sesion activa y se envia a pedidos
            if(this.isLoggedIn()){
                // el ususario tiene una sesion activa
                Intent i = new Intent(_context, MainActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                _context.startActivity(i);
            }else {
                //si no tiene sesion activa se lo dirige al login de ussuario
                // user is not logged in redirect him to Login Activity
                Intent i = new Intent(_context, LoginActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                _context.startActivity(i);
            }
        }
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        //var_editor.clear();
        //var_editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isRecordLogin(){
        return pref.getBoolean(KEY_RECORDAR, false);
    }
}
