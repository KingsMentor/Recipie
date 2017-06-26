package xyz.belvi.recipie.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.pojos.RecipeStep;

/**
 * Created by zone2 on 6/20/17.
 */

public class RecipeStepDetailsDescFragment extends RecipeStepFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_video_and_desc, container, false);
        RecipeStep recipeStep = getRecipeStep();
        initStepDescription(rootView, recipeStep);
        return rootView;

    }

    private void initStepDescription(View view, RecipeStep recipeStep) {
        AppCompatTextView recipeDesc = (AppCompatTextView) view.findViewById(R.id.step_details);
        recipeDesc.setText(recipeStep.getDescription());
    }
}
