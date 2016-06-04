package rs.ftn.pma.tourismobile.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.activities.TagDetailsActivity_;
import rs.ftn.pma.tourismobile.model.Tag;

/**
 * Created by danex on 5/12/16.
 */
@EViewGroup(R.layout.view_item_tag)
public class TagItemView extends CardView implements IViewHolder<Tag> {

    private int tagId;

    @ViewById
    TextView name;

    @ViewById
    TextView description;

    public TagItemView(final Context context) {
        super(context);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TagDetailsActivity_.intent(context).tagId(tagId).start();
            }
        });
    }

    @Override
    public void bind(Tag tag) {
        tagId = tag.getId();
        name.setText(tag.getName());
        description.setText(tag.getDescription());
    }

}
