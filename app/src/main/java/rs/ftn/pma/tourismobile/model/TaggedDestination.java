package rs.ftn.pma.tourismobile.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * This class models many to many relationship between destinations and tags.
 * Created by danielkupco on 6/4/16.
 */
@DatabaseTable
public class TaggedDestination {

    public static final String ID_FIELD = "id";
    public static final String DESTINATION_FIELD = "destination";
    public static final String TAG_FIELD = "tag";

    @DatabaseField(columnName = ID_FIELD, generatedId = true)
    private int id;

    @DatabaseField(columnName = DESTINATION_FIELD, foreign = true)
    private Destination destination;

    @DatabaseField(columnName = TAG_FIELD, foreign = true)
    private Tag tag;

    public TaggedDestination() {}

    public TaggedDestination(int id, Destination destination, Tag tag) {
        this.id = id;
        this.destination = destination;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
