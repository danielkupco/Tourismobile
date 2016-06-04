package rs.ftn.pma.tourismobile.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for destinations.
 * Created by danex on 5/11/16.
 */
@DatabaseTable
public class Destination {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String LINK_FIELD = "link";
    public static final String IMAGE_URI_FIELD = "image_uri";
    public static final String FAVOURITE_FIELD = "favourite";

    @DatabaseField(columnName = ID_FIELD, generatedId = true)
    private int id;

    @DatabaseField(columnName = NAME_FIELD, canBeNull = false)
    private String name;

    @DatabaseField(columnName = DESCRIPTION_FIELD, canBeNull = false)
    private String description;

    @DatabaseField(columnName = LINK_FIELD, canBeNull = false)
    private String link;

    @DatabaseField(columnName = IMAGE_URI_FIELD)
    private String imageURI;

    @DatabaseField(columnName = FAVOURITE_FIELD, canBeNull = false)
    private boolean favourite;

    private List<Tag> tags = new ArrayList<>();

    public Destination() {}

    public Destination(String name, String description, String link, String imageURI, boolean favourite) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.imageURI = imageURI;
        this.favourite = favourite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean addTag(Tag tag) {
        return tags.contains(tag) ? false : tags.add(tag);
    }

}
