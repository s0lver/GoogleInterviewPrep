package com.solver.googleinterviewprep;

import static com.solver.googleinterviewprep.Utils.createUrlString;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WorkTask extends Worker {

    public WorkTask(@NonNull Context context,
        @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.i("WorkTask", "Input data is " + workerParams.getInputData().toString());
    }

    @NonNull
    @Override
    public Result doWork() {
        Request request = new Request.Builder().url(createUrlString()).build();
        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                return Result.success(new Data.Builder().putString("body", response.body()
                    .string()).build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.failure(new Data.Builder().putString("body",
            "Somebody failed and it was not me").build());
    }
}