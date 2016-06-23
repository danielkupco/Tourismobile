package rs.ftn.pma.tourismobile.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.model.Destination;

/**
 * Utility class for DBPedia service and JSON data manipulation.
 * Created by Daniel Kupčo on 05.06.2016.
 */
@EBean
public class DBPediaUtils {

    private static final String TAG = DBPediaUtils.class.getSimpleName();

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
            destination.setImageURI(getJsonValueAsString(jsonObject, Destination.IMAGE_URI_FIELD));
            destinations.add(destination);
        }
        return destinations;
    }

    public static Destination extractDestinationForDetails(Object response) {
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
