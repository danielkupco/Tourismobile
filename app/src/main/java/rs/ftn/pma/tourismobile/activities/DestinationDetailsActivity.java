package rs.ftn.pma.tourismobile.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.network.ServiceDBPedia;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;
import rs.ftn.pma.tourismobile.util.SPARQLBuilder;

/**
 * Created by Daniel Kupčo on 08.06.2016.
 */
@EActivity(R.layout.activity_destination_details)
public class DestinationDetailsActivity extends AppCompatActivity {

    private static final String TAG = DestinationDetailsActivity.class.getSimpleName();

    @ViewById
    MaterialProgressBar progressBar;

    @ViewById
    SimpleDraweeView destinationImage;

    @ViewById
    TextView tvDestinationName;

    @ViewById
    TextView tvDestinationDescription;

    @ViewById
    TextView tvDestinationTags;

    @ViewById
    TextView tvWikiLink;

    @Extra
    int wikiPageID;

    @RestService
    ServiceDBPedia serviceDBPedia;

    private Destination destination;

    @AfterInject
    @Background
    void loadDestination() {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        SPARQLBuilder sparqlBuilder = new SPARQLBuilder();
        String sparql = sparqlBuilder.select()
                .from("http://dbpedia.org")
                .startWhere()
                .triplet("destination", "a", "dbo:Park")
                .triplet("destination", "dbo:wikiPageID", String.format("\"%d\"^^xsd:integer", wikiPageID))
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
        destination = DBPediaUtils.extractDestinationForDetails(result);
        if(destination == null) {
            tvDestinationName.setText("Destination not found!");
            updateUIAfterQuery(false);
            return;
        }

        bind(destination);
        updateUIAfterQuery(true);
    }

    public void bind(Destination destination) {
        destinationImage.setImageURI(Uri.parse(destination.getImageURI()));
        tvDestinationName.setText(destination.getName());
        tvDestinationDescription.setText(destination.getDescription());
        tvWikiLink.setText(destination.getWikiLink());
    }

    @UiThread
    void updateUIAfterQuery(boolean success) {
        progressBar.setVisibility(View.INVISIBLE);
    }

}
