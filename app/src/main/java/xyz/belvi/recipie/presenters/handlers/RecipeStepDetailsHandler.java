package xyz.belvi.recipie.presenters.handlers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.pojos.RecipeStep;
import xyz.belvi.recipie.presenters.interfaces.IntentKeys;
import xyz.belvi.recipie.presenters.interfaces.RecipeStepsController;

/**
 * Created by zone2 on 6/21/17.
 */

public class RecipeStepDetailsHandler implements ExoPlayer.EventListener {

    private RecipeStepsController mRecipeStepsController;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;
    private Context mContext;

    public void init(Context context, RecipeStepsController mRecipeStepsController) {
        this.mContext = context;
        this.mRecipeStepsController = mRecipeStepsController;


        initializePlayer(context);
        initializeMediaSession(context);
    }

    private void initializePlayer(Context context) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);
            mRecipeStepsController.setPlayer(mExoPlayer);


            // Prepare the MediaSource.
        }
    }

    private void initializeMediaSession(Context context) {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(context, context.getString(R.string.app_name));

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new SessionHandler(mExoPlayer));

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    public void loadMediaContent(RecipeStep recipeStep) {

        String userAgent = Util.getUserAgent(mContext, mContext.getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(recipeStep.getVideoURL()), new DefaultDataSourceFactory(
                mContext, userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object o) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {

    }

    @Override
    public void onLoadingChanged(boolean b) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    public void saveInstance(Bundle outState) {
        if (mExoPlayer != null)
            outState.putLong(IntentKeys.PlayerCurrentPosition, mExoPlayer.getCurrentPosition());
    }

    public void seek(long position) {
        mExoPlayer.seekTo(position);
    }

    public void release() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
            mMediaSession.setActive(false);
            mRecipeStepsController.onPlayerRelease();
        }
    }
}
