package zm.ac.mulungushi.registrar.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/** Student registration. The claim code proves the student owns that student number. */
public class RegisterRequest {
    @SerializedName("studentNumber") public String studentNumber;
    @SerializedName("claimCode") public String claimCode;
    @SerializedName("name") public String name;
    @SerializedName("email") public String email;
    @SerializedName("phone") public String phone;
    @SerializedName("nrc") public String nrc;
    @SerializedName("programme") public String programme;
    @SerializedName("password") public String password;
}
