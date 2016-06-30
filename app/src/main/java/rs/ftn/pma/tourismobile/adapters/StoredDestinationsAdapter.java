package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Observable;
import java.util.Observer;

import rs.ftn.pma.tourismobile.database.dao.wrapper.DestinationDAOWrapper;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.views.DestinationItemView;
import rs.ftn.pma.tourismobile.views.DestinationItemView_;

/**
 * Created by Daniel Kupčo on 13.06.2016.
 */
@EBean
public class StoredDestinationsAdapter extends RecyclerViewAdapterBase<Destination, DestinationItemView> implements Observer {

    private static final String TAG = StoredDestinationsAdapter.class.getSimpleName();

    private int pageLimit = 10;

    @Bean
    DestinationDAOWrapper destinationDAOWrapper;

    @RootContext
    Context context;

    // We do not set data after injection because we make query from fragment
    @AfterInject
    void initData() {
        hasFooter = true;
        destinationDAOWrapper.addObserver(this);
    }

    @Override
    protected DestinationItemView onCreateItemView(ViewGroup parent, int viewType) {
        return DestinationItemView_.build(context);
    }

    // updating adapter if data has changed
    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof DestinationDAOWrapper) {
            setItems(destinationDAOWrapper.findAllForPage(0, pageLimit));
        }
    }

    public void loadItemsForPage(int page) {
        addItems(destinationDAOWrapper.findAllForPage(page, pageLimit));
    }

}
