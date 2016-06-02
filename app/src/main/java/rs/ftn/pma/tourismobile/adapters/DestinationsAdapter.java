package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.repository.DestinationRepository;
import rs.ftn.pma.tourismobile.views.DestinationItemView;
import rs.ftn.pma.tourismobile.views.DestinationItemView_;

/**
 * Created by danex on 5/12/16.
 */
@EBean
public class DestinationsAdapter extends RecyclerViewAdapterBase<Destination, DestinationItemView> {

    @Bean
    DestinationRepository repository;

    @RootContext
    Context context;

    // It is important to set data after injection
    @AfterInject
    void initData() {
        items = new ArrayList<>(repository.getDestinations());
    }

    @Override
    protected DestinationItemView onCreateItemView(ViewGroup parent, int viewType) {
        return DestinationItemView_.build(context);
    }
}
