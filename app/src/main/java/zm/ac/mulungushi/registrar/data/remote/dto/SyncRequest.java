package zm.ac.mulungushi.registrar.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** The queued operations, oldest first. */
public class SyncRequest {

    public static class Operation {
        @SerializedName("operationId") public String operationId;
        @SerializedName("studentId") public String studentId;
        /** CREATE, UPDATE, DELETE or GROUP_CHANGE. */
        @SerializedName("type") public String type;
        /** The change itself, as a JSON string. */
        @SerializedName("payload") public String payload;
        @SerializedName("baseVersion") public int baseVersion;
    }

    @SerializedName("operations") public List<Operation> operations;
}
