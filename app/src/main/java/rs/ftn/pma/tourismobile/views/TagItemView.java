package rs.ftn.pma.tourismobile.views;

import android.content.Context;
import android.content.Intent;
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

    @ViewById
    TextView name;

    @ViewById
    TextView description;

    private Tag tag;

    public TagItemView(final Context context) {
        super(context);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TagDetailsActivity_.intent(context)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .tagId(tag.getId())
                        .start();
            }
        });
    }

    @Override
    public void bind(Tag item) {
        tag = item;
        name.setText(item.getName());
        description.setText(item.getDescription());
    }
}
