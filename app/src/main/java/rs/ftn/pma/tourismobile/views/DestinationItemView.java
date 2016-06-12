package rs.ftn.pma.tourismobile.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.activities.DestinationDetailsActivity_;
import rs.ftn.pma.tourismobile.database.dao.wrapper.DestinationDAOWrapper;
import rs.ftn.pma.tourismobile.dialogs.SelectTagsDialog_;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;

/**
 * Created by danex on 5/12/16.
 */
@EViewGroup(R.layout.view_item_destination)
public class DestinationItemView extends CardView implements IViewHolder<Destination> {

    private static final String TAG = DestinationItemView.class.getSimpleName();

    @Bean
    DestinationDAOWrapper destinationDAOWrapper;

    @Bean
    DBPediaUtils dbPediaUtils;

    @ViewById
    SimpleDraweeView image;

    @ViewById
    TextView name;

    @ViewById
    TextView description;

    private Destination destination;

    private boolean persisted = false;

    @ViewById
    ImageView imgFavourite;

    private static final float MAX_BOUNCE = 3f;

    private static final float MIDDLE_BOUNCE = 2f;

    private static final float END_BOUNCE = 1f;

    private static final int DURATION = 800;

    public DestinationItemView(Context context) {
        super(context);
    }

    @Override
    public void bind(Destination item) {
        destination = item;
        image.setImageURI(Uri.parse(item.getImageURI()));
        name.setText(item.getName());
        description.setText(item.getComment());
        // find if destination exists locally
        Destination inDatabase = destinationDAOWrapper.findByWikiPageID(destination.getWikiPageID());
        if(inDatabase != null) {
            this.destination = inDatabase;
            persisted = true;
        }
        // must rewrite persisted value because view items are recycled
        else {
            persisted = false;
        }
        updateIcon(this.destination.isFavourite());
    }

    @Click
    void btnDetails() {
        Log.e(TAG, "details");
        Log.e(TAG, persisted + "");
        Log.e(TAG, destination.toString());
        DestinationDetailsActivity_.IntentBuilder_ intentBuilder =
                DestinationDetailsActivity_.intent(this.getContext());
        if(persisted) {
            intentBuilder.destinationID(destination.getId());
        }
        else {
            intentBuilder.wikiPageID(destination.getWikiPageID());
        }
        intentBuilder.start();
    }

    @Click(R.id.imgFavourite)
    @Background
    void toggleFavourite() {
        Log.e(TAG, "toggle");
        Log.e(TAG, persisted + "");
        Log.e(TAG, destination.toString());
        boolean favourite = destination.isFavourite();
        // if it is marked as favourite for the first time persist the destination
        if(!persisted) {
            Destination toSave = dbPediaUtils.queryDBPediaForDetails(destination.getWikiPageID());
            if(toSave == null) {
                showToast(String.format("Sorry! Destination '%s' could not be loaded from DBPedia!", destination.getName()));
                return;
            }
            toSave.setFavourite(true);
            persisted = destinationDAOWrapper.create(toSave);
            destination = toSave;
            Log.e(TAG, "saved");
            Log.e(TAG, persisted + "");
            Log.e(TAG, destination.toString());
        }
        // otherwise just update favourite state
        else {
            destination.setFavourite(!favourite);
            destinationDAOWrapper.update(destination);
        }
        updateIcon(destination.isFavourite());
        animateIcon();
    }

    @Click
    void btnSelectTags() {
        Log.e(TAG, "select tags dialog");
        Context context = getContext();
        if(context instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            SelectTagsDialog_.builder().build().show(fragmentManager, TAG);
        }
    }

    @UiThread
    void showToast(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    @UiThread
    void updateIcon(boolean favourited) {
        imgFavourite.setImageResource(favourited ? R.drawable.ic_star_white_36dp : R.drawable.ic_star_outline_white_36dp);
    }

    @UiThread
    void animateIcon() {
        ObjectAnimator.ofFloat(imgFavourite, "alpha", 0f, 1f, .8f, 1f, 0f, 1f).setDuration(DURATION).start();
        ObjectAnimator.ofFloat(imgFavourite, "scaleX", END_BOUNCE, MAX_BOUNCE, MIDDLE_BOUNCE, MAX_BOUNCE, END_BOUNCE).setDuration(DURATION).start();
        ObjectAnimator.ofFloat(imgFavourite, "scaleY", END_BOUNCE, MAX_BOUNCE, MIDDLE_BOUNCE, MAX_BOUNCE, END_BOUNCE).setDuration(DURATION).start();
        ObjectAnimator.ofFloat(imgFavourite, "translationX", 0f, 50f, 0f).setDuration(DURATION).start();
    }

}
