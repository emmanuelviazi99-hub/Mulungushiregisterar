package zm.ac.mulungushi.registrar.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/** identity = student number (9 digits) or lecturer email. The password is sent once and never saved on the phone. */
public class LoginRequest {
    @SerializedName("identity") public String identity;
    @SerializedName("password") public String password;

    public LoginRequest(String identity, String password) {
        this.identity = identity;
        this.password = password;
    }
}
