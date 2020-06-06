package com.example.torchlight;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;


public class FlashService extends Service {

    private CameraManager mCameraManager;
    private String mCameraId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        turnOnFlashLight();
        return super.onStartCommand(intent, flags, startId);
    }

    public void turnOnFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        turnOffFlashLight();
    }


    public void turnOffFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


