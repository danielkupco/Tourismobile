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

        for (int i = 1; i <= 25; i++) {
            if(i > 25 / 2)
                tags.add(new Tag(String.format("Tag %d", i), String.format("Realy loooooong description of tag %d", i)));
            else
                tags.add(new Tag(String.format("Tag %d", i), String.format("Description of tag %d", i)));
        }
    }

    public List<Tag> getTags() {
        return tags;
    }
}
