package rs.ftn.pma.tourismobile.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import rs.ftn.pma.tourismobile.model.Tag;

/**
 * Created by Daniel Kupčo on 04.06.2016.
 */
public class TagDAO extends RuntimeExceptionDao<Tag, Integer> {
    public TagDAO(Dao<Tag, Integer> dao) {
        super(dao);
    }
}
