package zm.ac.mulungushi.registrar.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Per-operation results from a push, or the changes from a pull. */
public class SyncResponse {

    public static class Result {
        @SerializedName("operationId") public String operationId;
        /** OK, CONFLICT, GROUP_FULL, DUPLICATE_NUMBER, VALIDATION, FORBIDDEN or NOT_FOUND. */
        @SerializedName("status") public String status;
        /** The server's current record, so the app can show it for review on CONFLICT. */
        @SerializedName("student") public StudentDto student;
    }

    @SerializedName("results") public List<Result> results;
    /** Pull only: changed or deleted students (deleted ones have active = false). */
    @SerializedName("changes") public List<StudentDto> changes;
    /** Pull only: the newest version seen, to pass as ?since= next time. */
    @SerializedName("latestVersion") public long latestVersion;
}
