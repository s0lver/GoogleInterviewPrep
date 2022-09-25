package com.solver.googleinterviewprep;

import static com.solver.googleinterviewprep.Utils.createUrlString;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SampleFragment2 extends Fragment {

    public SampleFragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnDoMagic = view.findViewById(R.id.btnFrag2DoMagic);
        btnDoMagic.setOnClickListener(this::onDoMagicButtonClick);
    }

    public void onDoMagicButtonClick(View view) {
        WorkRequest request = new OneTimeWorkRequest.Builder(WorkTask.class).build();

        WorkManager workerManagerInstance = WorkManager.getInstance(this.getActivity());

        workerManagerInstance.enqueue(request);

        workerManagerInstance.getWorkInfoByIdLiveData(request.getId()).observe(this, workInfo -> {
            if (workInfo != null && workInfo.getState().isFinished()) {
                Data outputData = workInfo.getOutputData();

                Snackbar.make(view, outputData.getString("body"), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static class WorkTask extends Worker {

        public WorkTask(@NonNull Context context,
            @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
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
}