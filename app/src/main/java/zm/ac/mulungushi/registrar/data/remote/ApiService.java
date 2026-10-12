package zm.ac.mulungushi.registrar.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import zm.ac.mulungushi.registrar.data.remote.dto.GroupCountDto;
import zm.ac.mulungushi.registrar.data.remote.dto.LoginRequest;
import zm.ac.mulungushi.registrar.data.remote.dto.LoginResponse;
import zm.ac.mulungushi.registrar.data.remote.dto.RegisterRequest;
import zm.ac.mulungushi.registrar.data.remote.dto.StudentDto;
import zm.ac.mulungushi.registrar.data.remote.dto.StudentPage;
import zm.ac.mulungushi.registrar.data.remote.dto.SyncRequest;
import zm.ac.mulungushi.registrar.data.remote.dto.SyncResponse;

/**
 * The REST contract with the Node.js server (see backend/README.md for roles and error codes).
 * Retrofit runs these off the main thread when you use enqueue(); call execute() only from a worker.
 */
public interface ApiService {

    // ---- auth ----
    @POST("auth/register")
    Call<LoginResponse> register(@Body RegisterRequest body);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);

    // ---- students ----
    /** Lecturer: paginated roster with combined search / programme / group filters. */
    @GET("students")
    Call<StudentPage> listStudents(@Query("search") String search,
                                   @Query("programme") String programme,
                                   @Query("group") String group,
                                   @Query("page") int page,
                                   @Query("pageSize") int pageSize);

    /** Student: my own profile only. */
    @GET("students/me")
    Call<StudentDto> myProfile();

    @POST("students")
    Call<StudentDto> createStudent(@Header("Idempotency-Key") String operationId, @Body StudentDto body);

    @PUT("students/{id}")
    Call<StudentDto> updateStudent(@Header("Idempotency-Key") String operationId,
                                   @Path("id") String id, @Body StudentDto body);

    @DELETE("students/{id}")
    Call<Void> deleteStudent(@Header("Idempotency-Key") String operationId, @Path("id") String id);

    // ---- groups ----
    @GET("groups")
    Call<List<GroupCountDto>> groupCounts();

    // ---- sync ----
    /** Sends queued operations in order; the server answers per operation (OK, CONFLICT, GROUP_FULL, ...). */
    @POST("sync")
    Call<SyncResponse> push(@Body SyncRequest body);

    /** Pulls changes (including deletion markers) after the given version. */
    @GET("sync/changes")
    Call<SyncResponse> pull(@Query("since") long sinceVersion);
}
