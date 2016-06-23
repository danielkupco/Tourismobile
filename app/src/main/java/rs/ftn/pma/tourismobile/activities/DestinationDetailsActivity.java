package rs.ftn.pma.tourismobile.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.database.dao.wrapper.DestinationDAOWrapper;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TaggedDestinationDAOWrapper;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.services.DBPediaService;
import rs.ftn.pma.tourismobile.services.DBPediaService_;
import rs.ftn.pma.tourismobile.services.IServiceActivity;
import rs.ftn.pma.tourismobile.util.MapUtils;

/**
 * Created by Daniel Kupčo on 08.06.2016.
 */
@EActivity(R.layout.activity_destination_details)
@OptionsMenu(R.menu.action_bar_destination_details)
public class DestinationDetailsActivity extends AppCompatActivity implements IServiceActivity {

    private static final String TAG = DestinationDetailsActivity.class.getSimpleName();

    @ViewById
    MaterialProgressBar progressBar;

    @ViewById
    AppBarLayout appBarLayout;

    @ViewById
    CollapsingToolbarLayout collapsingToolbar;

    @ViewById
    Toolbar toolbar;

    @ViewById
    SimpleDraweeView destinationImage;

    @ViewById
    TextView tvDestinationName;

    @ViewById
    TextView tvDestinationDescription;

    @ViewById
    TextView tvDestinationTags;

    @ViewById
    TextView tvDestinationLatitude;

    @ViewById
    TextView tvDestinationLongitude;

    @ViewById
    Button btnWikiLink;

    @Extra
    int destinationID = 0;

    @Extra
    int wikiPageID;

    @Bean
    DestinationDAOWrapper destinationDAOWrapper;

    @Bean
    TaggedDestinationDAOWrapper taggedDestinationDAOWrapper;

    private Destination destination;

    private DBPediaService mService;

    private boolean mBound = false;

    private static final String DESTINATION_ID_STATE = "destination_id";
    private static final String DESTINATION_WIKI_PAGE_STATE = "destination_wiki_page";

    /** Defines callbacks for mService binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've mBound to LocalService, cast the IBinder and get LocalService instance
            DBPediaService.ServiceBinder binder = (DBPediaService.ServiceBinder) service;
            DestinationDetailsActivity.this.mService = binder.getService();
            mBound = true;
            // loading destination only when we make sure that service is connected
            loadDestination();
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


    // must set toolbar after view layout is initialized
    @SuppressWarnings("ConstantConditions")
    @AfterViews
    void initViews() {
        ViewCompat.setTransitionName(appBarLayout, TAG);
        supportPostponeEnterTransition();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_destination_details));
    }

    @Background
    void loadDestination() {
        try {
            if(mBound) {
                if (destinationID > 0) {
                    this.destination = destinationDAOWrapper.findById(destinationID);
                } else {
                    this.destination = mService.queryDestinationDetails(wikiPageID);
                }
            }
            queryDBPediaSuccess(destination);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        finally {
            updateUIAfterQuery();
        }

    }

    @UiThread
    void queryDBPediaSuccess(Destination destination) {
        if(destination == null) {
            tvDestinationName.setText(R.string.error_dst_not_found);
        }
        else {
            bind(destination);
        }
        updateUIAfterQuery();
    }

    public void bind(Destination destination) {
        this.destination = destination;
        destinationImage.setImageURI(Uri.parse(destination.getImageURI()));
        tvDestinationName.setText(destination.getName());
        tvDestinationDescription.setText(destination.getDescription());
        tvDestinationLatitude.setText(String.format("Latitude:  %.2f", destination.getLatitude()));
        tvDestinationLongitude.setText(String.format("Latitude:  %.2f", destination.getLongitude()));
        // destination tags
        StringBuilder stringBuilder = new StringBuilder("Tags: ");
        for(Tag tag : taggedDestinationDAOWrapper.findAllTagsForDestination(destination)) {
            stringBuilder.append(tag.getName()).append(", ");
        }
        final int length = stringBuilder.length();
        stringBuilder.deleteCharAt(length - 1);
        stringBuilder.deleteCharAt(length - 2);
        tvDestinationTags.setText(stringBuilder.toString());
    }

    @UiThread
    void updateUIAfterQuery() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    // must set explicitly because home ID is not part of our menu layout
    @OptionsItem(android.R.id.home)
    void home() {
        onBackPressed();
    }

    @OptionsItem
    void actionMap() {
        MapUtils.setDestinations(destination);
        MapsActivity_.intent(this).start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DESTINATION_ID_STATE, destinationID);
        outState.putInt(DESTINATION_WIKI_PAGE_STATE, wikiPageID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            destinationID = savedInstanceState.getInt(DESTINATION_ID_STATE);
            wikiPageID = savedInstanceState.getInt(DESTINATION_WIKI_PAGE_STATE);
        }
    }

    @Click
    void btnWikiLink() {
        Uri wikiUri = Uri.parse(destination.getWikiLink());
        if(!wikiUri.toString().startsWith("http://") && !wikiUri.toString().startsWith("https://")) {
            wikiUri = Uri.parse(String.format("http://%s", wikiUri.toString()));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, wikiUri);
        startActivity(intent);
    }

}
