package rs.ftn.pma.tourismobile.model;

/**
 * Created by danex on 5/12/16.
 */
public class Tag {

    private String name;
    private String description;

    public Tag() {}

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
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
}
