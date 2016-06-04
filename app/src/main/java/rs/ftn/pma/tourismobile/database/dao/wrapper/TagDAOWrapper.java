package rs.ftn.pma.tourismobile.database.dao.wrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

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

    public List<Tag> getAll() {
        return tagDAO.queryForAll();
    }

}
