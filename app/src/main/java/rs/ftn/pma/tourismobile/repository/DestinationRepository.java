package rs.ftn.pma.tourismobile.repository;

import android.annotation.SuppressLint;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.model.Destination;

/**
 * Created by danex on 5/12/16.
 */
@EBean
public class DestinationRepository {

    private List<Destination> destinations;

    @SuppressLint("DefaultLocale")
    public DestinationRepository() {
        this.destinations = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Destination dst = new Destination(
                    String.format("Destination %d", i),
                    String.format("Description of destination %d", i),
                    String.format("Link to destination %d", i),
                    i % 2 == 0);
            this.destinations.add(dst);
        }
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

}
