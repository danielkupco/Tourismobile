package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.repository.TagRepository;
import rs.ftn.pma.tourismobile.views.TagItemView;
import rs.ftn.pma.tourismobile.views.TagItemView_;

/**
 * Created by danex on 5/12/16.
 */
@EBean
public class TagsAdapter extends RecyclerViewAdapterBase<Tag, TagItemView> {

    @Bean
    TagRepository tagRepository;

    @RootContext
    Context context;

    // It is important to initialize adapter and set data after injection
    @AfterInject
    void initData() {
        items = new ArrayList<>(tagRepository.getTags());
        hasFooter = true;
    }

    @Override
    protected TagItemView onCreateItemView(ViewGroup parent, int viewType) {
        return TagItemView_.build(context);
    }

    public boolean addTag(Tag tag) {
        tagRepository.getTags().add(tag);
        this.notifyItemInserted(tagRepository.getTags().size() - 1);
        return true;
    }

}
