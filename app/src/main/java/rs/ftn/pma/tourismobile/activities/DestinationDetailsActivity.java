package rs.ftn.pma.tourismobile.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;

/**
 * Created by Daniel Kupčo on 08.06.2016.
 */
@EActivity(R.layout.activity_destination_details)
public class DestinationDetailsActivity extends AppCompatActivity {

    private static final String TAG = DestinationDetailsActivity.class.getSimpleName();

    @ViewById
    MaterialProgressBar progressBar;

    @ViewById
    SimpleDraweeView destinationImage;

    @ViewById
    TextView tvDestinationName;

    @ViewById
    TextView tvDestinationDescription;

    @ViewById
    TextView tvDestinationTags;

    @ViewById
    TextView tvWikiLink;

    @Extra
    int wikiPageID;

    @Bean
    DBPediaUtils dbPediaUtils;

    private Destination destination;

    @AfterInject
    @Background
    void loadDestination() {
        try {
            Destination destination = dbPediaUtils.queryDBPediaForDetails(wikiPageID);
            queryDBPediaSuccess(destination);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            updateUIAfterQuery();
        }
    }

    @UiThread
    void queryDBPediaSuccess(Destination destination) {
        if(destination == null) {
            tvDestinationName.setText("Destination not found!");
        }
        else {
            bind(destination);
        }
        updateUIAfterQuery();
    }

    public void bind(Destination destination) {
        this.destination = destination;
        destinationImage.setImageURI(Uri.parse(destination.getImageURI()));
        tvDestinationName.setText(destination.getName());
        tvDestinationDescription.setText(destination.getDescription());
        tvWikiLink.setText(destination.getWikiLink());
    }

    @UiThread
    void updateUIAfterQuery() {
        progressBar.setVisibility(View.INVISIBLE);
    }

}
