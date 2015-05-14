package com.iha.genbrug;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;


public class CreateAccountActivity extends FragmentActivity{

    private EditText CreateUserName;
    private EditText CreatePassword;
    private EditText CreateRePassword;
    private String UserName;
    private String Password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        setupVariables();


        if (savedInstanceState == null) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FBFragment fragment = new FBFragment();
            fragmentTransaction.add(R.id.container2, fragment);

            ConnectionFragment conFragment = new ConnectionFragment();
            fragmentTransaction.add(R.id.container2, conFragment);

            fragmentTransaction.commit();
        }
    }

    private void setupVariables() {
        CreateUserName = (EditText) findViewById(R.id.createUsername);
        CreatePassword = (EditText) findViewById(R.id.createPassword);
        CreateRePassword = (EditText) findViewById(R.id.createRePassword);
    }

    public void authenticateSignUp (View view){
        String username;
        String password;
        String rePassword;

        username = CreateUserName.getText().toString();
        SetUserName(username);
        password = CreatePassword.getText().toString();
        rePassword = CreateRePassword.getText().toString();
        Intent intent = new Intent(this, LoginActivity.class);

        if(!password.isEmpty()&& !rePassword.isEmpty()&&password.equals(rePassword)&& validEmail(username))
        {
            SetPassword(password);
            Toast.makeText(getApplicationContext(), "Account created!",
                    Toast.LENGTH_LONG).show();

            this.startActivity(intent);
            finish();
        }
        else if (!validEmail(username)) {
            Toast.makeText(getApplicationContext(), "Wrong email address!",
                    Toast.LENGTH_SHORT).show();
        }
        else { Toast.makeText(getApplicationContext(), "Wrong password!",
                Toast.LENGTH_SHORT).show();
        }

    }
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void SetUserName(String userName)
    {
        this.UserName = userName;
    }

    public String GetUserName ()
    {
        return this.UserName;
    }


    public void SetPassword(String password)
    {
        this.Password = password;
    }

    public String GetPassword () {return this.Password;}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        finish();
    }
}
