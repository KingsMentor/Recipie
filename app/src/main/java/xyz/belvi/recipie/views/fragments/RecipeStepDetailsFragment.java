package xyz.belvi.recipie.views.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.models.pojos.RecipeStep;
import xyz.belvi.recipie.presenters.handlers.RecipeStepDetailsHandler;
import xyz.belvi.recipie.presenters.interfaces.IntentKeys;
import xyz.belvi.recipie.presenters.interfaces.RecipeStepsController;

/**
 * Created by zone2 on 6/19/17.
 */

public class RecipeStepDetailsFragment extends RecipeStepFragment implements RecipeStepsController {


    private RecipeStepDetailsHandler mRecipeStepDetailsHandler = new RecipeStepDetailsHandler();
    private SimpleExoPlayerView mPlayerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_details, container, false);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.recipe_vid_player);

        mRecipeStepDetailsHandler.init(getContext(), this);
        Recipe recipe = getRecipe();
        int position = getPosition();

        if (!getResources().getBoolean(R.bool.landscape) || getResources().getBoolean(R.bool.is_tab)) {
            TabLayout recipeTabLayout = (TabLayout) rootView.findViewById(R.id.tl_recipe);
            recipeTabLayout.setVisibility(getResources().getBoolean(R.bool.is_tab) ? View.GONE : View.VISIBLE);

            for (int index = 0; index < recipe.getSteps().size(); index++) {
                TabLayout.Tab tab = recipeTabLayout.newTab();
                tab.setText("Step " + index);
                recipeTabLayout.addTab(tab);
            }
            recipeTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            recipeTabLayout.setTabTextColors(ContextCompat.getColor(getContext(), R.color.tab_color), ContextCompat.getColor(getContext(), R.color.white));
            final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.vp_recipe);

            recipeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(recipeTabLayout));
            viewPager.addOnPageChangeListener(this);
            viewPager.setAdapter(getResources().getBoolean(R.bool.is_tab) ? new RecipeStepPagerAdapter(recipe.getSteps().get(position), getFragmentManager()) : new RecipeStepPagerAdapter(recipe, getChildFragmentManager()));
            viewPager.setCurrentItem(getResources().getBoolean(R.bool.is_tab) ? 0 : position);

            if (savedInstanceState != null) {
                long currentPosition = savedInstanceState.getLong(IntentKeys.PlayerCurrentPosition);
                mRecipeStepDetailsHandler.seek(currentPosition);
            }
        } else {
            getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        }

        return rootView;


    }


    public void loadThumbnail(RecipeStep recipeStep) {
        Glide.with(getContext()).load(Uri.parse(recipeStep.getThumbnailURL()))
                .asBitmap().listener(new RequestListener<Uri, Bitmap>() {
            @Override
            public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                mPlayerView.setDefaultArtwork(resource);
                return false;
            }
        }).preload();

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPlayerRelease() {
    }

    @Override
    public void onStop() {
        super.onStop();
        mRecipeStepDetailsHandler.release();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecipeStepDetailsHandler.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecipeStepDetailsHandler.release();
    }


    @Override
    public void setPlayer(SimpleExoPlayer exoPlayer) {
        mPlayerView.setPlayer(exoPlayer);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        loadThumbnail(getRecipe().getSteps().get(position));
        mRecipeStepDetailsHandler.loadMediaContent(getRecipe().getSteps().get(position));

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private class RecipeStepPagerAdapter extends FragmentStatePagerAdapter {

        private Recipe recipe;
        private RecipeStep recipeStep;

        public RecipeStepPagerAdapter(Recipe recipe, FragmentManager fm) {
            super(fm);
            this.recipe = recipe;
        }

        public RecipeStepPagerAdapter(RecipeStep recipeStep, FragmentManager fm) {
            super(fm);
            this.recipeStep = recipeStep;
        }

        @Override
        public Fragment getItem(int i) {
            if (getResources().getBoolean(R.bool.is_tab))
                return new RecipeStepDetailsDescFragment().newInstance(recipeStep);
            return new RecipeStepDetailsDescFragment().newInstance(recipe.getSteps().get(i));
        }

        @Override
        public int getCount() {
            if (getResources().getBoolean(R.bool.is_tab))
                return 1;
            return recipe.getSteps().size();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRecipeStepDetailsHandler.saveInstance(outState);
    }
}
