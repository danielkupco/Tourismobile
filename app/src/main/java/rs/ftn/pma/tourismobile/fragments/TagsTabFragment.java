package rs.ftn.pma.tourismobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.util.IBottomBarView;

/**
 * Created by Daniel Kupčo on 26.06.2016.
 */
public class TagsTabFragment extends Fragment implements IBottomBarView {

    private static final String TAG = TagsTabFragment.class.getSimpleName();

    private FragmentTabHost tabHost;

    private String lastTabTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_tag_tabs);

        Bundle arg1 = new Bundle();
        arg1.putBoolean(TagsFragment.DEFAULTS, true);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("Defaults"),
                TagsFragment_.class, arg1);
        lastTabTag = "Tab1";

        Bundle arg2 = new Bundle();
        arg2.putBoolean(TagsFragment.DEFAULTS, false);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("User defined"),
                TagsFragment_.class, arg2);

        tabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "clickwdawdadwa");
                hideBottomBar();
                tabHost.setCurrentTab(0);
            }
        });
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                Log.e(TAG, "idx: " + getCurrentTabIndex());
                Log.e(TAG, tabId);
                if(getCurrentTabIndex() == 0) {
                    Log.e(TAG, tabHost.getCurrentTabTag());
                    Log.e(TAG, getChildFragmentManager().findFragmentByTag(tabHost.getCurrentTabTag()).toString());
//                    hideBottomBar();
                    Fragment currentFragment = getChildFragmentManager().findFragmentByTag(lastTabTag);
                    if (currentFragment instanceof IBottomBarView) {
                        ((IBottomBarView) currentFragment).hideBottomBar();
                    }
                }
                lastTabTag = tabId;
            }
        });

        return tabHost;
    }

    public int getCurrentTabIndex() {
        return tabHost.getCurrentTab();
    }

    public boolean getCurrentTabAllowsSelectMode() {
        return tabHost.getCurrentTab() == 1;
    }

    @Override
    public boolean hideBottomBar() {
        Fragment currentFragment = getChildFragmentManager().findFragmentByTag(tabHost.getCurrentTabTag());
        if (currentFragment instanceof IBottomBarView) {
            return ((IBottomBarView) currentFragment).hideBottomBar();
        }
        return false;
    }

    @Override
    public boolean showBottomBar() {
        Fragment currentFragment = getChildFragmentManager().findFragmentByTag(tabHost.getCurrentTabTag());
        if (currentFragment instanceof IBottomBarView) {
            return ((IBottomBarView) currentFragment).showBottomBar();
        }
        return false;
    }
}
