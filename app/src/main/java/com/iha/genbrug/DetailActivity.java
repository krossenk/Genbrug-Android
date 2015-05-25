package com.iha.genbrug;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.login.LoginManager;
import webservice.Subscription;
import webservice.User;
import webservice.getUserSubscriptionsResponse;


public class DetailActivity extends Activity {

    Button logOutBtn;
    private long publicationId;
    private ImageLoader imgLoader;
    private ServerService serverServiceSubscribe;
    private UserSubscriptionReceiver  receiver;
    private getUserSubscriptionsResponse userSubscriptionsResponseList;
    private GlobalSettings  globalSettings =GlobalSettings.getInstance();
    long  userId;
    boolean itemSubscribedCheck = false;
    String header;
    String imageUrl;
    String desc;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            User user = globalSettings.getUserFromPref();
            userId = user.id;
            serverServiceSubscribe = binder.getService();
            serverServiceSubscribe.startGetUserSubscriptions(1);
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

        receiver = new UserSubscriptionReceiver();
        IntentFilter intentFilter = new IntentFilter(ServerService.ALL_USERSUBSRIPTIONS_RESULT);
        this.registerReceiver(receiver, intentFilter);

        //getting iteminfo from Feedadapter
        Bundle bundle = getIntent().getExtras();
        imageUrl =(bundle.getString("imageUrl"));
        header = (bundle.getString("headline"));
        desc = (bundle.getString("desc"));
        publicationId = (bundle.getLong("itemId"));
        imgLoader = VolleySingleton.getInstance().getImageLoader();

        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.itemPhoto);
        imageView.setImageUrl(imageUrl, imgLoader);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        TextView headerTextView = (TextView) findViewById(R.id.headlineTextView);
        TextView descTextView = (TextView) findViewById(R.id.descTextView);
        headerTextView.setText(header);
        descTextView.setText(desc);

        logOutBtn = (Button) findViewById(R.id.Logout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        unregisterReceiver(receiver);
    }

    // temporary logout function
    public void logOut(View v)
    {

        //Reset all information from user
        globalSettings.sharedPreferences.edit().putString("userObj", "").commit();
        globalSettings.sharedPreferences.edit().putBoolean("Islogin", false).commit();
        globalSettings.setUser(new User());

        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this,LoginActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void subscribe (View v){

        if (userId != 0)
        {

            if(userSubscriptionsResponseList!= null) {
                for (Subscription sub : userSubscriptionsResponseList) {
                    if (sub.publicationId.id == publicationId) {
                        itemSubscribedCheck = true;
                    }
                }

                if(itemSubscribedCheck)
                {
                    Toast.makeText(this, header + " is already subscribed", Toast.LENGTH_SHORT).show();
                }
                else {
                    serverServiceSubscribe.createSubscription(userId, publicationId);
                    Toast.makeText(this, header+ " is subscribed", Toast.LENGTH_SHORT).show();
                    itemSubscribedCheck= true;
                }
            }
            else {

                serverServiceSubscribe.createSubscription(userId, publicationId);
                Toast.makeText(this, header + " is subscribed", Toast.LENGTH_SHORT).show();
            }
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

    private class UserSubscriptionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().compareTo(ServerService.ALL_USERSUBSRIPTIONS_RESULT)==0)
            {
                userSubscriptionsResponseList = serverServiceSubscribe.getUserSubscriptions();
            }
        }
    }
}
