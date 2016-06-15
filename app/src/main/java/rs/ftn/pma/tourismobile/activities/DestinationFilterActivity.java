package rs.ftn.pma.tourismobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.dialogs.SelectTagsForFilterDialog_;
import rs.ftn.pma.tourismobile.util.FilterPreferences_;

/**
 * Created by Daniel Kupčo on 14.06.2016.
 */
@EActivity(R.layout.activity_destination_filters)
public class DestinationFilterActivity extends AppCompatActivity {

    private static String TAG = DestinationFilterActivity.class.getSimpleName();

    @Pref
    FilterPreferences_ filterPreferences;

    @ViewById
    TextView byName;

    @ViewById
    TextView byDescription;

    @Click
    void btnSelectTags() {
        SelectTagsForFilterDialog_.builder().build().show(getSupportFragmentManager(), TAG);
    }

    @Click
    void btnClearFilters() {
        filterPreferences.edit()
                .byName().remove()
                .byDescription().remove()
                .bySelectedTags().remove();
    }

    @Click
    void btnApplyFilters() {
        String nameFilter = byName.getText().toString().trim();
        String descriptionFilter = byDescription.getText().toString().trim();
        filterPreferences.byName().put(nameFilter);
        filterPreferences.byDescription().put(descriptionFilter);
        finish();
    }

}
