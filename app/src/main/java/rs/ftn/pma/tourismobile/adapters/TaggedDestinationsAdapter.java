package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import rs.ftn.pma.tourismobile.database.dao.wrapper.TaggedDestinationDAOWrapper;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.views.DestinationItemView;
import rs.ftn.pma.tourismobile.views.DestinationItemView_;

/**
 * Created by Daniel Kupčo on 03.07.2016.
 */
@EBean
public class TaggedDestinationsAdapter extends RecyclerViewAdapterBase<Destination, DestinationItemView> {

    private static final String TAG = TaggedDestinationsAdapter.class.getSimpleName();

    @Bean
    TaggedDestinationDAOWrapper taggedDestinationDAOWrapper;

    @RootContext
    Context context;

    @Override
    protected DestinationItemView onCreateItemView(ViewGroup parent, int viewType) {
        return DestinationItemView_.build(context);
    }

    public void loadItems(int tagID) {
        addItems(taggedDestinationDAOWrapper.findAllDestinationsForTag(tagID));
    }

}
