package zm.ac.mulungushi.registrar.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/** Result of a successful sign-in or registration. role is "student" or "lecturer". */
public class LoginResponse {
    @SerializedName("token") public String token;
    @SerializedName("accountId") public String accountId;
    @SerializedName("role") public String role;
    @SerializedName("studentId") public String studentId;
}
