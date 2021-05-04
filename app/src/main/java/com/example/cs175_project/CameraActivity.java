package com.example.cs175_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.view.CameraController;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.camera.view.video.OnVideoSavedCallback;
import androidx.camera.view.video.OutputFileOptions;
import androidx.camera.view.video.OutputFileResults;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {
    final static String TAG = "CameraActivity";
    final static long TOTAL_TIME = 10000;
    private long startTime;
    private PreviewView mPreviewView;
    private MaterialButton mRecordButton;
    private LinearProgressIndicator mProgress;
    private CameraController mController;
    private OutputFileOptions mOptions;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mPreviewView = findViewById(R.id.preview_view);
        mRecordButton = findViewById(R.id.btn_record);
        mProgress = findViewById(R.id.progress);
        mHandler = new Handler();
        setupController();
        setupOutput();
        setupHandler();
        setButtonStartRecord();
    }

    private void setupHandler() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                long duration = System.currentTimeMillis() - startTime;
                if (duration >= TOTAL_TIME) {
                    runOnUiThread(() -> mProgress.setProgress(100));
                    stopRecord();
                    return;
                }
                runOnUiThread(() -> mProgress.setProgress((int) (duration * 100 / TOTAL_TIME), true));
                mHandler.postDelayed(this, 100);
            }
        };
    }


    private void setButtonStartRecord() {
        mRecordButton.setOnClickListener(view -> startRecord());
        mRecordButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_videocam_24));
    }

    private void setButtonStopRecord() {
        mRecordButton.setOnClickListener(view -> stopRecord());
        mRecordButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_stop_24));
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void setupController() {
        mController = new LifecycleCameraController(this);
        ((LifecycleCameraController) mController).bindToLifecycle(this);
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        mController.setCameraSelector(cameraSelector);
        mPreviewView.setController(mController);
        mController.setEnabledUseCases(CameraController.VIDEO_CAPTURE);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void setupOutput() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "video_" + System.currentTimeMillis());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

        mOptions = OutputFileOptions.builder(
                getContentResolver(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                contentValues).build();
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void startRecord() {

        mController.startRecording(mOptions,
                Executors.newSingleThreadExecutor(),
                new OnVideoSavedCallback() {
                    @Override
                    public void onVideoSaved(@NonNull OutputFileResults outputFileResults) {
                        Log.d(TAG, "onSaved");
                    }

                    @Override
                    public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                        Log.d(TAG, message);
                    }
                }
        );
        setButtonStopRecord();
        startTime = System.currentTimeMillis();
        mHandler.post(mRunnable);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void stopRecord() {
        mHandler.removeCallbacks(mRunnable);
        runOnUiThread(() -> mProgress.setProgress(0));
        mController.stopRecording();
        setButtonStartRecord();
    }


}