package zm.ac.mulungushi.registrar.data.sync;

import android.content.Context;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

/**
 * Starts the background sync. WorkManager keeps the job across restarts, waits for a connection,
 * and retries with growing delays. The manual "Sync now" button calls the same method.
 */
public final class SyncScheduler {

    private static final String WORK_NAME = "registrar-sync";

    private SyncScheduler() {}

    public static void enqueue(Context context) {
        Constraints needsNetwork = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(needsNetwork)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .build();
        // KEEP: a second tap while one is queued does not create a duplicate job
        WorkManager.getInstance(context).enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.KEEP, request);
    }
}
