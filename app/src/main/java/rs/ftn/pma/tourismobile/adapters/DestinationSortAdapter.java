package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import org.androidannotations.annotations.EBean;

import rs.ftn.pma.tourismobile.model.Destination;

/**
 * Created by Daniel Kupčo on 15.06.2016.
 */
@EBean
public class DestinationSortAdapter extends ArrayAdapter<String> {

    private static final String TAG = DestinationSortAdapter.class.getSimpleName();

    public DestinationSortAdapter(Context context) {
        // Selected item layout
        super(context, android.R.layout.simple_spinner_item);
        // Dropdown item layout
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.addAll(Destination.NAME_FIELD, Destination.COMMENT_FIELD,
                Destination.LATITUDE_FIELD, Destination.LONGITUDE_FIELD);
    }

}
