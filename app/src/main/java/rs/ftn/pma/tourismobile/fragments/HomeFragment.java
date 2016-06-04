package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.network.ServiceDBPedia;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getSimpleName();

    @RestService
    ServiceDBPedia serviceDBPedia;

    @Background
    @Click
    void btnDBPedia() {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("query", "SELECT DISTINCT ?concept WHERE { ?s a ?concept . } LIMIT 50");
        params.set("format", "json");
        Object result = serviceDBPedia.queryDBPedia(params);
        Log.e(TAG, result.toString());
    }

}
