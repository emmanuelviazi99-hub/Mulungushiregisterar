package zm.ac.mulungushi.registrar.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import zm.ac.mulungushi.registrar.data.model.Student;

/**
 * Local (Room) copy of one student record.
 *
 * studentId is the immutable key (independent of the student number, as the brief requires).
 * number is a String so leading zeroes survive, and is unique so the phone can give early
 * duplicate feedback (the server still has the final say).
 * version is the server's record version; it is the "base version" sent with an offline edit.
 */
@Entity(tableName = "students",
        indices = {@Index(value = {"student_number"}, unique = true)})
public class StudentEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "student_id")
    public String studentId = "";

    @ColumnInfo(name = "student_number")
    public String number;

    public String name;
    public String programme;

    /** G01..G04 or "Unassigned". "group" is a reserved SQL word, so the column is lab_group. */
    @ColumnInfo(name = "lab_group")
    public String group;

    /** false = soft-deleted marker kept so a stale offline edit can never recreate the student. */
    public boolean active;

    public String email;
    public String phone;
    public String nrc;

    /** Last version confirmed by the server. */
    public int version;

    /** SAVED_LOCALLY, PENDING, SYNCING, SYNCED or ACTION_REQUIRED (see SyncState). */
    @ColumnInfo(name = "sync_state")
    public String syncState = SyncState.SYNCED;

    /** Account that owns this cached row, so data never leaks between users on one phone. */
    @ColumnInfo(name = "owner_account_id")
    public String ownerAccountId;

    public static StudentEntity fromModel(Student s, String ownerAccountId) {
        StudentEntity e = new StudentEntity();
        e.studentId = s.id;
        e.number = s.number;
        e.name = s.name;
        e.programme = s.programme;
        e.group = s.group;
        e.active = s.active;
        e.email = s.email;
        e.phone = s.phone;
        e.nrc = s.nrc;
        e.ownerAccountId = ownerAccountId;
        return e;
    }

    public Student toModel() {
        Student s = new Student(studentId, name, number, programme, group, active);
        s.email = email;
        s.phone = phone;
        s.nrc = nrc;
        return s;
    }

    /** The five labels the brief asks the UI to show. */
    public static final class SyncState {
        public static final String SAVED_LOCALLY = "SAVED_LOCALLY";
        public static final String PENDING = "PENDING";
        public static final String SYNCING = "SYNCING";
        public static final String SYNCED = "SYNCED";
        public static final String ACTION_REQUIRED = "ACTION_REQUIRED";

        private SyncState() {}
    }
}
