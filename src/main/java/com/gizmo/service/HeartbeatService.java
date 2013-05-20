package com.gizmo.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.gizmo.device.DeviceId;
import com.gizmo.request.HeartbeatRequest;

public class HeartbeatService extends Service {

    final private static int DELAY = 2000;
    final private static String TAG = "HeartbeatService";

    private Handler handler;
    private String deviceId;
    private String key;

    private HeartbeatServiceMode mode;

    final private Runnable task = new Runnable() {
        public void run() {
            new HeartbeatRequest().execute(HeartbeatService.this.key, HeartbeatService.this.deviceId);
            if (HeartbeatService.this.mode.equals(HeartbeatServiceMode.DEBUG)) {
                Log.d(TAG, "Heartbeat for " + HeartbeatService.this + " sent");
            }
            handler.postDelayed(this, DELAY);
        }
    };

    public static void start(Activity activity, String key, HeartbeatServiceMode mode) {
        Intent intent = new Intent(activity, HeartbeatService.class);
        intent.putExtra("key", key);
        intent.putExtra("mode", mode);
        activity.startService(intent);
    }

    public static void stop(Activity activity) {
        Intent intent = new Intent(activity, HeartbeatService.class);
        activity.stopService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.key = intent.getStringExtra("key");
        this.mode = (HeartbeatServiceMode) intent.getSerializableExtra("mode");
        this.deviceId = new DeviceId(this).toString();
        this.handler = new Handler();
        this.handler.postDelayed(task, DELAY);
        if (this.mode.equals(HeartbeatServiceMode.DEBUG)) {
            Log.d(TAG, "Service for " + this + " started...");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (this.mode.equals(HeartbeatServiceMode.DEBUG)) {
            Log.d(TAG, "Service for " + this + " stopped...");
        }
        this.handler.removeCallbacks(task);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public String toString() {
        return key + "/" + deviceId;
    }
}
