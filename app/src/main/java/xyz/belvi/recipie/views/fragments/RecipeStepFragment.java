package xyz.belvi.recipie.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.models.pojos.RecipeStep;
import xyz.belvi.recipie.presenters.interfaces.IntentKeys;

/**
 * Created by zone2 on 6/19/17.
 */

public class RecipeStepFragment extends Fragment {


    public RecipeStepFragment newInstance(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentKeys.RecipeIntentKey, recipe);
        setArguments(bundle);
        return this;
    }

    public RecipeStepFragment newInstance(RecipeStep recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentKeys.RecipeIntentStepKey, recipe);
        setArguments(bundle);
        return this;
    }

    public RecipeStepFragment newInstance(Recipe recipe, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentKeys.RecipeIntentKey, recipe);
        bundle.putInt(IntentKeys.RecipeIntentPositionKey, position);
        setArguments(bundle);
        return this;
    }

    protected Recipe getRecipe() {
        return getArguments().getParcelable(IntentKeys.RecipeIntentKey);
    }

    protected RecipeStep getRecipeStep() {
        return getArguments().getParcelable(IntentKeys.RecipeIntentStepKey);
    }

    protected int getPosition() {
        return getArguments().getInt(IntentKeys.RecipeIntentPositionKey);
    }
}
