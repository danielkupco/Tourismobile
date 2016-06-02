package rs.ftn.pma.tourismobile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danex on 5/11/16.
 */
public class Destination {

    private String name;
    private String decription;
    private String link;
    private String imageURI;
    private boolean favourite;
    private List<Tag> tags = new ArrayList<Tag>();

    public Destination() {}

    public Destination(String name, String decription, String link, String imageURI, boolean favourite) {
        this.name = name;
        this.decription = decription;
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

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
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
