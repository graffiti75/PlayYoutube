package com.example.rodrigo.playyoutube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Source: http://www.androidhive.info/2014/12/how-to-play-youtube-video-in-android-app/
 */
public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {

    //--------------------------------------------------
    // Constants
    //--------------------------------------------------

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    //--------------------------------------------------
    // Attributes
    //--------------------------------------------------

    private YouTubePlayerView mYouTubeView;
    private Button mPlayButton;

    private YouTubePlayer mYouTubePlayer;
    private Boolean mClicked = false;

    //--------------------------------------------------
    // Activity Life Cycle
    //--------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mYouTubeView = (YouTubePlayerView)findViewById(R.id.youtube_view);
        mPlayButton = (Button)findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(this);

        // Initializing video player with developer key.
        mYouTubeView.initialize(Config.DEVELOPER_KEY, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action.
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    //--------------------------------------------------
    // Methods
    //--------------------------------------------------

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    //--------------------------------------------------
    // YouTubePlayer.OnInitializedListener
    //--------------------------------------------------

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        mYouTubePlayer = player;
        if (!wasRestored) {
            // loadVideo() will auto play video.
            // Use cueVideo() method, if you don't want to play it automatically.
            player.loadVideo(Config.YOUTUBE_VIDEO_CODE);

            // Hiding player controls.
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
    }

    //--------------------------------------------------
    // Click Listener
    //--------------------------------------------------

    @Override
    public void onClick(View view) {
        mClicked = !mClicked;
        if (mClicked) {
            mYouTubePlayer.pause();
        } else {
            mYouTubePlayer.play();
        }
    }
}