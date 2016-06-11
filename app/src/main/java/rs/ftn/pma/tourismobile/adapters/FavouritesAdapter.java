package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
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
 * Created by Daniel Kupčo on 11.06.2016.
 */
@EBean
public class FavouritesAdapter extends RecyclerViewAdapterBase<Destination, DestinationItemView> implements Observer {

    private static final String TAG = FavouritesAdapter.class.getSimpleName();

    private int pageLimit = 3;

    @Bean
    DestinationDAOWrapper destinationDAOWrapper;

    @RootContext
    Context context;

    // It is important to set data after injection
    @AfterInject
    void initData() {
        setItems(destinationDAOWrapper.findAllFavouritesForPage(0, pageLimit));
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
            setItems(destinationDAOWrapper.findAllFavouritesForPage(0, pageLimit));
        }
    }

    public void loadItemsForPage(int page) {
        addItems(destinationDAOWrapper.findAllFavouritesForPage(page, pageLimit));
    }

}
