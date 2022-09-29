package com.solver.googleinterviewprep;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class ServiceActivity extends AppCompatActivity {
    Intent startedServiceIntent;
    Intent boundServiceIntent;
    SampleBoundService boundServiceReference;

    ServiceConnection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    public void clickOnStartServiceButton(View view) {
        this.startedServiceIntent = new Intent(this, SampleStartedService.class);
        startService(startedServiceIntent);
    }

    public void clickOnStartBoundServiceButton(View view) {
        if (boundServiceReference == null) {
            connection = createServiceConnection();

            boundServiceIntent = new Intent(this, SampleBoundService.class);
            bindService(boundServiceIntent, connection, Context.BIND_AUTO_CREATE);
        } else {
            Log.i("ServiceActivity", "Service already running");
        }
    }

    public void clickOnDoOperation(View view) {
        if (boundServiceReference == null) {
            Snackbar.make(view, "Hold on", Snackbar.LENGTH_SHORT).show();
        } else {
            int r = boundServiceReference.doSum(1, 2);
            Snackbar.make(view, r + "", Snackbar.LENGTH_LONG).show();
        }
    }

    public void clickOnStopBoundServiceButton(View view) {
        if (boundServiceReference != null) {
            unbindService(connection);
            connection = null;
            // boundServiceReference.unbindService(connection);
            Log.i("ServiceActivity", "Service is not bound anymore");
            boundServiceReference = null;
        }
    }

    private ServiceConnection createServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                SampleBoundService.TheBinder theBinder = (SampleBoundService.TheBinder) binder;
                ServiceActivity.this.boundServiceReference = theBinder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("ServiceConnection", "onServiceDisconnected");

                // ServiceActivity.this.boundServiceReference = null;
            }
        };
    }


    @Override
    protected void onDestroy() {
        if (startedServiceIntent != null) {
            stopService(startedServiceIntent);
        }
        super.onDestroy();
    }
}