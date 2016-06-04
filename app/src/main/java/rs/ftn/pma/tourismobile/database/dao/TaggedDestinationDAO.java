package rs.ftn.pma.tourismobile.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import rs.ftn.pma.tourismobile.model.TaggedDestination;

/**
 * Created by Daniel Kupčo on 04.06.2016.
 */
public class TaggedDestinationDAO extends RuntimeExceptionDao<TaggedDestination, Integer> {
    public TaggedDestinationDAO(Dao<TaggedDestination, Integer> dao) {
        super(dao);
    }
}
