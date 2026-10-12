package zm.ac.mulungushi.registrar.data.remote;

/**
 * Keeps the session token in memory only. It is never written to disk and is cleared on sign-out,
 * so closing the app or logging out leaves no usable credential on the phone.
 * (To survive a restart, move it to EncryptedSharedPreferences / Keystore; do not use plain storage.)
 */
public final class TokenStore {

    private static volatile String token;
    private static volatile String accountId;

    private TokenStore() {}

    public static void set(String newToken, String newAccountId) {
        token = newToken;
        accountId = newAccountId;
    }

    public static String token() {
        return token;
    }

    public static String accountId() {
        return accountId;
    }

    public static void clear() {
        token = null;
        accountId = null;
    }
}
