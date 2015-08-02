package com.ghsoft.barometergraph;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ghsoft.barometergraph.fragments.LiveGraphFragment;
import com.ghsoft.barometergraph.fragments.RecordingListFragment;
import com.ghsoft.barometergraph.service.BarometerDataService;
import com.ghsoft.barometergraph.service.BarometerServiceConnection;

import java.util.Stack;


public class Main extends ActionBarActivity implements BarometerServiceConnection.BarometerServiceEvents, LiveGraphFragment.LiveGraphFragmentEvents, View.OnClickListener {


    public static final String FRAGMENT_ID = "mContent";

    private BarometerDataService mService;
    private BarometerServiceConnection mServiceConnection;
    private FragmentManager mFragmentManager;
    private Intent mServiceIntent;
    private Stack<Fragment> mFragStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();

        mFragStack = new Stack<Fragment>();

        mFragmentManager = getFragmentManager();
        mServiceConnection = new BarometerServiceConnection(this);
        mServiceIntent = new Intent(this, BarometerDataService.class);
        startService(mServiceIntent);
        bindService(mServiceIntent, mServiceConnection, 0);
        Log.e("BINDING", "BINDING");

        ((Button)findViewById(R.id.open_recordings)).setOnClickListener(this);


        if (savedInstanceState == null) {
            goToMainFragment();
        }
    }

    private void goToMainFragment() {
        LiveGraphFragment liveGraphFragment = new LiveGraphFragment();
        mFragStack.push(liveGraphFragment);
        mFragmentManager.beginTransaction().replace(getFragmentContainer(), liveGraphFragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentManager.putFragment(outState, FRAGMENT_ID, mFragStack.peek());
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    public void onServiceConnect(BarometerDataService service) {
        mService = service;
        Fragment current = mFragStack.peek();
        if (current instanceof LiveGraphFragment) {
            ((LiveGraphFragment) current).setService(mService);
        }
    }

    @Override
    public void onServiceDisconnect() {
    }

    public static int getFragmentContainer() {
        return R.id.container;
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle;
        mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, toolbar,
                R.string.abc_action_bar_home_description, R.string.abc_action_bar_home_description
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        if (mFragStack.size() > 1) {
            popFragment();
        } else {
            mFragmentManager.beginTransaction().remove(mFragStack.pop()).commit();
            stopService(mServiceIntent);
            mService.stopSelf();
            finish();
        }

    }

    private void popFragment() {
        mFragmentManager.popBackStack();
        mFragStack.pop();
    }

    private void showFragment(Fragment f) {
        mFragmentManager.beginTransaction().hide(mFragStack.peek()).add(getFragmentContainer(), f).addToBackStack(f.getClass().getName()).commit();
        mFragStack.push(f);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open_recordings) {
            showFragment(new RecordingListFragment());
        }
    }
}
