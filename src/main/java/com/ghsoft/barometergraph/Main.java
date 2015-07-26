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

import com.ghsoft.barometergraph.fragments.LiveGraphFragment;
import com.ghsoft.barometergraph.service.BarometerDataService;
import com.ghsoft.barometergraph.service.BarometerServiceConnection;


public class Main extends ActionBarActivity implements BarometerServiceConnection.BarometerServiceEvents, LiveGraphFragment.LiveGraphFragmentEvents {


    public static final String FRAGMENT_ID = "mContent";

    private BarometerDataService mService;
    private BarometerServiceConnection mServiceConnection;
    private FragmentManager mFragmentManager;
    private Fragment mContent;
    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();

        mFragmentManager = getFragmentManager();
        mServiceConnection = new BarometerServiceConnection(this);
        mServiceIntent = new Intent(this, BarometerDataService.class);
        startService(mServiceIntent);
        bindService(mServiceIntent, mServiceConnection, 0);
        Log.e("BINDING", "BINDING");


        if (savedInstanceState != null) {
            mContent = mFragmentManager.getFragment(savedInstanceState, FRAGMENT_ID);
            mFragmentManager.executePendingTransactions();
        } else {
            goToMainFragment();
        }
    }

    private void goToMainFragment() {
        LiveGraphFragment liveGraphFragment = new LiveGraphFragment();
        mContent = liveGraphFragment;
        mFragmentManager.beginTransaction().replace(getFragmentContainer(), liveGraphFragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentManager.putFragment(outState, FRAGMENT_ID, mContent);
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    public void onServiceConnect(BarometerDataService service) {
        mService = service;
        if (mContent instanceof LiveGraphFragment) {
            ((LiveGraphFragment) mContent).setService(mService);
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
        if (mContent instanceof LiveGraphFragment) {
            mFragmentManager.beginTransaction().remove(mContent).commit();
            stopService(mServiceIntent);
            mService.stopSelf();
            finish();
        }
    }
}
