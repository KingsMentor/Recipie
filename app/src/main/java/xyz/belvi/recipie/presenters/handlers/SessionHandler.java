package xyz.belvi.recipie.presenters.handlers;

import android.support.v4.media.session.MediaSessionCompat;

import com.google.android.exoplayer2.SimpleExoPlayer;

public class SessionHandler extends MediaSessionCompat.Callback {

    private SimpleExoPlayer mExoPlayer;

    public SessionHandler(SimpleExoPlayer exoPlayer) {
        mExoPlayer = exoPlayer;
    }

    @Override
    public void onPlay() {
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onSkipToNext() {
        super.onSkipToNext();
    }

    @Override
    public void onPause() {
        mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onSkipToPrevious() {
        mExoPlayer.seekTo(0);
    }
}