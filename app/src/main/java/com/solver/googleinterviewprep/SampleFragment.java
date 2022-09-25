package com.solver.googleinterviewprep;

import static com.solver.googleinterviewprep.Utils.createUrlString;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SampleFragment extends Fragment {
    ExecutorService service;

    public SampleFragment() {
        this.service = Executors.newFixedThreadPool(1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnDoMagic = view.findViewById(R.id.btnFrag1DoMagic);
        btnDoMagic.setOnClickListener(this::onDoMagicButtonClick);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    public void onDoMagicButtonClick(View view) {
        Snackbar.make(view, "Trying to make a web request", Snackbar.LENGTH_LONG).show();

        this.service.execute(() -> {
            Request request = new Request.Builder().url(createUrlString()).build();
            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                if (response.body() != null) {
                    Snackbar.make(view, Objects.requireNonNull(response.body())
                        .string(), Snackbar.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}