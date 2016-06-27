package rs.ftn.pma.tourismobile.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.network.RestDBPedia;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;
import rs.ftn.pma.tourismobile.util.FilterPreferences_;
import rs.ftn.pma.tourismobile.util.SPARQLBuilder;

/**
 * Created by Daniel Kupčo on 18.06.2016.
 */
@EService
public class DBPediaService extends Service {

    private static final String TAG = DBPediaService.class.getSimpleName();

    @RestService
    RestDBPedia restDBPedia;

    @Pref
    FilterPreferences_ filterPreferences;

    @Bean
    DBPediaUtils dbPediaUtils;

    @Bean
    TagDAOWrapper tagDAOWrapper;

    // Binder given to clients
    private final IBinder binder = new ServiceBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class ServiceBinder extends Binder {
        public DBPediaService getService() {
            // Return this instance of the service so clients can call public methods
            return DBPediaService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public List<Destination> queryDestinationsWithFilters(int page) {
        final int queryLimit = 10;
        final String VARIABLE = "Destination";

        final boolean tagFiltersSelected = filterPreferences.bySelectedTags().exists();
        final String[] tagPositions = filterPreferences.bySelectedTags().getOr("").split(",");
        final String[] predicates = new String[tagPositions.length];
        final String[] objects = new String[tagPositions.length];

        // get filter attributes for selected tags
        List<Tag> filterTags = tagDAOWrapper.findAllForFilters();
        if(tagFiltersSelected && filterTags.size() >= tagPositions.length) {
            for (int i = 0; i < tagPositions.length; i++) {
                Tag tag = filterTags.get(Integer.valueOf(tagPositions[i]));
                predicates[i] = tag.getDbpProperty();
                objects[i] = tag.getDbpValue();
                Log.e(TAG, tag.toString());
            }
        }

        // build SPARQL query
        SPARQLBuilder sparqlBuilder = new SPARQLBuilder();
        String sparql = sparqlBuilder.startQuery()
                .select()
                .var(VARIABLE).var(Destination.NAME_FIELD).var(Destination.WIKI_PAGE_ID_FIELD)
                .var(Destination.IMAGE_URI_FIELD).var(Destination.COMMENT_FIELD)
                .aggregateVarAs("AVG", Destination.LATITUDE_FIELD, Destination.LATITUDE_FIELD)
                .aggregateVarAs("AVG", Destination.LONGITUDE_FIELD, Destination.LONGITUDE_FIELD)
                .from("http://dbpedia.org")
                .startWhere()
                    .startSubquery()
                        .select()
                        .variables() // all
                        .startWhere()
//                            .triplet(VARIABLE, "a", "dbo:City", false).andIf(tagFiltersSelected)
                            .triplets(VARIABLE, predicates, objects) // arrays of parameters
                            .property("rdfs:label").as(Destination.NAME_FIELD)
                            .property("rdfs:comment").as(Destination.COMMENT_FIELD)
                            .startFilter()
                                .varFunction("lang", Destination.COMMENT_FIELD).eqAsString("en").and()
                                .varFunction("lang", Destination.NAME_FIELD).eqAsString("en").andIf(filterPreferences.byName().exists())
                                .functionWithParams("CONTAINS", String.format("LCASE(?%s)", Destination.NAME_FIELD),
                                        String.format("LCASE(\"%s\")", filterPreferences.byName().get())).andIf(filterPreferences.byDescription().exists())
                                .functionWithParams("CONTAINS", String.format("LCASE(?%s)", Destination.COMMENT_FIELD),
                                        String.format("LCASE(\"%s\")", filterPreferences.byDescription().get()))
                            .endFilter()
                        .endWhere()
                    .endSubquery()
                    // must continue with triplet
                    .triplet(VARIABLE, "dbo:wikiPageID", Destination.WIKI_PAGE_ID_FIELD, true)
                    .property("dbo:thumbnail").as(Destination.IMAGE_URI_FIELD)
                    .propertyChoice("geo:lat", "dbp:latD").as(Destination.LATITUDE_FIELD)
                    .propertyChoice("geo:long", "dbp:longD").as(Destination.LONGITUDE_FIELD)
                .endWhere()
                .groupBy(VARIABLE, Destination.NAME_FIELD, Destination.WIKI_PAGE_ID_FIELD,
                        Destination.IMAGE_URI_FIELD, Destination.COMMENT_FIELD)
                // order by name and use ascending order by default
                .orderByWithDirection(filterPreferences.sortBy().getOr(Destination.NAME_FIELD),
                        filterPreferences.sortOrder().getOr(true) ? "ASC" : "DESC")
                .limit(queryLimit)
                .offset(queryLimit * page)
                .build();

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("query", sparql);
        params.set("format", "json");

        // this is to avoid connection reset error from server:
//         recvfrom failed: ECONNRESET (Connection reset by peer)
//        System.setProperty("http.keepAlive", "false");

        Log.e(TAG, "sparql list");
        Log.e(TAG, sparql);
        Log.e(TAG, sparqlBuilder.prettify());

        Object result = restDBPedia.queryDBPedia(params);
        return dbPediaUtils.extractDestinationsForList(result);
    }

    public Destination queryDestinationDetails(int wikiPageID) {
        final String VARIABLE = "Destination";

        SPARQLBuilder sparqlBuilder = new SPARQLBuilder();
        String sparql = sparqlBuilder.startQuery()
                .select()
                .var(VARIABLE).var(Destination.NAME_FIELD).var(Destination.IMAGE_URI_FIELD)
                .var(Destination.WIKI_PAGE_ID_FIELD).var(Destination.WIKI_LINK_FIELD)
                .var(Destination.COMMENT_FIELD).var(Destination.ABSTRACT_FIELD)
                .aggregateVarAs("AVG", Destination.LATITUDE_FIELD, Destination.LATITUDE_FIELD)
                .aggregateVarAs("AVG", Destination.LONGITUDE_FIELD, Destination.LONGITUDE_FIELD)
                .from("http://dbpedia.org")
                .startWhere()
                    .startSubquery()
                        .select()
                        .variables() // all
                        .startWhere()
                            .triplet(VARIABLE, "a", "dbo:City", false)
                            .triplet(VARIABLE, "dbo:wikiPageID", String.format("\"%d\"^^xsd:integer", wikiPageID))
                            .property("rdfs:label").as(Destination.NAME_FIELD)
                            .property("dbo:wikiPageID").as(Destination.WIKI_PAGE_ID_FIELD)
                            .property("foaf:isPrimaryTopicOf").as(Destination.WIKI_LINK_FIELD)
                            .property("rdfs:comment").as(Destination.COMMENT_FIELD)
                            .property("dbo:abstract").as(Destination.ABSTRACT_FIELD)
                            .startFilter()
                                .varFunction("lang", Destination.ABSTRACT_FIELD).eqAsString("en").and()
                                .varFunction("lang", Destination.COMMENT_FIELD).eqAsString("en").and()
                                .varFunction("lang", Destination.NAME_FIELD).eqAsString("en")
                            .endFilter()
                        .endWhere()
                    .endSubquery()
                    // must continue with triplet
                    .triplet(VARIABLE, "dbo:thumbnail", Destination.IMAGE_URI_FIELD, true)
                    .propertyChoice("geo:lat", "dbp:latD").as(Destination.LATITUDE_FIELD)
                    .propertyChoice("geo:long", "dbp:longD").as(Destination.LONGITUDE_FIELD)
                .endWhere()
                .groupBy(VARIABLE, Destination.NAME_FIELD, Destination.IMAGE_URI_FIELD,
                        Destination.WIKI_PAGE_ID_FIELD, Destination.WIKI_LINK_FIELD,
                        Destination.COMMENT_FIELD, Destination.ABSTRACT_FIELD)
                .orderBy(Destination.NAME_FIELD)
                .limit(1)
                .build();

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("query", sparql);
        params.set("format", "json");

        Log.e(TAG, "sparql details");
        Log.e(TAG, sparql);
        Log.e(TAG, sparqlBuilder.prettify());
        Object result = restDBPedia.queryDBPedia(params);
        return dbPediaUtils.extractDestinationForDetails(result);
    }

}
