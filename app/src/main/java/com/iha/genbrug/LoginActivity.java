package com.iha.genbrug;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends FragmentActivity {

    //Local Log in

    private EditText Localusername;
    private EditText password;
    private static boolean loginStatusVariable =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupVariables();

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

        Intent intent = new Intent(this,MainActivity.class);

        if (Localusername.getText().toString().equals("parsa@live.dk") &&
                password.getText().toString().equals("admin")) {
            Toast.makeText(getApplicationContext(), "Hello admin!",
                    Toast.LENGTH_SHORT).show();

            loginStatusVariable =true;
           this.startActivity(intent);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Wrong username or password!",
                    Toast.LENGTH_SHORT).show();
        }
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
}
