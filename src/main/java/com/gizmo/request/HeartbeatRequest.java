package com.gizmo.request;

import android.os.AsyncTask;
import com.gizmo.backend.BackendAPI;
import com.gizmo.backend.BackendAPIException;

public class HeartbeatRequest extends AsyncTask<Object, Object, Boolean> {

    private String applicationKey;
    private String deviceId;

    public void execute(String key, String deviceId) {
        this.applicationKey = key;
        this.deviceId = deviceId;
        this.execute();
    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        try {
            BackendAPI.heartbeat(applicationKey, deviceId);
            return true;
        } catch (BackendAPIException e) {
            return null;
        }
    }
}