package com.solver.googleinterviewprep;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LifecycleService;

public class SampleBoundService extends LifecycleService {
    IBinder theBinder = new TheBinder();

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        Log.i("SampleBoundService", "onBind");
        return theBinder;
    }

    public class TheBinder extends Binder {
        SampleBoundService getService() {
            return SampleBoundService.this;
        }
    }

    public int doSum(int a, int b) {
        return a + b;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SampleBoundService", "onDestroy");
    }
}