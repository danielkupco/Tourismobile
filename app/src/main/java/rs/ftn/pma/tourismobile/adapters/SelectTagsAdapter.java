package rs.ftn.pma.tourismobile.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.views.TagCheckboxItemView;
import rs.ftn.pma.tourismobile.views.TagCheckboxItemView_;

/**
 * Created by Daniel Kupčo on 12.06.2016.
 */
@EBean
public class SelectTagsAdapter extends BaseAdapter {

    private static final String TAG = SelectTagsAdapter.class.getSimpleName();

    private List<Tag> tags;

    private List<Integer> selectedIndices;

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @RootContext
    protected Context context;

    @AfterInject
    void initItems() {
        this.tags = new ArrayList<>(tagDAOWrapper.findAll());
        selectedIndices = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tags.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TagCheckboxItemView tagCheckboxItemView;
        if(convertView == null) {
            tagCheckboxItemView = TagCheckboxItemView_.build(context);
        }
        else {
            tagCheckboxItemView = (TagCheckboxItemView) convertView;
        }
        tagCheckboxItemView.bind((Tag) getItem(position));
        tagCheckboxItemView.setChecked(selectedIndices.contains(position));

        // must bound on View, not on ViewGroup
        tagCheckboxItemView.getTagCheckbox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if(cb.isChecked()) {
                    selectedIndices.add(position);
                }
                else {
                    selectedIndices.remove(Integer.valueOf(position));
                }
            }
        });

        return tagCheckboxItemView;
    }

    public List<Tag> getSelectedTags() {
        List<Tag> selectedTags = new ArrayList<>();
        for(Integer position : selectedIndices) {
            selectedTags.add(tags.get(position));
        }
        return selectedTags;
    }

}
