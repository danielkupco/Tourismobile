package rs.ftn.pma.tourismobile.util;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Daniel Kupčo on 15.06.2016.
 */
@SharedPref(SharedPref.Scope.UNIQUE)
public interface FilterPreferences {

    String byName();

    String byDescription();

    String bySelectedTags();

    String sortBy();

    boolean sortOrder();

}
