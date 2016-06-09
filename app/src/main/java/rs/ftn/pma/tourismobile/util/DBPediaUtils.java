package rs.ftn.pma.tourismobile.util;

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
        List<Destination> destinations = new ArrayList<>();
        JsonArray jsonArray = getResults(response);
        for(JsonElement jsonElement : jsonArray) {
            Destination destination = new Destination();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            destination.setName(getJsonValueAsString(jsonObject, Destination.NAME_FIELD));
            destination.setComment(shortenString(getJsonValueAsString(jsonObject, Destination.COMMENT_FIELD), 250));
            destination.setWikiPageID(getJsonValueAsInteger(jsonObject, Destination.WIKI_PAGE_ID_FIELD));
            destination.setImageURI(getJsonValueAsString(jsonObject, "thumbnail"));
            destinations.add(destination);
        }
        return destinations;
    }

    public static Destination extractDestinationForDetails(Object response) {
        List<Destination> destinations = new ArrayList<>();
        JsonArray jsonArray = getResults(response);
        if(jsonArray.size() == 0)
            return null;

        JsonElement jsonElement = jsonArray.get(0);
        Destination destination = new Destination();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        destination.setName(getJsonValueAsString(jsonObject, Destination.NAME_FIELD));
//        destination.setComment(shortenString(getJsonValueAsString(jsonObject, Destination.COMMENT_FIELD), 250));
        destination.setDescription(getJsonValueAsString(jsonObject, Destination.ABSTRACT_FIELD));
        destination.setWikiLink(getJsonValueAsString(jsonObject, Destination.WIKI_LINK_FIELD));
//            destination.setWikiPageID(getJsonValueAsInteger(jsonObject, Destination.WIKI_PAGE_ID_FIELD));
        destination.setImageURI(getJsonValueAsString(jsonObject, "thumbnail"));
        destination.setLatitude(getJsonValueAsDouble(jsonObject, "lat"));
        destination.setLongitude(getJsonValueAsDouble(jsonObject, "long"));
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
