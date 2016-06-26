package rs.ftn.pma.tourismobile.database.dao.wrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

import rs.ftn.pma.tourismobile.database.DatabaseHelper;
import rs.ftn.pma.tourismobile.database.dao.TagDAO;
import rs.ftn.pma.tourismobile.model.Tag;

/**
 * Created by Daniel Kupčo on 04.06.2016.
 */
@EBean(scope = EBean.Scope.Singleton)
public class TagDAOWrapper extends Observable {

    private static final String TAG = TagDAOWrapper.class.getSimpleName();

    @OrmLiteDao(helper = DatabaseHelper.class)
    TagDAO tagDAO;

    public void createTag(Tag tag) {
        tagDAO.create(tag);
        setChanged();
        notifyObservers();
    }

    public Tag findById(int id) {
        return tagDAO.queryForId(id);
    }

    public List<Tag> findAll() {
        return tagDAO.queryForAll();
    }

    public List<Tag> findAllForFilters() {
        try {
            return tagDAO.queryBuilder().where().eq(Tag.DBP_CAN_QUERY_BY_FIELD, true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Tag> findAllDefaults() {
        try {
            return tagDAO.queryBuilder().where().eq(Tag.DEFAULT, true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Tag> findAllUserDefined() {
        try {
            return tagDAO.queryBuilder().where().eq(Tag.DEFAULT, false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
