package com.iha.genbrug;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuView;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends Activity {

    Intent intent;
    Button infoBtn;
    Button logOutBtn;
    SharedPreferences prefs;
    private int itemId;
    private ServerService serverServiceSubscribe;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            serverServiceSubscribe = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = new Intent(this,ServerService.class);

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        //getting iteminfo from Feedadapter
        Bundle bundle = getIntent().getExtras();
        String imageId =(bundle.getString("imageId"));
        String header = (bundle.getString("headline"));
        String desc = (bundle.getString("desc"));
        itemId = (bundle.getInt("itemId"));
        Bitmap bm = BitmapFactory.decodeResource(getResources(), Integer.parseInt(imageId));
        ImageView imageView = (ImageView) findViewById(R.id.item_photo);
        imageView.setImageBitmap(bm);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        TextView headerTextView = (TextView) findViewById(R.id.headlineTextView);
        TextView descTextView = (TextView) findViewById(R.id.descTextView);
        headerTextView.setText(header);
        descTextView.setText(desc);

        infoBtn = (Button) findViewById(R.id.info);
        logOutBtn = (Button) findViewById(R.id.Logout);

    }

    }
    public void showInfo (View v)
    {
        prefs =PreferenceManager.getDefaultSharedPreferences(this);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        Toast.makeText(this,prefs.getString("LocalUser", ""),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,prefs.getString("FBUser", ""),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,prefs.getString("ProfileURL", ""),Toast.LENGTH_SHORT).show();
    }

    // temporary logout function
    public void logOut(View v)
    {
        prefs =PreferenceManager.getDefaultSharedPreferences(this);

        //Reset all information from user
        prefs.edit().putBoolean("Islogin", false).commit();
        prefs.edit().putString("UserName", "").commit();
        prefs.edit().putInt("localUserId", 0).commit();
        prefs.edit().putString("FBUser", "").commit();
        prefs.edit().putLong("FBUserId", 0).commit();

        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this,LoginActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void subscribe (View v){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int localUserId = prefs.getInt("localUserId", 0);
        Long FBUserId = prefs.getLong("FBUserId", 0);


        if (localUserId != 0)
        {
            serverServiceSubscribe.createSubscription(localUserId,itemId);
            Toast.makeText(this,"The item with itemId: " + itemId + " is subscribed",Toast.LENGTH_SHORT).show();
        }

        else if (FBUserId != 0)
        {
            Long FbUserVal = Long.valueOf(FBUserId);

            //serverServiceSubscribe.createSubscription(FbUserVal,itemId);
            Toast.makeText(this,"The item with itemId: " + itemId + " is subscribed",Toast.LENGTH_SHORT).show();
        }

        else
        {
            Toast.makeText(this,"No user to subscribe to",Toast.LENGTH_SHORT).show();
        }


    }

    public void callMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
        finish();
    }


    public void callFeedActivity(View view) {
        callMainActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callMainActivity();
    }
}
