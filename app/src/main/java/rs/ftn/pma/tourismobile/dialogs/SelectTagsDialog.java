package rs.ftn.pma.tourismobile.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.SelectTagsAdapter;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Tag;

/**
 * Created by Daniel Kupčo on 11.06.2016.
 */
@EFragment
public class SelectTagsDialog extends AppCompatDialogFragment {

    public static final String TAG = SelectTagsDialog.class.getSimpleName();

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @Bean
    SelectTagsAdapter selectTagsAdapter;

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
//                        NewTagDialog.this.getDialog().cancel();
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
//                    Log.e(TAG, "Count: " + tagsList.getChildCount());
                    Log.e(TAG, "Selected are:");
                    for(Tag t : selectTagsAdapter.getSelectedTags()) {
                        Log.e(TAG, t.getName());
                    }

//                    for (int i = 0; i < tagsList.getChildCount(); i++) {
//                        TagCheckboxItemView tagCheckboxItemView = (TagCheckboxItemView) tagsList.getChildAt(i);
//                        CheckBox checkBox = tagCheckboxItemView.getTagCheckbox();
//                        Log.e(TAG, checkBox.getText().toString() + " - " + checkBox.isChecked());
//                    }
                }
            });
        }
    }

}
