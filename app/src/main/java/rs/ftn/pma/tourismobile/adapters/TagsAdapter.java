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

import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.views.TagItemView;
import rs.ftn.pma.tourismobile.views.TagItemView_;

/**
 * Created by danex on 5/12/16.
 */
@EBean
public class TagsAdapter extends RecyclerViewAdapterBase<Tag, TagItemView> implements Observer {

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @RootContext
    Context context;

    // It is important to initialize adapter and set data after injection
    @AfterInject
    void initData() {
        items = new ArrayList<>(tagDAOWrapper.findAll());
        hasFooter = true;
        tagDAOWrapper.addObserver(this);
    }

    @Override
    protected TagItemView onCreateItemView(ViewGroup parent, int viewType) {
        return TagItemView_.build(context);
    }

    // updating adapter if data has changed
    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof TagDAOWrapper) {
            items = new ArrayList<>(tagDAOWrapper.findAll());
        }
    }
}
