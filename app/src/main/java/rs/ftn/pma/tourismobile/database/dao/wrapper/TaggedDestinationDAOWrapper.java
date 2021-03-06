package rs.ftn.pma.tourismobile.database.dao.wrapper;

import android.util.Log;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.SelectArg;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.database.DatabaseHelper;
import rs.ftn.pma.tourismobile.database.dao.DestinationDAO;
import rs.ftn.pma.tourismobile.database.dao.TagDAO;
import rs.ftn.pma.tourismobile.database.dao.TaggedDestinationDAO;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.model.TaggedDestination;

/**
 * Created by Daniel Kupčo on 13.06.2016.
 */
@EBean
public class TaggedDestinationDAOWrapper {

    private static final String TAG = TaggedDestinationDAOWrapper.class.getSimpleName();

    @OrmLiteDao(helper = DatabaseHelper.class)
    TaggedDestinationDAO taggedDestinationDAO;

    @OrmLiteDao(helper = DatabaseHelper.class)
    DestinationDAO destinationDAO;

    @OrmLiteDao(helper = DatabaseHelper.class)
    TagDAO tagDAO;

    public List<TaggedDestination> findAll() {
        return refreshAll(taggedDestinationDAO.queryForAll());
    }

    public TaggedDestination findById(int id) {
        return refresh(taggedDestinationDAO.queryForId(id));
    }

    public List<TaggedDestination> findAllTaggedDestinationsForTag(int tagID) {
        try {
            List<TaggedDestination> result = taggedDestinationDAO.queryBuilder().where()
                    .eq(TaggedDestination.TAG_ID_FIELD, tagID)
                    .query();
            return refreshAll(result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TaggedDestination> findAllTaggedDestinationsForDestination(Destination destination) {
        try {
            List<TaggedDestination> result = taggedDestinationDAO.queryBuilder().where()
                    .eq(TaggedDestination.DESTINATION_ID_FIELD, destination.getId())
                    .query();
            return refreshAll(result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Tag> findAllTagsForDestination(Destination destination) {
        List<TaggedDestination> taggedDestinations = findAllTaggedDestinationsForDestination(destination);
        List<Tag> tags = new ArrayList<>();
        for(TaggedDestination td : taggedDestinations) {
            tags.add(td.getTag());
        }
        return refreshAllTags(tags);
    }

    public List<Destination> findAllDestinationsForTag(int tagID) {
        List<TaggedDestination> taggedDestinations = findAllTaggedDestinationsForTag(tagID);
        List<Destination> destinations = new ArrayList<>();
        for(TaggedDestination td : taggedDestinations) {
            destinations.add(td.getDestination());
        }
        return destinations;
    }

    public boolean create(TaggedDestination taggedDestination) {
        return taggedDestinationDAO.create(taggedDestination) == 1;
    }

    public boolean createAllTagsForDestination(Destination destination, List<Tag> tags) {
        try {
            for (Tag tag : tags) {
                TaggedDestination taggedDestination = new TaggedDestination(destination, tag);
                taggedDestinationDAO.create(taggedDestination);
            }
            return true;
        }
        catch (RuntimeException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public int deleteAllForDestination(int destinationId) {
        try {
            DeleteBuilder<TaggedDestination, Integer> deleteBuilder = taggedDestinationDAO.deleteBuilder();
            deleteBuilder.where().eq(TaggedDestination.DESTINATION_ID_FIELD, destinationId);
            return deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private PreparedDelete<TaggedDestination> preparedDeleteForTagsStatement;
    public int deleteAllForTags(int[] tagIds) {
        try {
            if (preparedDeleteForTagsStatement == null) {
                final DeleteBuilder<TaggedDestination, Integer> deleteBuilder = taggedDestinationDAO.deleteBuilder();
                final SelectArg tagId = new SelectArg();

                deleteBuilder.where().eq(TaggedDestination.TAG_ID_FIELD, tagId);
                preparedDeleteForTagsStatement = deleteBuilder.prepare();
            }

            for (int i = 0; i < tagIds.length; i++) {
                preparedDeleteForTagsStatement.setArgumentHolderValue(0, tagIds[i]);
                taggedDestinationDAO.delete(preparedDeleteForTagsStatement);
            }
            return tagIds.length;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private PreparedDelete<TaggedDestination> preparedDeleteForDestinationsStatement;
    public int deleteAllForDestinations(int[] destinationIds) {
        try {
            if (preparedDeleteForDestinationsStatement == null) {
                final DeleteBuilder<TaggedDestination, Integer> deleteBuilder = taggedDestinationDAO.deleteBuilder();
                final SelectArg destinationId = new SelectArg();

                deleteBuilder.where().eq(TaggedDestination.DESTINATION_ID_FIELD, destinationId);
                preparedDeleteForDestinationsStatement = deleteBuilder.prepare();
            }

            for (int i = 0; i < destinationIds.length; i++) {
                preparedDeleteForDestinationsStatement.setArgumentHolderValue(0, destinationIds[i]);
                taggedDestinationDAO.delete(preparedDeleteForDestinationsStatement);
            }
            return destinationIds.length;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private TaggedDestination refresh(TaggedDestination taggedDestination) {
        destinationDAO.refresh(taggedDestination.getDestination());
        tagDAO.refresh(taggedDestination.getTag());
        return taggedDestination;
    }

    private List<TaggedDestination> refreshAll(List<TaggedDestination> taggedDestinations) {
        for(TaggedDestination taggedDestination : taggedDestinations) {
            destinationDAO.refresh(taggedDestination.getDestination());
            tagDAO.refresh(taggedDestination.getTag());
        }
        return taggedDestinations;
    }

    private List<Tag> refreshAllTags(List<Tag> tags) {
        for(Tag tag : tags) {
            tagDAO.refresh(tag);
        }
        return tags;
    }

}
