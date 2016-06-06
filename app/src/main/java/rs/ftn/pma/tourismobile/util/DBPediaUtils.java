package rs.ftn.pma.tourismobile.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.model.Destination;

/**
 * Utility class for DBPedia service and JSON data manipulation.
 * Created by Daniel Kupčo on 05.06.2016.
 */
public class DBPediaUtils {

    private static final String TAG = DBPediaUtils.class.getSimpleName();

    public static String formatJson(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        return gson.toJson(object);
    }

    public static JsonArray getResults(Object object) {
        return new Gson().toJsonTree(object).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
    }

    public static List<Destination> extractDestinationsFromResponse(Object response) {
        Gson gson = new Gson();
        List<Destination> destinations = new ArrayList<>();
        JsonArray jsonArray = gson.toJsonTree(response).getAsJsonObject().getAsJsonObject("results").getAsJsonArray("bindings");
        for(JsonElement jsonElement : jsonArray) {
            Destination destination = new Destination();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Log.e(TAG, jsonObject.toString());
            destination.setName(getJsonValueAsString(jsonObject, Destination.NAME_FIELD));
            destination.setComment(getJsonValueAsString(jsonObject, Destination.COMMENT_FIELD));
            destination.setDescription(getJsonValueAsString(jsonObject, Destination.ABSTRACT_FIELD));
            destination.setWikiLink(getJsonValueAsString(jsonObject, Destination.WIKI_LINK_FIELD));
            destination.setImageURI(getJsonValueAsString(jsonObject, "thumbnail"));
            destination.setLatitude(getJsonValueAsDouble(jsonObject, "lat"));
            destination.setLongitude(getJsonValueAsDouble(jsonObject, "long"));
            destinations.add(destination);
            Log.e(TAG, "extracted destination");
            Log.v(TAG, destination.toString());
        }
        return destinations;
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

}
