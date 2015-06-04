package com.iha.genbrug;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.login.LoginManager;
import com.iha.genbrug.give.GiveActivity;
import com.melnykov.fab.FloatingActionButton;

import webservice.User;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
    ViewPager viewPager = null;
    FloatingActionButton fab;
    private ImageButton mIbOptions;
    private Button mBtnCancelOptions;
    private Button mBtnSignOut;
    private Dialog mDialog;
    private GlobalSettings  globalSettings = GlobalSettings.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.ab_layout);

        mIbOptions = (ImageButton) findViewById(R.id.ib_options);
        viewPager = (ViewPager) findViewById(R.id.pager);
        fab = (FloatingActionButton) findViewById(R.id.btn_fab);

        mIbOptions.setOnClickListener(this);
        fab.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new MyAdapter(fragmentManager));
        // Pager sliding tabs:
        // https://github.com/astuetz/PagerSlidingTabStrip
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
         moveTaskToBack(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fab:
                startActivity(new Intent(this, GiveActivity.class));
                break;

            case R.id.ib_options:
                mDialog = new Dialog(this);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.setContentView(R.layout.dialog_options);
                mBtnCancelOptions = (Button) mDialog.findViewById(R.id.btn_cancel_options);
                mBtnSignOut = (Button) mDialog.findViewById(R.id.btn_sign_out_options);
                mBtnCancelOptions.setOnClickListener(this);
                mBtnSignOut.setOnClickListener(this);
                mDialog.show();
                break;

            case R.id.btn_cancel_options:
                mDialog.dismiss();
                break;

            case R.id.btn_sign_out_options:
                //Reset all information from user
                globalSettings.sharedPreferences.edit().putBoolean("Islogin", false).commit();
                globalSettings.saveUserToPref(new User());
                LoginManager.getInstance().logOut();
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                mDialog.dismiss();
                break;
        }
    }
}

class MyAdapter extends FragmentPagerAdapter {
    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        if(i == 0){
            fragment = new FeedFragment();
        }else if(i == 1){
            fragment = new SubscriptionsFragment();
        }else if(i == 2){
            fragment = new PublicationsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Feed";
        }else if(position == 1){
            return "Takes";
        }else if(position == 2){
            return "Gives";
        }
        return null;
    }

}
