package zm.ac.mulungushi.registrar.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import zm.ac.mulungushi.registrar.data.local.dao.PendingOperationDao;
import zm.ac.mulungushi.registrar.data.local.dao.StudentDao;
import zm.ac.mulungushi.registrar.data.local.entity.PendingOperationEntity;
import zm.ac.mulungushi.registrar.data.local.entity.StudentEntity;

/**
 * The phone's durable store (Room / SQLite). A student's edit and the pending operation that
 * describes it must be written together inside runInTransaction(...), so one never exists without the other.
 *
 * When the schema changes, raise the version and add a Migration with addMigrations(...)
 * instead of fallbackToDestructiveMigration(), so saved and pending work is not deleted.
 */
@Database(entities = {StudentEntity.class, PendingOperationEntity.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract StudentDao studentDao();

    public abstract PendingOperationDao pendingOperationDao();

    private static volatile AppDatabase instance;

    public static AppDatabase get(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(), AppDatabase.class, "registrar.db").build();
                }
            }
        }
        return instance;
    }
}
