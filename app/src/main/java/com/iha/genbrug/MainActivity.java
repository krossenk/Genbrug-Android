package com.iha.genbrug;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.iha.genbrug.give.GiveActivity;
import com.melnykov.fab.FloatingActionButton;



public class MainActivity extends FragmentActivity implements View.OnClickListener {
    ViewPager viewPager = null;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.ab_layout);
        fab = (FloatingActionButton) findViewById(R.id.btn_fab);
        fab.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new MyAdapter(fragmentManager));
        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
         moveTaskToBack(true);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_fab){
            startActivity(new Intent(this, GiveActivity.class));
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
