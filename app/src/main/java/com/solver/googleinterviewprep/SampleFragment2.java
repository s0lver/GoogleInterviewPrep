package com.solver.googleinterviewprep;

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

import com.google.android.material.snackbar.Snackbar;


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
}