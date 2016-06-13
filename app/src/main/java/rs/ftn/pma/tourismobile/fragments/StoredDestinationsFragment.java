package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.StoredDestinationsAdapter;
import rs.ftn.pma.tourismobile.util.EndlessRecyclerViewScrollListener;

/**
 * Created by Daniel Kupčo on 13.06.2016.
 */
@EFragment(R.layout.fragment_stored_destinations)
public class StoredDestinationsFragment extends Fragment {

    private static final String TAG = StoredDestinationsFragment.class.getSimpleName();

    @Bean
    StoredDestinationsAdapter storedDestinationsAdapter;

    @ViewById
    RecyclerView destinationsList;

    @ViewById
    MaterialProgressBar progressBar;

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
        destinationsList.setAdapter(storedDestinationsAdapter);
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

}
