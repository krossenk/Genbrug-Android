package com.iha.genbrug;

import android.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;

import webservice.RecycleWebService;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    ViewPager viewPager = null;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new MyAdapter(fragmentManager));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ActionBar.Tab tab1 = actionBar.newTab();
        tab1.setText("Feed");
        tab1.setTabListener(this);

        ActionBar.Tab tab2 = actionBar.newTab();
        tab2.setText("Takes");
        tab2.setTabListener(this);

        ActionBar.Tab tab3 = actionBar.newTab();
        tab3.setText("Gives");
        tab3.setTabListener(this);

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

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
        String title = new String();
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
