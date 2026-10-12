package zm.ac.mulungushi.registrar.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * One queued change waiting to be sent to the server (the outbox).
 *
 * The operationId is created once and reused on every retry, so the server can
 * recognise a repeat and answer from its stored receipt instead of applying it twice.
 * seq keeps the order the user made the changes in.
 * The queue is scoped by accountId so one user's changes are never replayed under another.
 */
@Entity(tableName = "pending_operations",
        indices = {@Index("account_id"), @Index("student_id")})
public class PendingOperationEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "operation_id")
    public String operationId = "";

    /** Order of creation; replayed lowest first. */
    public long seq;

    @ColumnInfo(name = "account_id")
    public String accountId;

    @ColumnInfo(name = "student_id")
    public String studentId;

    /** CREATE, UPDATE, DELETE or GROUP_CHANGE. */
    public String type;

    /** JSON body of the change. */
    public String payload;

    /** Server version the edit was based on; the server compares it before accepting. */
    @ColumnInfo(name = "base_version")
    public int baseVersion;

    /** PENDING, SYNCING, DONE, CONFLICT or REJECTED. */
    public String status = "PENDING";

    /** Server answer stored with the change, so a lost response cannot cause a duplicate effect. */
    @ColumnInfo(name = "result_json")
    public String resultJson;

    @ColumnInfo(name = "created_at")
    public long createdAt;
}
