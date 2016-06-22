package rs.ftn.pma.tourismobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.activities.DestinationFilterActivity_;
import rs.ftn.pma.tourismobile.activities.MainActivity;
import rs.ftn.pma.tourismobile.adapters.DestinationsAdapter;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;
import rs.ftn.pma.tourismobile.util.EndlessRecyclerViewScrollListener;
import rs.ftn.pma.tourismobile.util.FilterPreferences_;

@EFragment(R.layout.fragment_destinations)
public class DestinationsFragment extends Fragment {

    private static final String TAG = DestinationsFragment.class.getSimpleName();

    private static final int REQUEST_FILTERS = 1;

    @Bean
    DBPediaUtils dbPediaUtils;

    @Bean
    DestinationsAdapter destinationsAdapter;

    @ViewById
    RecyclerView destinationsList;

    @ViewById
    MaterialProgressBar progressBar;

    @Pref
    FilterPreferences_ filterPreferences;

    private boolean firstTime = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_fragment_destination, menu);
    }

    @OptionsItem
    void actionFilter() {
        DestinationFilterActivity_.intent(this).startForResult(REQUEST_FILTERS);
    }

    @OnActivityResult(REQUEST_FILTERS)
    void onFiltersSuccess() {
        Log.e(TAG, filterPreferences.byName().getOr("nema"));
        Log.e(TAG, filterPreferences.byDescription().getOr("nemaa"));
        Log.e(TAG, filterPreferences.bySelectedTags().getOr("nemaaa"));
        Log.e(TAG, filterPreferences.sortBy().getOr("nemaaaa"));
    }

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
                if(!firstTime) {
                    progressBar.setVisibility(View.VISIBLE);
                    queryDBPedia(page);
                }
                else {
                    firstTime = false;
                    destinationsList.scrollToPosition(0);
                    this.movePageUp();
                }
            }
        });

        // binding adapter to the view
        destinationsAdapter.bindAdapterToRecyclerView(destinationsList);
        queryDBPedia(0); // first page
    }

    @Background
    public void queryDBPedia(int page) {
        try {
            List<Destination> destinationList = ((MainActivity)getActivity()).getDBPediaService().queryDestinationsWithFilters(page);
            queryDBPediaSuccess(destinationList);
        }
        catch (HttpClientErrorException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            Log.e(TAG, "http client");
            updateUIAfterQuery();
            if(e.getStatusCode().is4xxClientError()) {
                toast("Sorry! It seems that request isn't valid...");
            }
        }
        catch (ResourceAccessException e) {
            Log.e(TAG, e.getMessage());
            toast("Sorry! DBPedia service is unavailable at the moment!\nPlease try again later...");
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Uncaught exception!");
        }
        finally {
            updateUIAfterQuery();
        }
    }

    @UiThread
    void queryDBPediaSuccess(List<Destination> destinationList) {
        destinationsAdapter.addItems(destinationList);
        updateUIAfterQuery();
    }

    @UiThread
    void updateUIAfterQuery() {
        // it happens sometimes that view is not injected yet when method is called
        if(progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @UiThread
    void toast(String message) {
        if(this.getContext() != null) {
            Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

}