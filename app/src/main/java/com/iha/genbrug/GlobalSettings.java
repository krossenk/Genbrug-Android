package com.iha.genbrug;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import webservice.User;

/**
 * Created by Parsa on 24/05/2015.
 */
public class GlobalSettings {

    private static GlobalSettings globalSettings;
    public SharedPreferences sharedPreferences;
    public Context context;

// Private Constructor
  private GlobalSettings ()
  {
      sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.getContextOfApplication());
  }

    //Creating an instance of the Class
    public static GlobalSettings getInstance()

    {
        if (globalSettings == null)
        {
            globalSettings = new GlobalSettings();

        }
        return globalSettings;
    }

    // Method to save a userobject in SharedPrefrences
    public void saveUserToPref (User value)
    {
        // Using Gson to save an object in sharedPreferences
        Gson gson = new Gson();
        String json = gson.toJson(value);
        sharedPreferences.edit().putString("userObj", json).commit();
    }

    // Method to get userobject from SharedPrefrences
    public User getUserFromPref ()
    {

        Gson gson = new Gson();
        String json = sharedPreferences.getString("userObj", "");
        User obj = gson.fromJson(json, User.class);
        return obj;

    }
}
