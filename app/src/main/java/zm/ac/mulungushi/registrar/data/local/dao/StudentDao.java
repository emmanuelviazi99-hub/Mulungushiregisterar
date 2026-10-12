package zm.ac.mulungushi.registrar.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import zm.ac.mulungushi.registrar.data.local.entity.StudentEntity;

/** Room access to cached students. Always call from a background thread, except the LiveData reads. */
@Dao
public interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(StudentEntity student);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsertAll(List<StudentEntity> students);

    @Query("SELECT * FROM students WHERE student_id = :id AND owner_account_id = :account")
    StudentEntity findById(String id, String account);

    @Query("SELECT * FROM students WHERE student_number = :number LIMIT 1")
    StudentEntity findByNumber(String number);

    /** Combined search + programme + group filter. Pass null (or "") to skip a filter; "Unassigned" is a valid group. */
    @Query("SELECT * FROM students WHERE owner_account_id = :account AND active = 1 "
            + "AND (:term IS NULL OR :term = '' OR name LIKE '%' || :term || '%' OR student_number LIKE '%' || :term || '%') "
            + "AND (:prog IS NULL OR :prog = '' OR programme = :prog) "
            + "AND (:grp IS NULL OR :grp = '' OR lab_group = :grp) "
            + "ORDER BY name LIMIT :lim OFFSET :off")
    LiveData<List<StudentEntity>> search(String account, String term, String prog, String grp, int lim, int off);

    @Query("SELECT COUNT(*) FROM students WHERE owner_account_id = :account AND active = 1 AND lab_group = :grp")
    int countInGroup(String account, String grp);

    /** Soft delete: keep the row (and so the number) but hide it. */
    @Query("UPDATE students SET active = 0, sync_state = 'PENDING' WHERE student_id = :id AND owner_account_id = :account")
    void markDeleted(String id, String account);

    @Query("DELETE FROM students WHERE owner_account_id = :account")
    void clearAccount(String account);
}
