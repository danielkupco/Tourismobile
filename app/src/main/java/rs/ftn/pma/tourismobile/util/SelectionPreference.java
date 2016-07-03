package rs.ftn.pma.tourismobile.util;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Daniel Kupčo on 28.06.2016.
 */
@SharedPref(SharedPref.Scope.UNIQUE)
public interface SelectionPreference {

    @DefaultBoolean(false)
    boolean selectionMode();

    String selectedItemIDs();

}
