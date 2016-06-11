package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.DestinationsAdapter;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;
import rs.ftn.pma.tourismobile.util.EndlessRecyclerViewScrollListener;

@EFragment(R.layout.fragment_destinations)
public class DestinationsFragment extends Fragment {

    private static final String TAG = DestinationsFragment.class.getSimpleName();

    @Bean
    DBPediaUtils dbPediaUtils;

    @Bean
    DestinationsAdapter destinationsAdapter;

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
                queryDBPedia(page);
            }
        });

        // binding adapter to the view
        destinationsList.setAdapter(destinationsAdapter);
        queryDBPedia(0); // first page
    }

    @Background
    public void queryDBPedia(int page) {
        try {
            List<Destination> destinationList = dbPediaUtils.queryDBPediaForList(page);
            queryDBPediaSuccess(destinationList);
        }
        catch (HttpClientErrorException e) {
            Log.e(TAG, e.getMessage());
            updateUIAfterQuery();
            if(e.getStatusCode().is4xxClientError()) {
                toast("Sorry! It seems that request isn't valid...");
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
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
        progressBar.setVisibility(View.INVISIBLE);
    }

    @UiThread
    void toast(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

}