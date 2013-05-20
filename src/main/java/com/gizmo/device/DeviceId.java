package com.gizmo.device;

import android.content.Context;
import android.content.ContextWrapper;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class DeviceId {

    private ContextWrapper ctx;

    public DeviceId(ContextWrapper ctx) {
        this.ctx = ctx;
    }

    @Override
    public String toString() {
        TelephonyManager tm = (TelephonyManager) ctx.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = "" + tm.getDeviceId(), tmSerial = "" + tm.getSimSerialNumber();
        String androidId = "" + android.provider.Settings.Secure.getString(ctx.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }
}
