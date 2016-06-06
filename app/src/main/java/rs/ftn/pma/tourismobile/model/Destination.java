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
    public static final String COMMENT_FIELD = "comment";
    public static final String ABSTRACT_FIELD = "abstract";
    public static final String WIKI_LINK_FIELD = "wikiLink";
    public static final String IMAGE_URI_FIELD = "image_uri";
    public static final String LATITUDE_FIELD = "latitude";
    public static final String LONGITUDE_FIELD = "longitude";
    public static final String FAVOURITE_FIELD = "favourite";

    @DatabaseField(columnName = ID_FIELD, generatedId = true)
    private int id;

    @DatabaseField(columnName = NAME_FIELD, canBeNull = false)
    private String name;

    @DatabaseField(columnName = COMMENT_FIELD)
    private String comment;

    @DatabaseField(columnName = ABSTRACT_FIELD)
    private String description;

    @DatabaseField(columnName = WIKI_LINK_FIELD, canBeNull = false)
    private String wikiLink;

    @DatabaseField(columnName = IMAGE_URI_FIELD)
    private String imageURI;

    @DatabaseField(columnName = LATITUDE_FIELD)
    private double latitude;

    @DatabaseField(columnName = LONGITUDE_FIELD)
    private double longitude;

    @DatabaseField(columnName = FAVOURITE_FIELD, canBeNull = false)
    private boolean favourite;

    private List<Tag> tags = new ArrayList<>();

    public Destination() {}

    public Destination(String name, String comment, String description, String wikiLink, String imageURI, double latitude, double longitude, boolean favourite) {
        this.name = name;
        this.comment = comment;
        this.description = description;
        this.wikiLink = wikiLink;
        this.imageURI = imageURI;
        this.latitude = latitude;
        this.longitude = longitude;
        this.favourite = favourite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWikiLink() {
        return wikiLink;
    }

    public void setWikiLink(String wikiLink) {
        this.wikiLink = wikiLink;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return "Destination{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", description='" + description + '\'' +
                ", wikiLink='" + wikiLink + '\'' +
                ", imageURI='" + imageURI + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", favourite=" + favourite +
                ", tags=" + tags +
                '}';
    }
}
