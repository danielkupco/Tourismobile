package rs.ftn.pma.tourismobile.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.DefaultTagsAdapter;
import rs.ftn.pma.tourismobile.adapters.RecyclerViewAdapterBase;
import rs.ftn.pma.tourismobile.adapters.TagsAdapter;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TaggedDestinationDAOWrapper;
import rs.ftn.pma.tourismobile.dialogs.NewTagDialog_;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.util.PreferenceUtil;
import rs.ftn.pma.tourismobile.util.SelectionPreference_;


@EFragment(R.layout.fragment_tags)
public class TagsFragment extends BottomBarFragment {

    private static final String TAG = TagsFragment.class.getSimpleName();

    @ViewById
    RecyclerView tagsList;

    @Bean
    DefaultTagsAdapter defaultTagsAdapter;

    @Bean
    TagsAdapter tagsAdapter;

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @Bean
    TaggedDestinationDAOWrapper taggedDestinationDAOWrapper;

    @ViewById
    FloatingActionButton fabAdd;

    // displaying default tags or custom made
    @FragmentArg
    boolean defaults = false;
    public static final String DEFAULTS = "Defaults";

    @Pref
    SelectionPreference_ selectionPreference;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !defaults) {
            if(savedInstanceState.getBoolean(BOTTOM_BAR_SHOWING)) {
                bottomBar.show();
            }
            else {
                bottomBar.hide();
            }
            firstTimeLoading = savedInstanceState.getBoolean(FIRST_TIME_LOADING);
        }
    }

    @Override
    public boolean hideBottomBar() {
        Log.e(TAG, "hide bb: " + firstTimeLoading + " - " + defaults);
        bottomBar.hide();
        tagsAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean showBottomBar() {
        Log.e(TAG, "show bb: " + firstTimeLoading + " - " + defaults);
        if(!defaults) {
            bottomBar.show();
            return true;
        }
        return false;
    }

    protected void selectAllBarBtn() {
        if(savedInstanceState != null) {
            firstTimeLoading = savedInstanceState.getBoolean(FIRST_TIME_LOADING);
        }
        if(firstTimeLoading) {
            firstTimeLoading = false;
            return;
        }
//        selectionPreference.selectionMode().put(true);
        List<Integer> ids = new ArrayList<>();
        for(Tag tag : tagsAdapter.getItems()) {
            ids.add(tag.getId());
        }
        selectionPreference.selectedItemIDs().put(TextUtils.join(",", ids));
        tagsAdapter.notifyDataSetChanged();
    }

    protected void clearSelectionBarBtn() {
        selectionPreference.selectedItemIDs().remove();
        tagsAdapter.notifyDataSetChanged();
    }

    protected void deleteBarBtn() {
        int[] selectedIDs = PreferenceUtil.getCommaArrayNumbers(selectionPreference.selectedItemIDs().getOr(""));
        // delete referenced tags first
        taggedDestinationDAOWrapper.deleteAllForTags(selectedIDs);
        // DAO notifies adapter of changed destinations
        tagDAOWrapper.delete(selectedIDs);
        // clear selection
        selectionPreference.selectedItemIDs().remove();
        Toast.makeText(this.getContext(), getString(R.string.msg_tags_delete), Toast.LENGTH_SHORT).show();
    }

}
