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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.util.ValidationUtils;

/**
 * Dialog for creating tags. It extends DialogFragment so it is annotated with <code>@EFragment</code>.<br>
 * Created by danex on 5/15/16.
 */
@EFragment
public class NewTagDialog extends AppCompatDialogFragment {

    public static final String TAG = NewTagDialog.class.getSimpleName();

    @Bean
    TagDAOWrapper tagDAOWrapper;

    private View dialogLayout;

    private TextView firstInvalidField;

    private boolean canQueryChecked = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogLayout = inflater.inflate(R.layout.dialog_new_tag, null);

        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton(getString(R.string.btn_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing here because we override this button later to change the close behaviour.
                        // However, we still need this because on older versions of Android unless we
                        // pass a handler the button doesn't get instantiated
                        Toast.makeText(getActivity(), "Create", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewTagDialog.this.getDialog().cancel();
                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog != null) {

            final CheckBox cbCanQuery = (CheckBox) dialogLayout.findViewById(R.id.dbpCanQuery);
            final EditText etTagProperty = (EditText) dialogLayout.findViewById(R.id.tagProperty);
            final EditText etTagValue = (EditText) dialogLayout.findViewById(R.id.tagValue);
            cbCanQuery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    canQueryChecked = ((CheckBox)v).isChecked();
                    if(canQueryChecked) {
                        etTagProperty.setEnabled(true);
                        etTagValue.setEnabled(true);
                    }
                    else {
                        etTagProperty.setEnabled(false);
                        etTagValue.setEnabled(false);
                    }
                }
            });

            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final TextView tvName = (TextView) dialogLayout.findViewById(R.id.tagName);
                    final TextView tvDescription = (TextView) dialogLayout.findViewById(R.id.tagDescription);

                    boolean validForm = validateTextField(tvName);
                    validForm &= validateTextField(tvDescription);
                    if(canQueryChecked) {
                        validForm &= validateTextField(etTagProperty);
                        validForm &= validateTextField(etTagValue);
                    }
                    if (!validForm) {
                        firstInvalidField.requestFocus();
                        firstInvalidField = null;
                        return;
                    }

                    Tag tag;
                    if(canQueryChecked) {
                        tag = new Tag(tvName.getText().toString(), tvDescription.getText().toString(),
                                etTagProperty.getText().toString(), etTagValue.getText().toString(), true);
                    }
                    else {
                        tag = new Tag(tvName.getText().toString(), tvDescription.getText().toString());
                    }
                    tagDAOWrapper.createTag(tag);

                    // We must dismiss dialog otherwise it stays open!
                    dialog.dismiss();
                }
            });
        }
    }

    private boolean validateTextField(TextView field) {
        boolean valid = ValidationUtils.validateField(field,
                true, // no additional validation
                getString(R.string.error_field_required),
                getString(R.string.error_text_not_alphanum));
        if(!valid && firstInvalidField == null) {
            firstInvalidField = field;
        }
        return valid;
    }

}
