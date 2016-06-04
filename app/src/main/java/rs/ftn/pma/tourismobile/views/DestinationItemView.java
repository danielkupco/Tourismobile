package rs.ftn.pma.tourismobile.views;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.model.Destination;

/**
 * Created by danex on 5/12/16.
 */
@EViewGroup(R.layout.view_item_destination)
public class DestinationItemView extends CardView implements IViewHolder<Destination> {

    @ViewById
    SimpleDraweeView image;

    @ViewById
    TextView name;

    @ViewById
    TextView description;

    public DestinationItemView(Context context) {
        super(context);
    }

    @Override
    public void bind(Destination item) {
        image.setImageURI(Uri.parse(item.getImageURI()));
        name.setText(item.getName());
        description.setText(item.getDescription());
    }

}
