package com.solver.googleinterviewprep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ServiceActivity extends AppCompatActivity {
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    public void clickOnStartServiceButton(View view) {
        this.serviceIntent = new Intent(this, SampleStartedService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        super.onDestroy();
    }
}