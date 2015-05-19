package com.iha.genbrug;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DetailActivity extends Activity {

    Intent intent;

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
