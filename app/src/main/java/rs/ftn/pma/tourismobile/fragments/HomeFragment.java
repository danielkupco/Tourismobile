package rs.ftn.pma.tourismobile.fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.rest.spring.annotations.RestService;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.network.RestDBPedia;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getSimpleName();

    @RestService
    RestDBPedia serviceDBPedia;

    @Background
    @Click
    void btnAction() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        boolean splashOn = SP.getBoolean(getString(R.string.pref_turn_splash_key), true);
        String splashDuration = SP.getString(getString(R.string.pref_splash_duration_key),
                getString(R.string.pref_splash_duration_default));
        String destinationsPerPage = SP.getString(getString(R.string.pref_destinations_per_page_key),
                getString(R.string.pref_destinations_per_page_default));
        Log.e(TAG, splashOn + "");
        Log.e(TAG, splashDuration);
        Log.e(TAG, destinationsPerPage);
//        MapsActivity_.intent(this.getContext()).start();
    }

}
