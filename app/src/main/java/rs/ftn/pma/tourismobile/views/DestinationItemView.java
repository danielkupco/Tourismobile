package rs.ftn.pma.tourismobile.views;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.activities.DestinationDetailsActivity_;
import rs.ftn.pma.tourismobile.model.Destination;

/**
 * Created by danex on 5/12/16.
 */
@EViewGroup(R.layout.view_item_destination)
public class DestinationItemView extends CardView implements IViewHolder<Destination> {

    private static final String TAG = DestinationItemView.class.getSimpleName();

    @ViewById
    SimpleDraweeView image;

    @ViewById
    TextView name;

    @ViewById
    TextView description;

    private Destination destination;

    public DestinationItemView(Context context) {
        super(context);
    }

    @Override
    public void bind(Destination item) {
        destination = item;
        image.setImageURI(Uri.parse(item.getImageURI()));
        name.setText(item.getName());
        description.setText(item.getComment());
    }

    @Click
    void btnDetails() {
        DestinationDetailsActivity_.intent(this.getContext())
//                .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .wikiPageID(destination.getWikiPageID())
                .start();
    }

}
