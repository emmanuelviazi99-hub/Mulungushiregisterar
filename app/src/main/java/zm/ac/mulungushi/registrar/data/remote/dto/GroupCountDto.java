package zm.ac.mulungushi.registrar.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/** Occupancy of one lab group. */
public class GroupCountDto {
    @SerializedName("group") public String group;
    @SerializedName("count") public int count;
    @SerializedName("capacity") public int capacity;
}
