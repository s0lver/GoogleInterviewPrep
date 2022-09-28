package com.solver.googleinterviewprep;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LifecycleService;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class SampleStartedService extends LifecycleService {
    public SampleStartedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        super.onBind(intent);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SampleService", "On start command");
        doTask();
        return super.onStartCommand(intent, flags, startId);
    }

    private void doTask() {
        WorkRequest request = new OneTimeWorkRequest.Builder(WorkTask.class).build();

        WorkManager workerManagerInstance = WorkManager.getInstance(this);

        workerManagerInstance.enqueue(request);

        workerManagerInstance.getWorkInfoByIdLiveData(request.getId())
            .observe(this, workInfo -> {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    Data outputData = workInfo.getOutputData();

                    Log.i("SampleService", outputData.getString("body"));
                }
            });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SampleService", "Creating service");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SampleService", "Destroying service");
    }
}