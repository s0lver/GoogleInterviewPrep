package com.solver.googleinterviewprep;

import static com.solver.googleinterviewprep.Utils.createUrlString;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String tag = "clickOnMakeRequest";
    // https://www.dev2qa.com/android-thread-message-looper-handler-example/
    // gold https://stackoverflow.com/a/34522758
    private Handler mainThreadHandler;
    private LooperThread looperThread;
    private EditText txtOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtOutput = MainActivity.this.findViewById(R.id.txtOutput);

        this.mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String newText = txtOutput.getText() + ", done you " + msg.what;
                txtOutput.setText(newText);

            }
        };

        this.looperThread = new LooperThread();
        looperThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        looperThread.handler.getLooper().quit();
    }

    public void doStuffWithLooper(View v) {
        Message message = new Message();
        message.what = 1;

        Bundle messageBundle = new Bundle();
        messageBundle.putString("name", "Rafael");
        messageBundle.putInt("age", 35);

        message.setData(messageBundle);

        looperThread.handler.sendMessage(message);
        looperThread.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("here", "arbitrary message in arbitrary runnable in " + Thread.currentThread()
                    .getName());
            }
        }, 3000);
    }

    public void doStuffWithAsyncTask(View v) {
        MyAsyncTask task = new MyAsyncTask();
        task.execute(5);
    }

    private class LooperThread extends Thread {
        public Handler handler;

        LooperThread() {
            super("worker thread");
        }

        @Override
        public void run() {
            // This attaches a looper to current thread
            Looper.prepare();

            // the handler to receive messages
            handler = new Handler(Looper.myLooper(), new Handler.Callback() {
                // This will help to process the messages received by the handler
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    Bundle messageData = message.getData();

                    Log.i("LooperThread",
                        "Got " + messageData.getString("name") + ", " +
                            messageData.getInt("age") + " in " +
                            Thread.currentThread().getName()
                    );

                    Message newMessage = new Message();
                    newMessage.what = message.what;

                    // We inject some waiting time here in THIS THREAD to simulate delay. In the meantime see that you can still use the UI
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Send the message back to main thread message queue use main thread message Handler.
                    mainThreadHandler.sendMessage(newMessage);
                    return true;
                }
            });

            Looper.loop();
        }
    }

    class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {
        // AsyncTask will automatically create a thread to execute whatever we instruct in
        // doInBackground
        // From there you are in safe ground. However docs say you should not do stuff that is way
        // to long with async tasks but prefer other methods.
        // Calling publishProgress in doInBackground will call onProgressUpdate. The beauty is that
        // onProgressUpdate is executed by the caller thread (i.e., UI thread), so from there you
        // can safely call your UI and update it
        // You also have the onPostExecute executed at the end of the successful processing also in the UI thread.
        // You have support to cancel the exec in doInBackground, you can check for isCancelled()
        @Override
        protected Void doInBackground(Integer... seconds) {
            for (int i = 0; i < seconds[0]; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress((i + 1) * 100 / seconds[0]);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // This runs on UI thread
            final String newText = txtOutput.getText() + " " + values[0];
            txtOutput.setText(newText);
        }

        @Override
        protected void onPostExecute(Void unused) {
            Snackbar.make(txtOutput, "Done!", Snackbar.LENGTH_LONG).show();
        }
    }

    public void clickOnMakeRequest(View view) {
        new Thread(() -> {
            try {

                URL url = new URL(createUrlString());
                Log.i(tag, "Asking to " + url);

                HttpURLConnection client = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(client.getInputStream());
                int readByte;
                StringBuilder sb = new StringBuilder();

                while ((readByte = in.read()) != -1) {
                    sb.append((char) readByte);
                }

                Log.i(tag, sb.toString());
                Snackbar.make(view, sb.toString(), Snackbar.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e(tag, e.getMessage());
            }
        }).start();
    }

    public void clickOnMakeRequestOkHttp(View view) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Builder builder = new Builder();

            Request request = builder.url(createUrlString()).build();

            try {
                Response response = client.newCall(request).execute();
                if (response.body() != null) {
                    String responseAsString = response.body().string();
                    Snackbar.make(view, responseAsString, Snackbar.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void clickOnLaunchFragActivity(View view) {
        startActivity(new Intent(this.getApplicationContext(), ActivityWithFragments.class));
    }

    public void clickOnLaunchServiceActivity(View view){
        startActivity(new Intent(this, ServiceActivity.class));
    }
}