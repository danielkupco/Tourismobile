package rs.ftn.pma.tourismobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.ftn.pma.tourismobile.R;

/**
 * Created by Daniel Kupčo on 26.06.2016.
 */
public class TagsTabFragment extends Fragment {

    private FragmentTabHost tabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_tag_tabs);

        Bundle arg1 = new Bundle();
        arg1.putBoolean(TagsFragment.DEFAULTS, true);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("Defaults"),
                TagsFragment_.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putBoolean(TagsFragment.DEFAULTS, false);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("User defined"),
                TagsFragment_.class, arg2);

        return tabHost;
    }

}
