package rs.ftn.pma.tourismobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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
import rs.ftn.pma.tourismobile.adapters.FavouritesAdapter;
import rs.ftn.pma.tourismobile.database.dao.wrapper.DestinationDAOWrapper;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TaggedDestinationDAOWrapper;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.util.EndlessRecyclerViewScrollListener;
import rs.ftn.pma.tourismobile.util.PreferenceUtil;
import rs.ftn.pma.tourismobile.util.SelectionPreference_;


@EFragment(R.layout.fragment_favourites)
public class FavouritesFragment extends BottomBarFragment {

    private static final String TAG = FavouritesFragment.class.getSimpleName();

    @Bean
    FavouritesAdapter favouritesAdapter;

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
        favouritesAdapter.bindAdapterToRecyclerView(destinationsList);
        loadItemsForPage(0);
    }

    @Background
    void loadItemsForPage(int page) {
        favouritesAdapter.loadItemsForPage(page);
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
        if (savedInstanceState != null && bottomBar != null) {
            if(savedInstanceState.getBoolean(BOTTOM_BAR_SHOWING)) {
                bottomBar.show();
            }
            else {
                bottomBar.hide();
            }
            firstTimeLoading = savedInstanceState.getBoolean(FIRST_TIME_LOADING);
        }
    }

    @Override
    public boolean hideBottomBar() {
        bottomBar.hide();
        favouritesAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean showBottomBar() {
        super.showBottomBar();
        bottomBar.show();
        return true;
    }

    protected void selectAllBarBtn() {
//        if(savedInstanceState != null) {
//            firstTimeLoading = savedInstanceState.getBoolean(FIRST_TIME_LOADING);
//        }
        if(firstTimeLoading) {
            firstTimeLoading = false;
            return;
        }
//        selectionPreference.selectionMode().put(true);
        List<Integer> ids = new ArrayList<>();
        for(Destination dst : favouritesAdapter.getItems()) {
            ids.add(dst.getId());
        }
        selectionPreference.selectedItemIDs().put(TextUtils.join(",", ids));
        favouritesAdapter.notifyDataSetChanged();
    }

    protected void clearSelectionBarBtn() {
        selectionPreference.selectedItemIDs().remove();
        favouritesAdapter.notifyDataSetChanged();
    }

    protected void deleteBarBtn() {
        int[] selectedIDs = PreferenceUtil.getCommaArrayNumbers(selectionPreference.selectedItemIDs().getOr(""));
        // delete referenced tags first
        taggedDestinationDAOWrapper.deleteAllForDestinations(selectedIDs);
        // DAO notifies adapter of changed destinations
        destinationDAOWrapper.delete(selectedIDs);
        // clear selection
        selectionPreference.selectedItemIDs().remove();
        Toast.makeText(this.getContext(), getString(R.string.msg_destinations_delete), Toast.LENGTH_SHORT).show();
    }

}
