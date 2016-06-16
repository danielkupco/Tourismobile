package rs.ftn.pma.tourismobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import fr.ganfra.materialspinner.MaterialSpinner;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.DestinationSortAdapter;
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

    @ViewById
    MaterialSpinner spinnerSortBy;

    @ViewById
    RadioButton rbAscending;

    @ViewById
    RadioButton rbDescending;

    @Bean
    DestinationSortAdapter destinationSortAdapter;

    @AfterViews
    void initializeViews() {
        spinnerSortBy.setAdapter(destinationSortAdapter);

        byName.setText(filterPreferences.byName().getOr(""));
        byDescription.setText(filterPreferences.byDescription().getOr(""));
        int position = filterPreferences.sortBy().exists() ?
                destinationSortAdapter.getPosition(filterPreferences.sortBy().get()) : 0;
        spinnerSortBy.setSelection(position);
        if(filterPreferences.sortOrder().exists()) {
            rbAscending.setChecked(filterPreferences.sortOrder().get());
            rbDescending.setChecked(!filterPreferences.sortOrder().get());
        }
    }

    @Click
    void btnSelectTags() {
        SelectTagsForFilterDialog_.builder().build().show(getSupportFragmentManager(), TAG);
    }

    @Click
    void btnClearFilters() {
        filterPreferences.edit()
                .byName().remove()
                .byDescription().remove()
                .bySelectedTags().remove()
                .sortBy().remove()
                .sortOrder().remove();
        byName.setText("");
        byDescription.setText("");
        spinnerSortBy.setSelection(0);
        rbAscending.setChecked(true);
        rbDescending.setChecked(false);
    }

    @Click
    void btnApplyFilters() {
        String nameFilter = byName.getText().toString().trim();
        String descriptionFilter = byDescription.getText().toString().trim();
        filterPreferences.byName().put(nameFilter);
        filterPreferences.byDescription().put(descriptionFilter);
        if(spinnerSortBy.getSelectedItemPosition() > 0) {
            String sortByFilter = (String) spinnerSortBy.getSelectedItem();
            filterPreferences.sortBy().put(sortByFilter);
        }
        filterPreferences.sortOrder().put(rbAscending.isChecked());
        finish();
    }

}
