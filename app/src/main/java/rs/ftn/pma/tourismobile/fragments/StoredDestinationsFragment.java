package rs.ftn.pma.tourismobile.fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.StoredDestinationsAdapter;
import rs.ftn.pma.tourismobile.database.dao.wrapper.DestinationDAOWrapper;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TaggedDestinationDAOWrapper;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.util.EndlessRecyclerViewScrollListener;
import rs.ftn.pma.tourismobile.util.IBottomBarView;
import rs.ftn.pma.tourismobile.util.PreferenceUtil;
import rs.ftn.pma.tourismobile.util.SelectionPreference_;

/**
 * Created by Daniel Kupčo on 13.06.2016.
 */
@EFragment(R.layout.fragment_stored_destinations)
public class StoredDestinationsFragment extends Fragment implements IBottomBarView {

    private static final String TAG = StoredDestinationsFragment.class.getSimpleName();

    @Bean
    StoredDestinationsAdapter storedDestinationsAdapter;

    @Bean
    DestinationDAOWrapper destinationDAOWrapper;

    @Bean
    TaggedDestinationDAOWrapper taggedDestinationDAOWrapper;

    @ViewById
    RecyclerView destinationsList;

    @ViewById
    MaterialProgressBar progressBar;

    @Pref
    SelectionPreference_ selectionPreference;

    private BottomBar bottomBar;

    private Bundle savedInstanceState;

    // first bottom bar button is called initially so we need to ignore that
    private boolean firstTimeLoading = true;

    private static final String BOTTOM_BAR_SHOWING = "bottom_bar_showing";
    private static final String FIRST_TIME_LOADING = "first_time_loading";

    @AfterViews
    void bindAdapter() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        destinationsList.setHasFixedSize(true);

        // must set layout manager to recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        destinationsList.setLayoutManager(layoutManager);

        destinationsList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                progressBar.setVisibility(View.VISIBLE);
                loadItemsForPage(page);
            }
        });

        // binding adapter to the view
        storedDestinationsAdapter.bindAdapterToRecyclerView(destinationsList);
        loadItemsForPage(0);

        bottomBar = BottomBar.attach(getActivity().findViewById(R.id.container), savedInstanceState);
        // Show all titles even when there's more than three tabs.
        // This BottomBar already has items! You must call the forceFixedMode() method before specifying any items.
        bottomBar.useFixedMode();
        bottomBar.noTopOffset();
        // Use custom text appearance in tab titles.
        bottomBar.setTextAppearance(R.style.BB_BottomBarItem_Fixed_TitleAppearance);
        bottomBar.setItems(R.menu.bottom_bar_menu);

        // only show bottom bar if it was previously shown
        if(savedInstanceState == null || !savedInstanceState.getBoolean(BOTTOM_BAR_SHOWING)) {
            bottomBar.hide();
        }

        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.bbSelectAll: {
                        selectAllBarBtn();
                        break;
                    }
                    case R.id.bbClearSelection: {
                        clearSelectionBarBtn();
                        break;
                    }
                    case R.id.bbDelete: {
                        deleteBarBtn();
                        break;
                    }
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.bbSelectAll: {
                        selectAllBarBtn();
                        break;
                    }
                    case R.id.bbClearSelection: {
                        clearSelectionBarBtn();
                        break;
                    }
                    case R.id.bbDelete: {
                        deleteBarBtn();
                        break;
                    }
                }
            }
        });
    }

    @Background
    void loadItemsForPage(int page) {
        storedDestinationsAdapter.loadItemsForPage(page);
        updateUIAfterQuery();
    }

    @UiThread
    void updateUIAfterQuery() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @UiThread
    void toast(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.savedInstanceState = savedInstanceState;
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

    @Override
    public boolean hideBottomBar() {
        bottomBar.hide();
        storedDestinationsAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean showBottomBar() {
        bottomBar.show();
        return true;
    }

    private void selectAllBarBtn() {
        if(savedInstanceState != null) {
            firstTimeLoading = savedInstanceState.getBoolean(FIRST_TIME_LOADING);
        }
        if(firstTimeLoading) {
            firstTimeLoading = false;
            return;
        }
//        selectionPreference.selectionMode().put(true);
        List<Integer> ids = new ArrayList<>();
        for(Destination dst : storedDestinationsAdapter.getItems()) {
            ids.add(dst.getId());
        }
        selectionPreference.selectedDestinationIDs().put(TextUtils.join(",", ids));
        storedDestinationsAdapter.notifyDataSetChanged();
    }

    private void clearSelectionBarBtn() {
        selectionPreference.selectedDestinationIDs().remove();
        storedDestinationsAdapter.notifyDataSetChanged();
    }

    private void deleteBarBtn() {
        int[] selectedIDs = PreferenceUtil.getCommaArrayNumbers(selectionPreference.selectedDestinationIDs().getOr(""));
        // delete referenced tags first
        taggedDestinationDAOWrapper.deleteAllForDestinations(selectedIDs);
        // DAO notifies adapter of changed destinations
        destinationDAOWrapper.delete(selectedIDs);
        // clear selection
        selectionPreference.selectedDestinationIDs().remove();
        Toast.makeText(this.getContext(), getString(R.string.msg_destinations_delete), Toast.LENGTH_SHORT).show();
    }
}
