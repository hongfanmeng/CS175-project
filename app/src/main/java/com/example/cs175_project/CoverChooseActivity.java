package com.example.cs175_project;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class CoverChooseActivity extends AppCompatActivity {

    private ImageView mImageView;
    private final static String TAG = "CoverChooseFragment";
    private Uri mVideoUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_choose);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mImageView = findViewById(R.id.image_view);

        MaterialButton autoButton = findViewById(R.id.btn_auto);
        autoButton.setOnClickListener((view) -> mImageView.setImageBitmap(getVideoThumb()));


        MaterialButton manualButton = findViewById(R.id.btn_manual);
        manualButton.setOnClickListener((view) -> mImageView.setImageBitmap(manualSelect()));


        MaterialButton uploadButton = findViewById(R.id.btn_upload);
        uploadButton.setOnClickListener((view) -> upload());

        ImageButton prevButton = findViewById(R.id.btn_prev);
        prevButton.setOnClickListener((view) -> finish());


        mVideoUri = Uri.parse(getIntent().getStringExtra("videoUri"));

        mImageView.setImageBitmap(getVideoThumb());
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private Bitmap getVideoThumb() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, mVideoUri);
        Bitmap bmp = mmr.getFrameAtTime();

        int videoWidth = bmp.getWidth();
        int videoHeight = bmp.getHeight();

        if (videoWidth > videoHeight) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (videoWidth < videoHeight) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        return bmp;
    }

    private Bitmap manualSelect() {
        return null;
    }

    private void upload() {

    }

}