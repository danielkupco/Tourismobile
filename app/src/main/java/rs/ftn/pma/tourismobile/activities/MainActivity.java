package rs.ftn.pma.tourismobile.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.fragments.DestinationsFragment_;
import rs.ftn.pma.tourismobile.fragments.FavouritesFragment_;
import rs.ftn.pma.tourismobile.fragments.HomeFragment;
import rs.ftn.pma.tourismobile.fragments.HomeFragment_;
import rs.ftn.pma.tourismobile.fragments.StoredDestinationsFragment_;
import rs.ftn.pma.tourismobile.fragments.TagsTabFragment;
import rs.ftn.pma.tourismobile.services.DBPediaService;
import rs.ftn.pma.tourismobile.services.DBPediaService_;
import rs.ftn.pma.tourismobile.services.IServiceActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements IServiceActivity,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DBPediaService mService;

    private boolean mBound = false;

    /** Defines callbacks for mService binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've mBound to LocalService, cast the IBinder and get LocalService instance
            DBPediaService.ServiceBinder binder = (DBPediaService.ServiceBinder) service;
            MainActivity.this.mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the mService
        Intent intent = new Intent(this, DBPediaService_.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the mService
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public DBPediaService getDBPediaService() {
        return mService;
    }

    @AfterViews
    void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // set HomeFragment at the beginning
            if(getSupportFragmentManager().findFragmentByTag(TAG) == null) {
                // Create a new Fragment to be placed in the activity layout
                HomeFragment fragment = HomeFragment_.builder().build();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                fragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment, TAG).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // update the main content by replacing fragments
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_maps: {
                MapsActivity_.intent(this).start();
                break;
            }
            case R.id.nav_destinations: {
                fragment = DestinationsFragment_.builder().build();
                break;
            }
            case R.id.nav_favourites: {
                fragment = FavouritesFragment_.builder().build();
                break;
            }
            case R.id.nav_tags: {
//                fragment = TagsFragment_.builder().build();
                fragment = new TagsTabFragment();
                break;
            }
            case R.id.nav_storage: {
                fragment = StoredDestinationsFragment_.builder().build();
                break;
            }
            case R.id.nav_settings: {
                break;
            }
            case R.id.nav_exit: {
                break;
            }
            default: {
                fragment = HomeFragment_.builder().build();
            }
        }

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, TAG).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
