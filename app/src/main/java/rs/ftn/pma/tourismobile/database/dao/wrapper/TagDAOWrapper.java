package rs.ftn.pma.tourismobile.database.dao.wrapper;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.SelectArg;

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

    public int delete(int tagId) {
        try {
            DeleteBuilder<Tag, Integer> deleteBuilder = tagDAO.deleteBuilder();
            deleteBuilder.where().eq(Tag.ID_FIELD, tagId);
            setChanged();
            notifyObservers();
            return deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private PreparedDelete<Tag> preparedDeleteStatement;
    public int delete(int[] tagIds) {
        try {
            if (preparedDeleteStatement == null) {
                final DeleteBuilder<Tag, Integer> deleteBuilder = tagDAO.deleteBuilder();
                final SelectArg tagId = new SelectArg();

                deleteBuilder.where().eq(Tag.ID_FIELD, tagId);
                preparedDeleteStatement = deleteBuilder.prepare();
            }

            for (int i = 0; i < tagIds.length; i++) {
                preparedDeleteStatement.setArgumentHolderValue(0, tagIds[i]);
                tagDAO.delete(preparedDeleteStatement);
            }

            setChanged();
            notifyObservers();
            return tagIds.length;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
