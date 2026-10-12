package zm.ac.mulungushi.registrar.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import zm.ac.mulungushi.registrar.data.local.entity.PendingOperationEntity;

/** The outbox: queued changes, oldest first. */
@Dao
public interface PendingOperationDao {

    @Insert
    void insert(PendingOperationEntity op);

    @Query("SELECT * FROM pending_operations WHERE account_id = :account AND status IN ('PENDING','SYNCING') ORDER BY seq")
    List<PendingOperationEntity> pendingFor(String account);

    @Query("SELECT COUNT(*) FROM pending_operations WHERE account_id = :account AND status IN ('PENDING','SYNCING')")
    int pendingCount(String account);

    @Query("SELECT COALESCE(MAX(seq), 0) FROM pending_operations")
    long maxSeq();

    @Query("UPDATE pending_operations SET status = :status, result_json = :result WHERE operation_id = :id")
    void finish(String id, String status, String result);

    @Query("DELETE FROM pending_operations WHERE account_id = :account")
    void clearAccount(String account);
}
