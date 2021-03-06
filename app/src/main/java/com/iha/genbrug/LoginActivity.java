package com.iha.genbrug;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import webservice.User;


public class LoginActivity extends FragmentActivity {

    //Local Log in

    private EditText localUsername;
    private EditText password;
    private boolean  loginStatusVariable = false;
    private ServerService serverService;
    private User user;
    private ServiceMessagesReceiver serviceMessagesReceiver;
    private Intent mainIntent;
    FBFragment fbFragment = new FBFragment();
    ConnectionFragment conFragment = new ConnectionFragment();

    private Button btnLogin;
    private Button btnCreate;

    private GlobalSettings globalSettings;
    public static Context contextOfApplication;


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


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateLogin(v);
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int actionId, KeyEvent event) {
                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    authenticateUser();
                    return true;
                }
                return false;
            }
        });

        contextOfApplication = getApplicationContext();
        globalSettings = GlobalSettings.getInstance();

        // get login status from shared preferences
        loginStatusVariable = globalSettings.sharedPreferences.getBoolean("Islogin", false);
        if(loginStatusVariable && isNetworkAvailable(getApplicationContext()))
        {
        //condition true means user is already login
            Intent i = new Intent(this, MainActivity.class);
            startActivityForResult(i, 1);
        }
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        unregisterReceiver(serviceMessagesReceiver);
    }

    //Local Log in method


    public void authenticateLogin(View view)  {
        authenticateUser();
    }

    private void authenticateUser() {
        try {
            serverService.validateUser(localUsername.getText().toString(), password.getText().toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupVariables() {
        localUsername = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        btnCreate = (Button) findViewById(R.id.btn_sign_up);
        btnLogin = (Button) findViewById(R.id.btn_login);
    }

    //method for enablation of buttons
    public void enabledState(boolean isEnabled) {
        btnLogin.setEnabled(isEnabled);
        btnCreate.setEnabled(isEnabled);
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

            if (intent.getAction().compareTo(ServerService.RESULT_RETURNED_FROM_SERVICE)==0) {
                user = serverService.getValidatedUser();
                if(isNetworkAvailable(context)&&user != null) {
                    startActivity(mainIntent);
                    loginStatusVariable =true;
                    globalSettings.saveUserToPref(user);
                    //islogin is a boolean value of your login status pushed to SharedPreferences
                    globalSettings.sharedPreferences.edit().putBoolean("Islogin", loginStatusVariable).commit();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong username or password!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
