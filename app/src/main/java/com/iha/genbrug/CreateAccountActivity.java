package com.iha.genbrug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import webservice.User;


public class CreateAccountActivity extends FragmentActivity{

    private EditText CreateUserName;
    private EditText CreatePassword;
    private EditText CreateRePassword;
    private EditText CreateFirstName;
    private EditText CreateLastName;
    private Button signUpBtn;

    private ServerService serverServiceCreate;
    private User user;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServerService.LocalBinder binder = (ServerService.LocalBinder) service;
            serverServiceCreate = binder.getService();
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

        setContentView(R.layout.activity_create_account);

        setupVariables();

        Intent intent = new Intent(this,ServerService.class);

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if (savedInstanceState == null) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FBFragment fragment = new FBFragment();
            fragmentTransaction.add(R.id.fl_fb_fragment_container_create_acc, fragment);

            ConnectionFragment conFragment = new ConnectionFragment();
            fragmentTransaction.add(R.id.container2, conFragment);

            fragmentTransaction.commit();
        }
    }

    public void enabledState(boolean isEnabled)
    {
        signUpBtn = (Button) findViewById(R.id.createSignUpBtn);
        signUpBtn.setEnabled(isEnabled);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
      
    }

    private void setupVariables() {
        CreateUserName = (EditText) findViewById(R.id.createUsername);
        CreatePassword = (EditText) findViewById(R.id.createPassword);
        CreateRePassword = (EditText) findViewById(R.id.createRePassword);
        CreateFirstName = (EditText) findViewById(R.id.createFirstName);
        CreateLastName = (EditText) findViewById(R.id.createLastName);
    }

    // method for creating an account in DB
    public void authenticateSignUp (View view){

        String username;
        String password;
        String rePassword;
        String firstName;
        String lastName;

        username = CreateUserName.getText().toString();
        password = CreatePassword.getText().toString();
        rePassword = CreateRePassword.getText().toString();
        firstName = CreateFirstName.getText().toString();
        lastName = CreateLastName.getText().toString();
        Intent intent = new Intent(this, LoginActivity.class);

        if(!password.isEmpty()&& !rePassword.isEmpty()
                &&password.equals(rePassword)&& validEmail(username)
                && !firstName.isEmpty()&& !lastName.isEmpty()
                && validateName(CreateFirstName.getText().toString())
                &validateName(CreateLastName.getText().toString()))
        {
            user = new User();
            user.username = username;
            user.password = password;
            user.firstname = firstName;
            user.lastname = lastName;

            Toast.makeText(getApplicationContext(), "Account created!",
                    Toast.LENGTH_LONG).show();

            this.startActivity(intent);
            finish();
        }
        else if (!validEmail(username)) {
            Toast.makeText(getApplicationContext(), "Wrong email address!",
                    Toast.LENGTH_SHORT).show();
        }

        else  if (firstName.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "First Name is required!",
                    Toast.LENGTH_SHORT).show();
        }

        else  if (lastName.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Last Name is required!",
                    Toast.LENGTH_SHORT).show();
        }

        else  if (validateName(CreateFirstName.getText().toString()) ==false)
        {
            Toast.makeText(getApplicationContext(), "Wrong First Name!",
                    Toast.LENGTH_SHORT).show();
        }

        else  if (validateName(CreateLastName.getText().toString()) ==false)
        {
            Toast.makeText(getApplicationContext(), "Wrong Last Name!",
                    Toast.LENGTH_SHORT).show();
        }
        else { Toast.makeText(getApplicationContext(), "Wrong password!",
                Toast.LENGTH_SHORT).show();
        }

        try {

            serverServiceCreate.createUser(user);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //method to validate emailadress
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // To logingActivity onBackPressed
       @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        finish();
    }

    private boolean validateName (String validate)
    {
        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher matchValue = ps.matcher(validate);
        boolean validator = matchValue.matches();

        if (validator) {

            return true;
        }
        else {
            
            return false;
        }
    }
}
