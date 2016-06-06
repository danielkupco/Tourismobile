package rs.ftn.pma.tourismobile.fragments;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.TagsAdapter;
import rs.ftn.pma.tourismobile.dialogs.NewTagDialog_;


@EFragment(R.layout.fragment_tags)
public class TagsFragment extends Fragment {

    private static final String TAG = TagsFragment.class.getSimpleName();

    @ViewById
    RecyclerView tagsList;

    @Bean
    TagsAdapter tagsAdapter;

    @AfterViews
    void bindAdapter() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        tagsList.setHasFixedSize(true);

        // must set layout manager to recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tagsList.setLayoutManager(layoutManager);

        // binding adapter to the view
        tagsList.setAdapter(tagsAdapter);
    }

    @Click
    void fabAdd() {
        // creating dialog fragment in AA way
        NewTagDialog_.builder()
                // pass arguments here if any
                .build()
                .show(getFragmentManager(), TAG);
    }

}
