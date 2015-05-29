package com.iha.genbrug;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
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
import java.util.ArrayList;
import java.util.List;

import webservice.Publication;
import webservice.Subscription;
import webservice.User;



public class DetailActivity extends Activity {

    Button logOutBtn;
    private long publicationId;
    private ImageLoader imgLoader;
    private ServerService serverServiceSubscribe;
    private GlobalSettings  globalSettings =GlobalSettings.getInstance();
    long  userId;
    boolean itemSubscribedCheck = false;
    String header;
    String imageUrl;
    String desc;
    List<Subscription> subList;
    private Publication getPublicationResponse;
    private DetailsMessagesReceiver  receiver;
    TextView headerTextView;
    TextView descTextView;
    NetworkImageView imageView;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;


            serverServiceSubscribe = binder.getService();
            serverServiceSubscribe.startGetUserSubscriptions(userId);
            Bundle bundle = getIntent().getExtras();
            publicationId = (bundle.getLong("itemId"));
            serverServiceSubscribe.startGetPublication(publicationId);
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
        receiver = new DetailsMessagesReceiver();
        IntentFilter intentFilter = new IntentFilter(ServerService.PUBLICATIONRESULT);
        registerReceiver(receiver, intentFilter);



        User user = globalSettings.getUserFromPref();
        userId = user.id;
        imgLoader = VolleySingleton.getInstance().getImageLoader();
        imageView = (NetworkImageView) findViewById(R.id.itemPhoto);
        headerTextView = (TextView) findViewById(R.id.headlineTextView);
        descTextView = (TextView) findViewById(R.id.descTextView);
        subList = new ArrayList<>();

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
        globalSettings.sharedPreferences.edit().putBoolean("Islogin", false).commit();
        globalSettings.saveUserToPref(new User());

        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this,LoginActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void subscribe (View v){

        if (userId != 0)
        {
            subList = SubscriptionsFragment.userSubscriptionsResponseList;

            if(subList!= null) {
                for (Subscription sub : subList) {
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

    private class DetailsMessagesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().compareTo(ServerService.PUBLICATIONRESULT) == 0) {

                getPublicationResponse = serverServiceSubscribe.getReturnedPublication();

                if(getPublicationResponse != null)
                {

                    header = getPublicationResponse.title;
                    desc = getPublicationResponse.description;
                    imageUrl = getPublicationResponse.imageURL;
                    headerTextView.setText(header);
                    descTextView.setText(desc);
                    imageView.setImageUrl(imageUrl,imgLoader);
                }

            }
        }

    }


}
