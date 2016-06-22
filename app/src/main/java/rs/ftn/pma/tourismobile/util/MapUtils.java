package rs.ftn.pma.tourismobile.util;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.model.Destination;

/**
 * Created by Daniel Kupčo on 22.06.2016.
 */
public class MapUtils {

    private static boolean updated = false;

    private static List<Destination> destinations = new ArrayList<>();

    public static boolean isUpdated() {
        return updated;
    }

    public static List<Destination> getDestinations() {
        updated = false;
        return destinations;
    }

    public static void setDestinations(Destination destination) {
        updated = true;
        destinations.clear();
        destinations.add(destination);
    }

    public static void setDestinations(List<Destination> destinations) {
        updated = true;
        destinations = new ArrayList<>(destinations);
    }

}
