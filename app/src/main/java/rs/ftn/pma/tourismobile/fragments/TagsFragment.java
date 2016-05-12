package rs.ftn.pma.tourismobile.fragments;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.TagsAdapter;


@EFragment(R.layout.fragment_tags)
public class TagsFragment extends Fragment {

    @ViewById
    RecyclerView tagList;

    @Bean
    TagsAdapter tagsAdapter;

    @AfterViews
    void bindAdapter() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        tagList.setHasFixedSize(true);

        // must set layout manager to recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tagList.setLayoutManager(layoutManager);

        // binding adapter to the view
        tagList.setAdapter(tagsAdapter);
    }

//    @ItemClick
//    void tagListItemClicked(Tag tag) {
//    }

}
