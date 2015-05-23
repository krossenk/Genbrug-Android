package com.iha.genbrug;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import webservice.User;


public class LoginActivity extends FragmentActivity {

    //Local Log in

    private EditText Localusername;
    private EditText password;
    private boolean  loginStatusVariable = false;
    private ServerService serverService;
    private User user;
    private ServiceMessagesReceiver serviceMessagesReceiver;
    private Intent mainIntent;
    FBFragment fbFragment = new FBFragment();
    ConnectionFragment conFragment = new ConnectionFragment();
    private Button loginBtn;
    private Button createBtn;


    //method for connection to service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            serverService = binder.getService();
                   }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupVariables();
        mainIntent = new Intent(this,MainActivity.class);
        Intent intent = new Intent(this,ServerService.class);


        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        serviceMessagesReceiver = new ServiceMessagesReceiver();
        IntentFilter intentFilter = new IntentFilter(ServerService.RESULT_RETURNED_FROM_SERVICE);
        registerReceiver(serviceMessagesReceiver,intentFilter);

        // call FB and Connections fragments in Activity
        if (savedInstanceState == null) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.container, fbFragment);

            fragmentTransaction.add(R.id.container, conFragment);
            fragmentTransaction.commit();
        }
        //loginStatusVariable = false;

        final Button btn = (Button) findViewById(R.id.loginBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateLogin(v);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

          // get login status from shared preferences
        loginStatusVariable = prefs.getBoolean("Islogin", false);

        if(loginStatusVariable && isNetworkAvailable(getApplicationContext()))
        {
        //condition true means user is already login
            Intent i = new Intent(this, MainActivity.class);
            startActivityForResult(i, 1);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        unregisterReceiver(serviceMessagesReceiver);
    }

    //Local Log in method


    public void authenticateLogin(View view)  {


        try {
            serverService.validateUser(Localusername.getText().toString(), password.getText().toString());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupVariables() {
        Localusername = (EditText) findViewById(R.id.usernameET);
        password = (EditText) findViewById(R.id.passwordET);

    }

    //method for enablation of buttons
    public void enabledState(boolean isEnabled)
    {
        loginBtn = (Button) findViewById(R.id.loginBtn);
        createBtn = (Button) findViewById(R.id.signUpBtn);
        loginBtn.setEnabled(isEnabled);
        createBtn.setEnabled(isEnabled);

    }

    //method to call createAccountActivity
    public void callCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        finish();
    }

    // method for checking if networkConnection is available
    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    // a broadcastReciever to recieve action from serverservice for validation of user
    private class ServiceMessagesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().compareTo(ServerService.RESULT_RETURNED_FROM_SERVICE)==0)
            {
                  user = serverService.getValidatedUser();


               if( isNetworkAvailable(context)&&user != null )

                {
                    startActivity(mainIntent);
                    finish();

                    loginStatusVariable =true;

                    // to store  if user has tried log in
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    //islogin is a boolean value of your login status pushed to SharedPreferences
                    prefs.edit().putBoolean("Islogin", loginStatusVariable).commit();
                    prefs.edit().putInt ("localUserId", user.id).commit();


                    if (user.firstname !=null && user.lastname !=null)
                    {
                        prefs.edit().putString("UserName", user.firstname + " " + user.lastname).commit();

                    }
                    else {
                        prefs.edit().putString("UserName", user.username).commit();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Wrong username or password!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}