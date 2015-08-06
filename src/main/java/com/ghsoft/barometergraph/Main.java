package com.ghsoft.barometergraph;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ghsoft.barometergraph.data.RecordingData;
import com.ghsoft.barometergraph.fragments.AboutFragment;
import com.ghsoft.barometergraph.fragments.LiveGraphFragment;
import com.ghsoft.barometergraph.fragments.NoSensorFragment;
import com.ghsoft.barometergraph.fragments.RecordedDataViewFragment;
import com.ghsoft.barometergraph.fragments.RecordingListFragment;
import com.ghsoft.barometergraph.service.BarometerDataService;
import com.ghsoft.barometergraph.service.BarometerServiceConnection;
import com.ghsoft.barometergraph.views.IRecordingDataEvents;


public class Main extends ActionBarActivity implements BarometerServiceConnection.BarometerServiceEvents,
        View.OnClickListener, FragmentManager.OnBackStackChangedListener,
        IRecordingDataEvents, RecordedDataViewFragment.RecordedDataFragmentEvents {

    public static final String FRAGMENT_ID = "mContent";

    private BarometerDataService mService;
    private BarometerServiceConnection mServiceConnection;
    private FragmentManager mFragmentManager;
    private Intent mServiceIntent;
    private Settings mSettings;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();

        mSettings = new Settings(this);
        mFragmentManager = getFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);
        mServiceConnection = new BarometerServiceConnection(this);
        mServiceIntent = new Intent(this, BarometerDataService.class);
        startService(mServiceIntent);
        bindService(mServiceIntent, mServiceConnection, 0);

        findViewById(R.id.open_recordings).setOnClickListener(this);
        findViewById(R.id.open_about).setOnClickListener(this);

        if (savedInstanceState == null) {
            goToMainFragment();
        } else {
            refreshState();
        }
    }

    private void goToMainFragment() {
        launchFragment(new LiveGraphFragment());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentManager.putFragment(outState, Integer.toString(getFragmentCount() - 1), getCurrentFragment());
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    public void onServiceConnect(BarometerDataService service) {
        mService = service;
        mService.setSettings(mSettings);
        if (mService.hasSensor()) {
            if (getCurrentFragment() instanceof LiveGraphFragment) {
                ((LiveGraphFragment) getCurrentFragment()).setService(mService);
            }
        } else {
            mFragmentManager.popBackStackImmediate();
            launchFragment(new NoSensorFragment());
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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        if (mFragmentManager.getBackStackEntryCount() == 1) {
            stopService(mServiceIntent);
            finish();
            return;
        }
        mFragmentManager.popBackStack();

    }

    public void launchFragment(Fragment fragment) {
        if (getFragmentCount() == 0) {
            mFragmentManager.beginTransaction()
                    .add(getFragmentContainer(), fragment, getNewFragmentId())
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(getFragmentContainer(), fragment, getNewFragmentId())
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open_recordings && !getCurrentFragment().getClass().equals(RecordingListFragment.class)) {
            launchFragment(new RecordingListFragment());
        }
        if (v.getId() == R.id.open_about && !getCurrentFragment().getClass().equals(AboutFragment.class)) {
            launchFragment(new AboutFragment());
        }
        mDrawerLayout.closeDrawers();
    }


    protected int getFragmentCount() {
        return mFragmentManager.getBackStackEntryCount();
    }

    private Fragment getFragmentAt(int index) {
        return getFragmentCount() > 0 ? mFragmentManager.findFragmentByTag(Integer.toString(index)) : null;
    }

    protected Fragment getCurrentFragment() {
        return getFragmentAt(getFragmentCount() - 1);
    }

    private String getNewFragmentId() {
        return Integer.toString(getFragmentCount());
    }

    @Override
    public void onBackStackChanged() {
        refreshState();
        if (getCurrentFragment() instanceof RecordingListFragment) {
            ((RecordingListFragment) getCurrentFragment()).referesh();
        }
    }

    public void refreshState() {
        if (getCurrentFragment() instanceof LiveGraphFragment && mService != null) {
            ((LiveGraphFragment) getCurrentFragment()).setService(mService);
            getSupportActionBar().setElevation(getResources().getDimension(R.dimen.bar_elivation));
            getSupportActionBar().setTitle("Barometer Graph");
        }

        if (getCurrentFragment() instanceof RecordingListFragment) {
            getSupportActionBar().setElevation(getResources().getDimension(R.dimen.bar_elivation));
            getSupportActionBar().setTitle("Recorded Data");
        }

        if (getCurrentFragment() instanceof RecordedDataViewFragment) {
            getSupportActionBar().setTitle(((RecordedDataViewFragment) getCurrentFragment()).getFileName());
            getSupportActionBar().setElevation(getResources().getDimension(R.dimen.bar_elivation));
        }

        if (getCurrentFragment() instanceof AboutFragment) {
            getSupportActionBar().setElevation(0f);
            getSupportActionBar().setTitle(getResources().getString(R.string.about));
        }

        if (getCurrentFragment() instanceof NoSensorFragment) {
            getSupportActionBar().setElevation(0f);
        }

    }

    @Override
    public void onViewData(RecordingData data) {
        RecordedDataViewFragment recordedDataViewFragment = new RecordedDataViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RecordedDataViewFragment.PACKAGE_DATA_KEY, data);
        recordedDataViewFragment.setArguments(bundle);
        launchFragment(recordedDataViewFragment);
    }

    @Override
    public void onDelete() {
        mFragmentManager.popBackStack();

    }

}
