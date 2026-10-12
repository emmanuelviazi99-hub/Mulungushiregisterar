package zm.ac.mulungushi.registrar.data.remote;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Builds the one ApiService the repositories use. Adds the bearer token to every request. */
public final class RetrofitClient {

    private static volatile ApiService service;

    private RetrofitClient() {}

    public static ApiService api() {
        if (service == null) {
            synchronized (RetrofitClient.class) {
                if (service == null) {
                    OkHttpClient http = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .addInterceptor(new AuthInterceptor())
                            .build();
                    service = new Retrofit.Builder()
                            .baseUrl(ApiConfig.BASE_URL)
                            .client(http)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(ApiService.class);
                }
            }
        }
        return service;
    }

    private static final class AuthInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request req = chain.request();
            String token = TokenStore.token();
            if (token != null) {
                req = req.newBuilder().header("Authorization", "Bearer " + token).build();
            }
            return chain.proceed(req);
        }
    }
}
