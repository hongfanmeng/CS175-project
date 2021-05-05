package com.example.cs175_project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.button.MaterialButton;

import static android.app.Activity.RESULT_OK;

public class UploadPreviewActivity extends AppCompatActivity {

    final static int REQUEST_TAKE_GALLERY_VIDEO = 1001;
    private final static String TAG = "UploadPreviewFragment";
    ImageButton mPrevButton, mNextButton;
    private PlayerView mPlayerView;
    SimpleExoPlayer mPlayer;
    Uri mVideoUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_preview);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mPrevButton = findViewById(R.id.btn_prev);
        mNextButton = findViewById(R.id.btn_next);

        mPrevButton.setOnClickListener(view -> finish());
        mNextButton.setOnClickListener((view) -> nextStep());

        mPlayerView = findViewById(R.id.video_view);
        mPlayer = new SimpleExoPlayer.Builder(this).build();
        mPlayerView.setPlayer(mPlayer);

        videoSelect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                mVideoUri = data.getData();
                playVideo(mVideoUri);
            }
        }
    }

    private void videoSelect() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    private void playVideo(Uri uri) {
        MediaItem mediaItem = MediaItem.fromUri(uri);
        mPlayer.setMediaItem(mediaItem);
        mPlayer.prepare();
        mPlayer.play();
    }

    private void nextStep() {
        Intent intent = new Intent(UploadPreviewActivity.this, CoverChooseActivity.class);
        intent.putExtra("videoUri", mVideoUri.toString());
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}