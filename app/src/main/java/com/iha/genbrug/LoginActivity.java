package com.iha.genbrug;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

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
    private GlobalSettings globalSettings;


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
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

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

            fragmentTransaction.add(R.id.fl_fb_fragment_container, fbFragment);

            fragmentTransaction.add(R.id.container, conFragment);
            fragmentTransaction.commit();
        }


        final Button btn = (Button) findViewById(R.id.loginBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateLogin(v);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

          // get login status from shared preferences
         loginStatusVariable = prefs.getBoolean("Islogin", false);



        // Gson gson = new Gson();
        //String json = Global.getSharedPreferences(this.getApplicationContext()).getString("userObj", "");
    /*    String json = prefs.getString("userObj", "");

        User obj = gson.fromJson(json, User.class);
        globalSettings.setUser(obj);*/

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

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    //islogin is a boolean value of your login status pushed to SharedPreferences

                    globalSettings = GlobalSettings.getInstance();
                    globalSettings.setUser(user);

                    Gson gson = new Gson();
                    String json = gson.toJson(user);


                    prefs.edit().putString("userObj", json).commit();
                    prefs.edit().putBoolean("Islogin", loginStatusVariable).commit();


                }

                else {
                    Toast.makeText(getApplicationContext(), "Wrong username or password!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
