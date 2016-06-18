package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.rest.spring.annotations.RestService;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.activities.MapsActivity_;
import rs.ftn.pma.tourismobile.network.RestDBPedia;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getSimpleName();

    @RestService
    RestDBPedia serviceDBPedia;

    @Background
    @Click
    void btnAction() {
        MapsActivity_.intent(this.getContext()).start();
    }

}
