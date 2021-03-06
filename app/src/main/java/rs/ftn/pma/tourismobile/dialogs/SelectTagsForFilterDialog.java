package rs.ftn.pma.tourismobile.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.activities.DestinationFilterActivity;
import rs.ftn.pma.tourismobile.adapters.SelectTagsAdapter;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.util.FilterPreferences_;

/**
 * Created by Daniel Kupčo on 15.06.2016.
 */
@EFragment
public class SelectTagsForFilterDialog extends AppCompatDialogFragment {

    public static final String TAG = SelectTagsForFilterDialog.class.getSimpleName();

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @Bean
    SelectTagsAdapter selectTagsAdapter;

    @Pref
    FilterPreferences_ filterPreferences;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogLayout = inflater.inflate(R.layout.dialog_select_tags, null);

        // If there are already selected tags for filters
        // Load their positions from preferences and select them
        List<Integer> positions = new ArrayList<>();
        if(filterPreferences.bySelectedTags().exists() && filterPreferences.bySelectedTags().get().length() > 0) {
            String selectedPositions = filterPreferences.bySelectedTags().get();
            String[] positionTokens = selectedPositions.split(",");
            for(String position : positionTokens) {
                positions.add(Integer.valueOf(position));
            }
            selectTagsAdapter.setSelectedIndices(positions);
        }
        else {
            selectTagsAdapter.setSelectedIndices(positions);
        }

        // binding adapter to the list
        ListView tagsList = (ListView) dialogLayout.findViewById(R.id.lvTags);
        selectTagsAdapter.setItems(tagDAOWrapper.findAllForFilters());
        tagsList.setAdapter(selectTagsAdapter);

        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton(getString(R.string.btn_select), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing here because we override this button later to change the close behaviour.
                        // However, we still need this because on older versions of Android unless we
                        // pass a handler the button doesn't get instantiated
                    }
                })
                .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SelectTagsForFilterDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog != null) {

            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // get selected positions, concatenate them and store them in preferences
                    List<Integer> selectedPositions = selectTagsAdapter.getSelectedIndices();
                    if(selectedPositions.size() > 0) {
                        filterPreferences.bySelectedTags().put(TextUtils.join(",", selectedPositions));
                    }
                    else {
                        filterPreferences.bySelectedTags().remove();
                    }
                    Activity parentActivity = getActivity();
                    if (parentActivity != null && parentActivity instanceof DestinationFilterActivity) {
                        ((DestinationFilterActivity) parentActivity).setSelectedTagsNumber(selectedPositions.size());
                    }

                    SelectTagsForFilterDialog.this.getDialog().cancel();
                }
            });
        }
    }

}
