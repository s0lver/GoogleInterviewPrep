package com.solver.googleinterviewprep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    // https://www.dev2qa.com/android-thread-message-looper-handler-example/
    // gold https://stackoverflow.com/a/34522758
    private Handler mainThreadHandler;
    private LooperThread looperThread;
    private EditText txtOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtOutput = MainActivity.this.findViewById(R.id.txtOutput);

        mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String newText = txtOutput.getText() + ", done you " + msg.what;
                txtOutput.setText(newText);

            }
        };

        looperThread = new LooperThread();
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
                Log.i("here", "arbitrary message in arbitrary runnable in " + Thread.currentThread().getName());
            }
        }, 3000);
    }

    class LooperThread extends Thread {
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
}