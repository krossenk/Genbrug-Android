package com.iha.genbrug;

import android.content.Context;
import android.content.SharedPreferences;

import webservice.User;

/**
 * Created by Parsa on 24/05/2015.
 */
public class GlobalSettings {

    private static GlobalSettings globalSettings;
    private User user;
    private SharedPreferences sharedPreferences;


  private GlobalSettings ()
  {

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

    public User getUser ()
    {
        return user;
    }


    //StaticUser staticUser = new StaticUser();
    // you have to pass the context to it. In your case:
// this is inside a public class
    public static SharedPreferences getSharedPreferences (Context ctxt) {
        return ctxt.getSharedPreferences("FILE", 0);
    }

    
}
