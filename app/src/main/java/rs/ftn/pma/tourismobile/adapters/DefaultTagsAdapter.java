package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.views.TagItemView;
import rs.ftn.pma.tourismobile.views.TagItemView_;

/**
 * Created by Daniel Kupčo on 26.06.2016.
 */
@EBean
public class DefaultTagsAdapter extends RecyclerViewAdapterBase<Tag, TagItemView> {

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @RootContext
    Context context;

    // It is important to initialize adapter and set data after injection
    @AfterInject
    public void initData() {
        items = new ArrayList<>(tagDAOWrapper.findAllDefaults());
    }

    @Override
    protected TagItemView onCreateItemView(ViewGroup parent, int viewType) {
        return TagItemView_.build(context);
    }

}
