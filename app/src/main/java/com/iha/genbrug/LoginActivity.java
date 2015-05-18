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
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import webservice.User;


public class LoginActivity extends FragmentActivity {

    //Local Log in

    private EditText Localusername;
    private EditText password;
    private static boolean loginStatusVariable =false;
    private ServerService serverService;
    private User user;
    private ServiceMessagesReceiver serviceMessagesReceiver;
    private Intent mainIntent;

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

        if (savedInstanceState == null) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FBFragment fragment = new FBFragment();
            fragmentTransaction.add(R.id.container, fragment);
            ConnectionFragment conFragment = new ConnectionFragment();
            fragmentTransaction.add(R.id.container, conFragment);
            fragmentTransaction.commit();
        }
        loginStatusVariable = false;

        final Button btn = (Button) findViewById(R.id.loginBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateLogin(v);
            }
        });
    }

    //Local Log in

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

       /* if (Localusername.getText().toString().equals("parsa@live.dk") &&
                password.getText().toString().equals("admin")) {
            Toast.makeText(getApplicationContext(), "Hello admin!",
                    Toast.LENGTH_SHORT).show();

            loginStatusVariable =true;
           this.startActivity(intent);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Wrong username or password!",
                    Toast.LENGTH_SHORT).show();
        }*/
    }

    public static Boolean loginStatus()
    {
        return loginStatusVariable;
    }

    private void setupVariables() {
        Localusername = (EditText) findViewById(R.id.usernameET);
        password = (EditText) findViewById(R.id.passwordET);

    }

    public void callCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        finish();
    }

    private class ServiceMessagesReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().compareTo(ServerService.RESULT_RETURNED_FROM_SERVICE)==0)
            {
                  user = serverService.getValidatedUser();

                if(user != null)
                {
                    Toast.makeText(getApplicationContext(), "Hello " + user.firstname + " " + user.lastname,
                            Toast.LENGTH_SHORT).show();

                    //loginStatusVariable =true;
                    startActivity(mainIntent);
                    finish();
                }

                else{

                    Toast.makeText(getApplicationContext(), "Wrong username or password!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
