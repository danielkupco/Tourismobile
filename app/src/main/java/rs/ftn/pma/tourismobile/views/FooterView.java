package rs.ftn.pma.tourismobile.views;

import android.content.Context;
import android.widget.LinearLayout;

import org.androidannotations.annotations.EViewGroup;

import rs.ftn.pma.tourismobile.R;

/**
 * Simple empty footer view high enough to push list items above floating action button.
 * Created by danielkupco on 6/2/16.
 */
@EViewGroup(R.layout.footer)
public class FooterView extends LinearLayout {

    public FooterView(Context context) {
        super(context);
    }

}
