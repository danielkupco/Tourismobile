package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.DestinationsAdapter;
import rs.ftn.pma.tourismobile.network.ServiceDBPedia;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;
import rs.ftn.pma.tourismobile.util.EndlessRecyclerViewScrollListener;
import rs.ftn.pma.tourismobile.util.SPARQLBuilder;

@EFragment(R.layout.fragment_destinations)
public class DestinationsFragment extends Fragment {

    private static final String TAG = DestinationsFragment.class.getSimpleName();

    @RestService
    ServiceDBPedia serviceDBPedia;

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
                Log.e(TAG, String.format("Page: %d , count: %d", page, totalItemsCount));
                progressBar.setVisibility(View.VISIBLE);
                queryDBPedia(page);
            }
        });

        // binding adapter to the view
        destinationsList.setAdapter(destinationsAdapter);
        queryDBPedia(0);
    }

    @Background
    public void queryDBPedia(int page) {
        final int queryLimit = 10;

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        SPARQLBuilder sparqlBuilder = new SPARQLBuilder();
        String sparql = sparqlBuilder.select()
                .from("http://dbpedia.org")
                .startWhere()
                .triplet("destination", "a", "dbo:Park")
                .property("dbp:name").as("name")
                .property("dbo:thumbnail").as("thumbnail")
                .property("rdfs:comment").as("comment")
                .property("dbo:wikiPageID").as("wikiPageID")
                .filter("lang(?comment)=\"en\"")
                .endWhere()
                .orderBy("name")
                .limit(queryLimit)
                .offset(queryLimit * page)
                .build();
        params.set("query", sparql);
        params.set("format", "json");

        try {
            Object result = serviceDBPedia.queryDBPedia(params);
            queryDBPediaSuccess(result);
        }
        catch (HttpClientErrorException errorException) {
            Log.e(TAG, errorException.getMessage());
            updateUIAfterQuery(false);
        }
    }

    @UiThread
    void queryDBPediaSuccess(Object result) {
        destinationsAdapter.addItems(DBPediaUtils.extractDestinationsForList(result));
        updateUIAfterQuery(true);
    }

    @UiThread
    void updateUIAfterQuery(boolean success) {
        progressBar.setVisibility(View.INVISIBLE);
    }
}