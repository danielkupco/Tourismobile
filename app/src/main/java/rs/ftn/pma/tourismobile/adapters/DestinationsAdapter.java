package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import rs.ftn.pma.tourismobile.database.dao.wrapper.DestinationDAOWrapper;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.views.DestinationItemView;
import rs.ftn.pma.tourismobile.views.DestinationItemView_;

/**
 * Created by danex on 5/12/16.
 */
@EBean
public class DestinationsAdapter extends RecyclerViewAdapterBase<Destination, DestinationItemView> implements Observer {

    private static final String TAG = DestinationsAdapter.class.getSimpleName();

    @Bean
    DestinationDAOWrapper destinationDAOWrapper;

    @RootContext
    Context context;

    // It is important to set data after injection
    @AfterInject
    void initData() {
//        setItems(destinationDAOWrapper.findAll());
        setItems(new ArrayList<Destination>());
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
//        if(observable instanceof DestinationDAOWrapper) {
//            setItems(destinationDAOWrapper.findAll());
//        }
    }
}
