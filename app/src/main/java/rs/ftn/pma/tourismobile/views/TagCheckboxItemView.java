package rs.ftn.pma.tourismobile.views;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.model.Tag;

/**
 * Created by Daniel Kupčo on 12.06.2016.
 */
@EViewGroup(R.layout.view_item_tag_checkbox)
public class TagCheckboxItemView extends FrameLayout implements IViewHolder<Tag> {

    private static final String TAG = TagCheckboxItemView.class.getSimpleName();

    @ViewById
    CheckBox cbTag;

    public TagCheckboxItemView(Context context) {
        super(context);
    }

    @Override
    public void bind(Tag item) {
        cbTag.setText(item.getName());
    }

    public void setChecked(boolean checked) {
        cbTag.setChecked(checked);
    }

    public CheckBox getTagCheckbox() {
        return cbTag;
    }

}
