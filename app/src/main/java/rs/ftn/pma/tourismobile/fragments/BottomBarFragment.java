package rs.ftn.pma.tourismobile.fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.util.IBottomBarView;

/**
 * Created by Daniel Kupčo on 02.07.2016.
 */
public abstract class BottomBarFragment extends Fragment implements IBottomBarView {

    private static final String TAG = BottomBarFragment.class.getSimpleName();

    protected BottomBar bottomBar;
    protected Bundle savedInstanceState;
    // first bottom bar button is called initially so we need to ignore that
    protected boolean firstTimeLoading = true;
    protected static final String BOTTOM_BAR_SHOWING = "bottom_bar_showing";
    protected static final String FIRST_TIME_LOADING = "first_time_loading";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        // must attach to activity in order to be visible even after fragment replacing
        bottomBar = BottomBar.attach(getActivity(), savedInstanceState);
        // Show all titles even when there's more than three tabs.
        // This BottomBar already has items! You must call the forceFixedMode() method before specifying any items.
        bottomBar.useFixedMode();
        bottomBar.noTopOffset();
        // Use custom text appearance in tab titles.
        bottomBar.setTextAppearance(R.style.BB_BottomBarItem_Fixed_TitleAppearance);
        bottomBar.setItems(R.menu.bottom_bar_menu);

        // only show bottom bar if it was previously shown
        if (savedInstanceState == null || !savedInstanceState.getBoolean(BOTTOM_BAR_SHOWING)) {
            bottomBar.hide();
        }

        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                Log.e(TAG, "select with: " + firstTimeLoading + " - " + bottomBar);
                switch (menuItemId) {
                    case R.id.bbSelectAll: {
                        Log.e(TAG, "selected all");
                        selectAllBarBtn();
                        break;
                    }
                    case R.id.bbClearSelection: {
                        Log.e(TAG, "selected clear");
                        clearSelectionBarBtn();
                        break;
                    }
                    case R.id.bbDelete: {
                        Log.e(TAG, "selected delete");
                        deleteBarBtn();
                        break;
                    }
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                Log.e(TAG, "reselect with: " + firstTimeLoading + " - " + bottomBar);
                switch (menuItemId) {
                    case R.id.bbSelectAll: {
                        Log.e(TAG, "reselected all");
                        selectAllBarBtn();
                        break;
                    }
                    case R.id.bbClearSelection: {
                        Log.e(TAG, "reselected clear");
                        clearSelectionBarBtn();
                        break;
                    }
                    case R.id.bbDelete: {
                        Log.e(TAG, "reselected delete");
                        deleteBarBtn();
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
        outState.putBoolean(BOTTOM_BAR_SHOWING, bottomBar.isShown());
        outState.putBoolean(FIRST_TIME_LOADING, firstTimeLoading);
    }

    protected abstract void selectAllBarBtn();
    protected abstract void clearSelectionBarBtn();
    protected abstract void deleteBarBtn();

    public abstract boolean showBottomBar();
    public abstract boolean hideBottomBar();

}
