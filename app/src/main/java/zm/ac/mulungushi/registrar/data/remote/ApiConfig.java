package zm.ac.mulungushi.registrar.data.remote;

/**
 * Where the Node.js server lives. The Android app talks only to this API, never to MySQL.
 *
 * 10.0.2.2 is the emulator's name for the computer it runs on. For a real phone or the final
 * hand-in, use the server's HTTPS address instead (the brief requires HTTPS for the remote backend).
 * Cleartext http:// is blocked by Android 9+ unless a network security config allows it.
 */
public final class ApiConfig {

    public static final String BASE_URL = "https://10.0.2.2:3000/api/";

    private ApiConfig() {}
}
