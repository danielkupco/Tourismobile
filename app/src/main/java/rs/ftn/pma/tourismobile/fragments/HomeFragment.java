package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.rest.spring.annotations.RestService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.network.ServiceDBPedia;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getSimpleName();

    @RestService
    ServiceDBPedia serviceDBPedia;

    @Background
    @Click
    void btnDBPedia() {
        try {
            Field[] fields = Class.forName("rs.ftn.pma.tourismobile.model.Destination").getDeclaredFields();
            Log.e(TAG, Arrays.toString(fields));
            List<Field> ff = new ArrayList<>();
            for(Field f : fields) {
                Log.e(TAG, f.getType().toString());
                Log.e(TAG, String.class.getEnclosingClass().toString());
                if(f.getType().equals(String.class.getEnclosingClass().toString())
                        && !f.getName().equals(Destination.ID_FIELD)
                        && f.getName().endsWith("_FIELD")) {
                    ff.add(f);
                }
            }
            Log.e(TAG, ff.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
