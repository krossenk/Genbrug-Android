package com.iha.genbrug;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;


public class DetailActivity extends Activity {

    Intent intent;
    Button infoBtn;
    Button logOutBtn;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        String imageId =(bundle.getString("imageId"));
        String header = (bundle.getString("headline"));
        String desc = (bundle.getString("desc"));
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

    public void showInfo (View v)
    {
        prefs =PreferenceManager.getDefaultSharedPreferences(this);

        Toast.makeText(this,prefs.getString("LocalUser", ""),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,prefs.getString("FBUser", ""),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,prefs.getString("ProfileURL", ""),Toast.LENGTH_SHORT).show();
    }

    public void logOut(View v)
    {
        prefs =PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("Islogin", false).commit();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this,LoginActivity.class);
        this.startActivity(intent);
        finish();
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
