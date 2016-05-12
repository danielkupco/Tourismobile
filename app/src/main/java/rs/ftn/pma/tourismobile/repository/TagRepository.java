package rs.ftn.pma.tourismobile.repository;

import android.annotation.SuppressLint;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.model.Tag;

/**
 * Created by danex on 5/12/16.
 */
@EBean
public class TagRepository {

    private List<Tag> tags;

    @SuppressLint("DefaultLocale")
    public TagRepository() {
        this.tags = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            tags.add(new Tag(String.format("Tag %d", i), String.format("Description of tag %d.", i)));
        }
    }

    public List<Tag> getTags() {
        return tags;
    }
}
