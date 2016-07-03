package rs.ftn.pma.tourismobile.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.activities.MainActivity;
import rs.ftn.pma.tourismobile.activities.TagDetailsActivity_;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;
import rs.ftn.pma.tourismobile.util.PreferenceUtil;
import rs.ftn.pma.tourismobile.util.SelectionPreference_;

/**
 * Created by danex on 5/12/16.
 */
@EViewGroup(R.layout.view_item_tag)
public class TagItemView extends CardView implements IViewHolder<Tag> {

    private static String TAG = TagItemView.class.getSimpleName();

    @ViewById
    TextView name;

    @ViewById
    TextView description;

    @ViewById
    TextView footer_note;

    @ViewById
    CheckBox cbSelect;

    @Pref
    SelectionPreference_ selectionPreference;

    private Tag tag;

    public TagItemView(final Context context) {
        super(context);
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TagDetailsActivity_.intent(context)
//                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .tagId(tag.getId())
//                        .start();
//            }
//        });
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
        //noinspection WrongConstant
        cbSelect.setVisibility(PreferenceUtil.getSelectionModeVisibility(
                selectionPreference.selectionMode().getOr(false)));

        // can change checked state only if visible
        if(cbSelect.getVisibility() == VISIBLE) {
            cbSelect.setChecked(PreferenceUtil.isNumberInCommaArray(
                    selectionPreference.selectedItemIDs().getOr(""), tag.getId()));
        }
    }

    @LongClick({R.id.cardView})
    void selectMode() {
        Context context = getContext();
        if (context instanceof MainActivity && ((MainActivity) context).isSelectionAllowed()) {
            Log.e(TAG, "show bb");
            Log.e(TAG, selectionPreference.selectedItemIDs().getOr("nemaa"));
            ((MainActivity) context).showBottomBar();
            cbSelect.setVisibility(VISIBLE);
            cbSelect.setChecked(true);
            selectionPreference.selectionMode().put(true);
            selectionPreference.selectedItemIDs().put(PreferenceUtil.addNumberToCommaArray(
                    selectionPreference.selectedItemIDs().getOr(""), tag.getId()));
//            childDrawableStateChanged(cbSelect);
            // notifying RecyclerView to redraw all items when selection mode is changed
            if(getParent() instanceof RecyclerView) {
                ((RecyclerView) getParent()).getAdapter().notifyDataSetChanged();
            }
        }

    }

    @Click(R.id.cbSelect)
    void selectionChange() {
        if(cbSelect.isChecked()) {
            selectionPreference.selectedItemIDs().put(PreferenceUtil.addNumberToCommaArray(
                    selectionPreference.selectedItemIDs().getOr(""), tag.getId()));
        }
        else {
            selectionPreference.selectedItemIDs().put(PreferenceUtil.removeNumberFromCommaArray(
                    selectionPreference.selectedItemIDs().getOr(""), tag.getId()));
        }
    }

    @Click(R.id.cardView)
    void openDetails() {
        if(!selectionPreference.selectionMode().getOr(false)) {
            TagDetailsActivity_.intent(getContext())
                    .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .tagId(tag.getId())
                    .start();
        }
    }
}
