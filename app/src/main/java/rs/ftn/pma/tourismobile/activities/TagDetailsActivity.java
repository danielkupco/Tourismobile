package rs.ftn.pma.tourismobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Tag;

/**
 * Created by Daniel Kupčo on 04.06.2016.
 */
@EActivity(R.layout.activity_tag_details)
public class TagDetailsActivity extends AppCompatActivity {

    @ViewById
    TextView tagName;

    @ViewById
    TextView tagProperty;

    @ViewById
    TextView tagValue;

    @ViewById
    TextView tagDescription;

    @Bean
    TagDAOWrapper tagDAOWrapper;

    @Extra
    int tagId;

    private Tag tag;

    @AfterExtras
    void injectExtra() {
        tag = tagDAOWrapper.findById(tagId);
    }

    @AfterViews
    void bindViews() {
        tagName.setText(tag.getName());
        tagProperty.setText(tag.getDbpProperty());
        tagValue.setText(tag.getDbpValue());
        tagDescription.setText(tag.getDescription());
    }

}
