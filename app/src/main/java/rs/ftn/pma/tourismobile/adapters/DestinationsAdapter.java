package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import rs.ftn.pma.tourismobile.database.dao.wrapper.DestinationDAOWrapper;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.network.ServiceDBPedia;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;
import rs.ftn.pma.tourismobile.views.DestinationItemView;
import rs.ftn.pma.tourismobile.views.DestinationItemView_;

/**
 * Created by danex on 5/12/16.
 */
@EBean
public class DestinationsAdapter extends RecyclerViewAdapterBase<Destination, DestinationItemView> implements Observer {

    private static final String TAG = DestinationsAdapter.class.getSimpleName();

    @Bean
    DestinationDAOWrapper destinationDAOWrapper;

    @RestService
    ServiceDBPedia serviceDBPedia;

    @RootContext
    Context context;

    // It is important to set data after injection
    @AfterInject
    void initData() {
        items = new ArrayList<>(destinationDAOWrapper.findAll());
        hasFooter = true;
        destinationDAOWrapper.addObserver(this);
    }

    @Override
    protected DestinationItemView onCreateItemView(ViewGroup parent, int viewType) {
        return DestinationItemView_.build(context);
    }

    @Background
    public void queryDBPedia() {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String query = "SELECT DISTINCT * FROM <http://dbpedia.org>\n" +
                "WHERE  { \n" +
                "  ?destination a dbo:Park ;\n" +
                "    dbp:name ?name ;\n" +
                "    geo:lat ?lat ;\n" +
                "    geo:long ?long ;\n" +
                "    dbo:thumbnail ?thumbnail ;\n" +
                "    foaf:isPrimaryTopicOf ?wikiLink ;\n" +
                "    rdfs:comment ?comment ;\n" +
                "    dbo:abstract ?abstract .\n" +
                "  FILTER( lang(?comment)=\"en\" && lang(?abstract)=\"en\") .\n" +
                "}\n" +
                "ORDER BY ?name\n" +
                "LIMIT 10";
        params.set("query", query);
        params.set("format", "json");
        Object result = serviceDBPedia.queryDBPedia(params);
        queryDBPediaSuccess(result);
    }

    @UiThread
    void queryDBPediaSuccess(Object result) {
        Log.e(TAG, DBPediaUtils.formatJson(result));
        items = new ArrayList<>(DBPediaUtils.extractDestinationsFromResponse(result));
        notifyDataSetChanged();
    }

    // updating adapter if data has changed
    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof DestinationDAOWrapper) {
            items = new ArrayList<>(destinationDAOWrapper.findAll());
        }
    }
}
