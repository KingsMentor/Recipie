package xyz.belvi.recipie.presenters.interfaces;

import android.support.v4.view.ViewPager;

import com.google.android.exoplayer2.SimpleExoPlayer;

/**
 * Created by zone2 on 6/21/17.
 */

public interface RecipeStepsController extends ViewPager.OnPageChangeListener {
    void onPlaying();

    void onPlayerRelease();


    void setPlayer(SimpleExoPlayer exoPlayer);
}
