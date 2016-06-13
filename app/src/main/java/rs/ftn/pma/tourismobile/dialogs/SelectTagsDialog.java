package rs.ftn.pma.tourismobile.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.SelectTagsAdapter;
import rs.ftn.pma.tourismobile.database.dao.wrapper.DestinationDAOWrapper;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TaggedDestinationDAOWrapper;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;

/**
 * Created by Daniel Kupčo on 11.06.2016.
 */
@EFragment
public class SelectTagsDialog extends AppCompatDialogFragment {

    public static final String TAG = SelectTagsDialog.class.getSimpleName();

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @Bean
    DestinationDAOWrapper destinationDAOWrapper;

    @Bean
    TaggedDestinationDAOWrapper taggedDestinationDAOWrapper;

    @Bean
    DBPediaUtils dbPediaUtils;

    @Bean
    SelectTagsAdapter selectTagsAdapter;

    @FragmentArg
    int destinationWikiPageID;

    private View dialogLayout;

    private ListView tagsList;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogLayout = inflater.inflate(R.layout.dialog_select_tags, null);

        // binding adapter to the list
        tagsList = (ListView) dialogLayout.findViewById(R.id.lvTags);
        tagsList.setAdapter(selectTagsAdapter);

        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton(getString(R.string.btn_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing here because we override this button later to change the close behaviour.
                        // However, we still need this because on older versions of Android unless we
                        // pass a handler the button doesn't get instantiated
                    }
                })
                .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SelectTagsDialog.this.getDialog().cancel();
//                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
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
                    saveDestinationAndTags();
                    SelectTagsDialog.this.getDialog().cancel();
                }
            });
        }
    }

    @Background
    void saveDestinationAndTags() {
        // find destination if exists locally
        Destination destination = destinationDAOWrapper.findByWikiPageID(destinationWikiPageID);
        // if it does not
        if(destination == null) {
            // get fully detailed destination
            destination = dbPediaUtils.queryDBPediaForDetails(destinationWikiPageID);
            // save destination in database
            destinationDAOWrapper.create(destination);
        }
        else {
            // delete previous tags for destination
            taggedDestinationDAOWrapper.deleteAllForDestination(destination.getId());
        }
        // bind destination to selected tags
        taggedDestinationDAOWrapper.createAllTagsForDestination(destination, selectTagsAdapter.getSelectedTags());
    }

}
