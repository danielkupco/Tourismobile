package rs.ftn.pma.tourismobile.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Model class for tags.
 * Created by danex on 5/12/16.
 */
@DatabaseTable
public class Tag {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String DBP_PROPERTY_FIELD = "dbpProperty";
    public static final String DBP_VALUE_FIELD = "dbpValue";
    public static final String DBP_CAN_QUERY_BY_FIELD = "dbpCanQueryBy";
    public static final String DEFAULT = "default";

    @DatabaseField(columnName = ID_FIELD, generatedId = true)
    private int id;

    @DatabaseField(columnName = NAME_FIELD, unique = true, canBeNull = false)
    private String name;

    @DatabaseField(columnName = DESCRIPTION_FIELD)
    private String description;

    @DatabaseField(columnName = DBP_PROPERTY_FIELD)
    private String dbpProperty;

    @DatabaseField(columnName = DBP_VALUE_FIELD)
    private String dbpValue;

    @DatabaseField(columnName = DBP_CAN_QUERY_BY_FIELD)
    private boolean dbpCanQueryBy;

    @DatabaseField(columnName = DEFAULT)
    private boolean isDefault;

    public Tag() {}


    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
        this.dbpCanQueryBy = false;
        this.isDefault = false;
    }

    public Tag(String name, String description, String dbpProperty, String dbpValue, boolean dbp_can_query_by, boolean isDefault) {
        this.name = name;
        this.description = description;
        this.dbpProperty = dbpProperty;
        this.dbpValue = dbpValue;
        this.dbpCanQueryBy = dbp_can_query_by;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
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

    public String getDbpProperty() {
        return dbpProperty;
    }

    public void setDbpProperty(String dbpProperty) {
        this.dbpProperty = dbpProperty;
    }

    public String getDbpValue() {
        return dbpValue;
    }

    public void setDbpValue(String dbpValue) {
        this.dbpValue = dbpValue;
    }

    public boolean isDbpCanQueryBy() {
        return dbpCanQueryBy;
    }

    public void setDbpCanQueryBy(boolean dbpCanQueryBy) {
        this.dbpCanQueryBy = dbpCanQueryBy;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dbpProperty='" + dbpProperty + '\'' +
                ", dbpValue='" + dbpValue + '\'' +
                ", dbpCanQueryBy=" + dbpCanQueryBy +
                ", default=" + isDefault +
                '}';
    }
}
