package rs.ftn.pma.tourismobile.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.network.ServiceDBPedia;

/**
 * Utility class for DBPedia service and JSON data manipulation.
 * Created by Daniel Kupčo on 05.06.2016.
 */
@EBean
public class DBPediaUtils {

    private static final String TAG = DBPediaUtils.class.getSimpleName();

    @RestService
    ServiceDBPedia serviceDBPedia;

    public List<Destination> queryDBPediaForList(int page) {
        final int queryLimit = 10;

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        SPARQLBuilder sparqlBuilder = new SPARQLBuilder();
        String sparql = sparqlBuilder.select()
                .from("http://dbpedia.org")
                .startWhere()
                .triplet("destination", "a", "dbo:Park")
                .property("dbp:name").as(Destination.NAME_FIELD)
                .property("dbo:thumbnail").as(Destination.IMAGE_URI_FIELD)
                .property("rdfs:comment").as(Destination.COMMENT_FIELD)
                .property("dbo:wikiPageID").as(Destination.WIKI_PAGE_ID_FIELD)
                .filter("lang(?comment)=\"en\"")
                .endWhere()
                .orderBy(Destination.NAME_FIELD)
                .limit(queryLimit)
                .offset(queryLimit * page)
                .build();
        params.set("query", sparql);
        params.set("format", "json");

        Log.e(TAG, "sparql list");
        Log.e(TAG, sparql);

        Object result = serviceDBPedia.queryDBPedia(params);
        return extractDestinationsForList(result);
    }

    public Destination queryDBPediaForDetails(int wikiPageID) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        SPARQLBuilder sparqlBuilder = new SPARQLBuilder();
        String sparql = sparqlBuilder.select()
                .from("http://dbpedia.org")
                .startWhere()
                .triplet("destination", "a", "dbo:Park")
                .triplet("destination", "dbo:wikiPageID", String.format("\"%d\"^^xsd:integer", wikiPageID))
                .property("dbp:name").as(Destination.NAME_FIELD)
                .propertyChoice("geo:lat", "dbp:latD").as(Destination.LATITUDE_FIELD)
                .propertyChoice("geo:long", "dbp:latD").as(Destination.LONGITUDE_FIELD)
                .property("dbo:thumbnail").as(Destination.IMAGE_URI_FIELD)
                .property("foaf:isPrimaryTopicOf").as(Destination.WIKI_LINK_FIELD)
                .property("dbo:wikiPageID").as(Destination.WIKI_PAGE_ID_FIELD)
                .property("rdfs:comment").as(Destination.COMMENT_FIELD)
                .property("dbo:abstract").as(Destination.ABSTRACT_FIELD)
                .filter("lang(?comment)=\"en\" && lang(?abstract)=\"en\"")
                .endWhere()
                .orderBy(Destination.NAME_FIELD)
                .build();
        params.set("query", sparql);
        params.set("format", "json");

        Log.e(TAG, "sparql details");
        Log.e(TAG, sparql);
        Object result = serviceDBPedia.queryDBPedia(params);
        return extractDestinationForDetails(result);
    }

    public static String formatJson(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.toJson(object);
    }

    public static JsonArray getResults(Object object) {
        return new Gson().toJsonTree(object).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
    }

    public static List<Destination> extractDestinationsForList(Object response) {
        Log.e(TAG, "response list");
        Log.e(TAG, response.toString());
        List<Destination> destinations = new ArrayList<>();
        JsonArray jsonArray = getResults(response);
        for(JsonElement jsonElement : jsonArray) {
            Destination destination = new Destination();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            destination.setName(getJsonValueAsString(jsonObject, Destination.NAME_FIELD));
            destination.setComment(shortenString(getJsonValueAsString(jsonObject, Destination.COMMENT_FIELD), 250));
            destination.setWikiPageID(getJsonValueAsInteger(jsonObject, Destination.WIKI_PAGE_ID_FIELD));
            destination.setImageURI(getJsonValueAsString(jsonObject, Destination.IMAGE_URI_FIELD));
            destinations.add(destination);
            Log.e(TAG, destination.toString());
        }
        return destinations;
    }

    public static Destination extractDestinationForDetails(Object response) {
        Log.e(TAG, "response details");
        Log.e(TAG, response.toString());
        JsonArray jsonArray = getResults(response);
        if(jsonArray.size() == 0)
            return null;

        JsonElement jsonElement = jsonArray.get(0);
        Destination destination = new Destination();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        destination.setName(getJsonValueAsString(jsonObject, Destination.NAME_FIELD));
        destination.setComment(shortenString(getJsonValueAsString(jsonObject, Destination.COMMENT_FIELD), 250));
        destination.setDescription(getJsonValueAsString(jsonObject, Destination.ABSTRACT_FIELD));
        destination.setWikiLink(getJsonValueAsString(jsonObject, Destination.WIKI_LINK_FIELD));
        destination.setWikiPageID(getJsonValueAsInteger(jsonObject, Destination.WIKI_PAGE_ID_FIELD));
        destination.setImageURI(getJsonValueAsString(jsonObject, Destination.IMAGE_URI_FIELD));
        destination.setLatitude(getJsonValueAsDouble(jsonObject, Destination.LATITUDE_FIELD));
        destination.setLongitude(getJsonValueAsDouble(jsonObject, Destination.LONGITUDE_FIELD));
        Log.e(TAG, destination.toString());
        return destination;
    }

    public static JsonElement getJsonValue(JsonObject jsonObject, String fieldName) {
        return jsonObject.get(fieldName).getAsJsonObject().get("value");
    }


    public static String getJsonValueAsString(JsonObject jsonObject, String fieldName) {
        if(jsonObject.has(fieldName))
            return getJsonValue(jsonObject, fieldName).getAsString();
        return new JsonPrimitive("").getAsString();
    }

    public static double getJsonValueAsDouble(JsonObject jsonObject, String fieldName) {
        if(jsonObject.has(fieldName))
            return getJsonValue(jsonObject, fieldName).getAsDouble();
        return new JsonPrimitive(0.0).getAsDouble();
    }

    public static int getJsonValueAsInteger(JsonObject jsonObject, String fieldName) {
        if(jsonObject.has(fieldName))
            return getJsonValue(jsonObject, fieldName).getAsInt();
        return new JsonPrimitive(0).getAsInt();
    }

    public static String shortenString(String string, int limit) {
        if(string.length() > limit) {
            return String.format("%s...", string.substring(0, limit - 3));
        }
        return string;
    }

}
