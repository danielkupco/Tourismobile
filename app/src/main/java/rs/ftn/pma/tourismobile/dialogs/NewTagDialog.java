package rs.ftn.pma.tourismobile.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Toast;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.TagsAdapter;

/**
 * Created by danex on 5/15/16.
 */
public class NewTagDialog extends DialogFragment {

    private TagsAdapter tagsAdapter;


    public NewTagDialog setAdapter(TagsAdapter tagsAdapter) {
        this.tagsAdapter = tagsAdapter;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_new_tag, null))
                // Add action buttons
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Create", Toast.LENGTH_SHORT).show();
//                        String tagName = ((TextView) getView().findViewById(R.id.name)).getText().toString();
//                        String tagDescription = ((TextView) getView().findViewById(R.id.description)).getText().toString();
//                        Tag tag = new Tag(tagName, tagDescription);
//                        tagsAdapter.addTag(tag);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewTagDialog.this.getDialog().cancel();
                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }

}
