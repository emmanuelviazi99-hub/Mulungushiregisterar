package zm.ac.mulungushi.registrar.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** One page of the lecturer roster. */
public class StudentPage {
    @SerializedName("items") public List<StudentDto> items;
    @SerializedName("page") public int page;
    @SerializedName("pageSize") public int pageSize;
    @SerializedName("total") public int total;
}
