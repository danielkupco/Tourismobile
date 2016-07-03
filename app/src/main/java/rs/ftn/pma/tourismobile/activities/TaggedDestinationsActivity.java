package rs.ftn.pma.tourismobile.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.TaggedDestinationsAdapter;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.util.MapUtils;

/**
 * Created by Daniel Kupčo on 03.07.2016.
 */
@EActivity(R.layout.activity_tagged_destinations)
@OptionsMenu(R.menu.action_bar_destination_list)
public class TaggedDestinationsActivity extends AppCompatActivity {

    @Bean
    TaggedDestinationsAdapter taggedDestinationsAdapter;

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @ViewById
    RecyclerView destinationsList;

    @ViewById
    Toolbar toolbar;

    @Extra
    int tagID;

    // must set toolbar after view layout is initialized
    @AfterViews
    void initViews() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                Tag tag = tagDAOWrapper.findById(tagID);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Tagged by " + tag.getName());
            }
        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        destinationsList.setHasFixedSize(true);

        // must set layout manager to recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        destinationsList.setLayoutManager(layoutManager);

        // binding adapter to the view
        taggedDestinationsAdapter.bindAdapterToRecyclerView(destinationsList);
        taggedDestinationsAdapter.loadItems(tagID);
    }

    // must set explicitly because home ID is not part of our menu layout
    @OptionsItem(android.R.id.home)
    void home() {
        onBackPressed();
    }

    @OptionsItem
    void actionSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @OptionsItem
    void actionMap() {
        MapUtils.setDestinations(taggedDestinationsAdapter.getItems());
        MapsActivity_.intent(this).start();
    }

}
