package rs.ftn.pma.tourismobile.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.activities.TagDetailsActivity_;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;

/**
 * Created by danex on 5/12/16.
 */
@EViewGroup(R.layout.view_item_tag)
public class TagItemView extends CardView implements IViewHolder<Tag> {

    @ViewById
    TextView name;

    @ViewById
    TextView description;

    @ViewById
    TextView footer_note;

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
        description.setText(DBPediaUtils.shortenString(item.getDescription(), 100));
        if(!item.isDbpCanQueryBy()) {
            footer_note.setText(getContext().getString(R.string.label_users_tag));
            footer_note.setGravity(Gravity.START | Gravity.LEFT);
        }
        else {
            footer_note.setText(getContext().getString(R.string.label_dbpedia_tag));
            footer_note.setGravity(Gravity.END | Gravity.RIGHT);
        }
    }
}
