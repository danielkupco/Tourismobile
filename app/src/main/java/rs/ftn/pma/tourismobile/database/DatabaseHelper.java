package rs.ftn.pma.tourismobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.model.Tag;
import rs.ftn.pma.tourismobile.model.TaggedDestination;

/**
 * Created by danex on 27/05/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "tourismobile.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Destination.class);
            TableUtils.createTable(connectionSource, Tag.class);
            TableUtils.createTable(connectionSource, TaggedDestination.class);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, TaggedDestination.class, true);
            TableUtils.dropTable(connectionSource, Tag.class, true);
            TableUtils.dropTable(connectionSource, Destination.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
