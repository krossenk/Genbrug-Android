package com.iha.genbrug;

import android.app.Application;
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
    private User user;
    public SharedPreferences sharedPreferences;
    public Context context;


  private GlobalSettings ()
  {
      sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.getContextOfApplication());

  }

    public static GlobalSettings getInstance()

    {
        if (globalSettings == null)
        {
            globalSettings = new GlobalSettings();

        }
        return globalSettings;
    }

    public void setUser (User user)
    {
      this.user = user;
    }

   /* public User getUser ()
    {
        return user;
    }*/


    public void saveUserToPref (User value)
    {

        Gson gson = new Gson();
        String json = gson.toJson(value);
        sharedPreferences.edit().putString("userObj", json).commit();
    }

    public User getUserFromPref ()
    {

        Gson gson = new Gson();
        String json = sharedPreferences.getString("userObj", "");
        User obj = gson.fromJson(json, User.class);
        return obj;

    }
}
