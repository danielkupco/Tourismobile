package rs.ftn.pma.tourismobile.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.DefaultTagsAdapter;
import rs.ftn.pma.tourismobile.adapters.RecyclerViewAdapterBase;
import rs.ftn.pma.tourismobile.adapters.TagsAdapter;
import rs.ftn.pma.tourismobile.dialogs.NewTagDialog_;


@EFragment(R.layout.fragment_tags)
public class TagsFragment extends Fragment {

    private static final String TAG = TagsFragment.class.getSimpleName();

    @ViewById
    RecyclerView tagsList;

    @Bean
    DefaultTagsAdapter defaultTagsAdapter;

    @Bean
    TagsAdapter tagsAdapter;

    @ViewById
    FloatingActionButton fabAdd;

    @FragmentArg
    boolean defaults = false;

    public static final String DEFAULTS = "Defaults";

    @AfterViews
    void bindAdapter() {
        if(tagsList != null) {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            tagsList.setHasFixedSize(true);

            // must set layout manager to recycler view
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            tagsList.setLayoutManager(layoutManager);

            RecyclerViewAdapterBase adapter;
            if (defaults) {
                adapter = defaultTagsAdapter;
            } else {
                adapter = tagsAdapter;
            }
            // binding adapter to the view
            adapter.bindAdapterToRecyclerView(tagsList);
        }
    }

    @Click
    void fabAdd() {
        // creating dialog fragment in AA way
        NewTagDialog_.builder()
                // pass arguments here if any
                .build()
                .show(getFragmentManager(), TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tags, container, false);
        tagsList = (RecyclerView) rootView.findViewById(R.id.tagsList);
        fabAdd = (FloatingActionButton) rootView.findViewById(R.id.fabAdd);
        Bundle args = getArguments();
        if (args != null) {
            defaults = args.getBoolean(DEFAULTS);
            if(defaults) {
                fabAdd.setVisibility(View.INVISIBLE);
            }
        }
        bindAdapter();
        return rootView;
    }

}
