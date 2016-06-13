package rs.ftn.pma.tourismobile.database.dao.wrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

import rs.ftn.pma.tourismobile.database.DatabaseHelper;
import rs.ftn.pma.tourismobile.database.dao.DestinationDAO;
import rs.ftn.pma.tourismobile.model.Destination;

/**
 * Created by Daniel Kupčo on 04.06.2016.
 */
@EBean(scope = EBean.Scope.Singleton)
public class DestinationDAOWrapper extends Observable {

    @OrmLiteDao(helper = DatabaseHelper.class)
    DestinationDAO destinationDAO;

    public List<Destination> findAll() {
        return destinationDAO.queryForAll();
    }

    public Destination findById(int id) {
        return destinationDAO.queryForId(id);
    }

    public boolean create(Destination destination) {
        return destinationDAO.create(destination) == 1;
    }

    public void update(Destination destination) {
        destinationDAO.update(destination);
    }

    public void delete(Destination destination) {
        destinationDAO.delete(destination);
    }

    public Destination findByWikiPageID(int wikiPageID) {
        try {
            return destinationDAO.queryBuilder().where().eq(Destination.WIKI_PAGE_ID_FIELD, wikiPageID).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Destination> findAllFavourites() {
        try {
            return destinationDAO.queryBuilder().where().eq(Destination.FAVOURITE_FIELD, true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Destination> findAllForPage(int page, int limit) {
        try {
            return destinationDAO.queryBuilder().offset(Long.valueOf(page * limit)).limit(Long.valueOf(limit)).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Destination> findAllFavouritesForPage(int page, int limit) {
        try {
            return destinationDAO.queryBuilder().offset(Long.valueOf(page * limit)).limit(Long.valueOf(limit))
                    .where().eq(Destination.FAVOURITE_FIELD, true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
