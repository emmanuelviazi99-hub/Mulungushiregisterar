package zm.ac.mulungushi.registrar.data.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import zm.ac.mulungushi.registrar.data.local.AppDatabase;
import zm.ac.mulungushi.registrar.data.local.dao.PendingOperationDao;
import zm.ac.mulungushi.registrar.data.local.entity.PendingOperationEntity;
import zm.ac.mulungushi.registrar.data.remote.RetrofitClient;
import zm.ac.mulungushi.registrar.data.remote.TokenStore;
import zm.ac.mulungushi.registrar.data.remote.dto.SyncRequest;
import zm.ac.mulungushi.registrar.data.remote.dto.SyncResponse;

/**
 * Sends the queued operations to the server, in order, then stores each server answer with its change.
 * Runs on a WorkManager background thread, so blocking calls are fine here.
 *
 * Safe to retry: every operation keeps the same operationId, and the server answers a repeated id from
 * its receipt instead of applying it twice.
 */
public class SyncWorker extends Worker {

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String account = TokenStore.accountId();
        if (account == null) {
            // signed out or session expired: do nothing, and never replay under another user
            return Result.success();
        }
        PendingOperationDao dao = AppDatabase.get(getApplicationContext()).pendingOperationDao();
        List<PendingOperationEntity> queue = dao.pendingFor(account);
        if (queue.isEmpty()) return Result.success();

        SyncRequest body = new SyncRequest();
        body.operations = new ArrayList<>();
        for (PendingOperationEntity op : queue) {
            SyncRequest.Operation o = new SyncRequest.Operation();
            o.operationId = op.operationId;
            o.studentId = op.studentId;
            o.type = op.type;
            o.payload = op.payload;
            o.baseVersion = op.baseVersion;
            body.operations.add(o);
        }

        try {
            Response<SyncResponse> response = RetrofitClient.api().push(body).execute();
            if (response.code() == 401) {
                // session expired: pause sync until the user signs in again
                return Result.success();
            }
            if (!response.isSuccessful() || response.body() == null || response.body().results == null) {
                return Result.retry();
            }
            for (SyncResponse.Result r : response.body().results) {
                String status = "OK".equals(r.status) ? "DONE"
                        : "CONFLICT".equals(r.status) ? "CONFLICT" : "REJECTED";
                dao.finish(r.operationId, status, r.status);
            }
            return Result.success();
        } catch (IOException offlineOrLostResponse) {
            // keep the queue as it is; the same operation ids are sent again next time
            return Result.retry();
        }
    }
}
