package rs.ftn.pma.tourismobile.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Tag;

/**
 * Created by Daniel Kupčo on 04.06.2016.
 */
@EActivity(R.layout.activity_tag_details)
@OptionsMenu(R.menu.action_bar_tag_details)
public class TagDetailsActivity extends AppCompatActivity {

    private static final String TAG = TagDetailsActivity.class.getSimpleName();

    @ViewById
    TextView tagName;

    @ViewById
    TextView tagProperty;

    @ViewById
    TextView tagValue;

    @ViewById
    TextView tagDescription;

    @ViewById
    TextView tagDbpCanQueryBy;

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @Extra
    int tagId;

    @ViewById
    Toolbar toolbar;

    private Tag tag;

    // must set toolbar after view layout is initialized
    @AfterViews
    void initViews() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(getString(R.string.title_tag_details));
            }
        }
    }

    @AfterExtras
    void injectExtra() {
        tag = tagDAOWrapper.findById(tagId);
    }

    @AfterViews
    void bindViews() {
        tagName.setText(tag.getName());
        tagDescription.setText(tag.getDescription());
        if (tag.isDbpCanQueryBy()) {
            tagProperty.setText(String.format("%s:  %s", getString(R.string.label_property_name), tag.getDbpProperty()));
            tagValue.setText(String.format("%s:  %s", getString(R.string.label_property_value), tag.getDbpValue()));
        }
        else {
            tagDbpCanQueryBy.setText(getString(R.string.label_dbp_can_query_by_false));
            tagProperty.setVisibility(View.GONE);
            tagValue.setVisibility(View.GONE);
        }
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
    void actionTaggedDestinations() {
        TaggedDestinationsActivity_.intent(this).tagID(tagId).start();
    }

}
