package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.DestinationsAdapter;
import rs.ftn.pma.tourismobile.network.ServiceDBPedia;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;
import rs.ftn.pma.tourismobile.util.SPARQLBuilder;

@EFragment(R.layout.fragment_destinations)
public class DestinationsFragment extends Fragment {

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

        // binding adapter to the view
        destinationsList.setAdapter(destinationsAdapter);
        queryDBPedia();
    }

    @Background
    public void queryDBPedia() {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        SPARQLBuilder sparqlBuilder = new SPARQLBuilder();
        String sparql = sparqlBuilder.select()
                .from("http://dbpedia.org")
                .startWhere()
                .triplet("destination", "a", "dbo:Park")
                .property("dbp:name").as("name")
                .property("geo:lat").as("lat")
                .property("geo:long").as("long")
                .property("dbo:thumbnail").as("thumbnail")
                .property("foaf:isPrimaryTopicOf").as("wikiLink")
                .property("rdfs:comment").as("comment")
                .property("dbo:abstract").as("abstract")
                .filter("lang(?comment)=\"en\" && lang(?abstract)=\"en\"")
                .endWhere()
                .orderBy("name")
                .limit(10)
                .build();

        params.set("query", sparql);
        params.set("format", "json");

        Object result = serviceDBPedia.queryDBPedia(params);
        queryDBPediaSuccess(result);
    }

    @UiThread
    void queryDBPediaSuccess(Object result) {
        destinationsAdapter.setItems(DBPediaUtils.extractDestinationsFromResponse(result));
        progressBar.setVisibility(View.INVISIBLE);
    }
}