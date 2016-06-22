package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.views.DestinationItemView;
import rs.ftn.pma.tourismobile.views.DestinationItemView_;

/**
 * Created by danex on 5/12/16.
 */
@EBean
public class DestinationsAdapter extends RecyclerViewAdapterBase<Destination, DestinationItemView> {

    private static final String TAG = DestinationsAdapter.class.getSimpleName();

    @RootContext
    Context context;

    @AfterInject
    void initData() {
        hasFooter = true;
    }

    @Override
    protected DestinationItemView onCreateItemView(ViewGroup parent, int viewType) {
        return DestinationItemView_.build(context);
    }

}
