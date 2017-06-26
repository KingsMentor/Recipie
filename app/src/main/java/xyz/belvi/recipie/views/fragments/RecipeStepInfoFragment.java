package xyz.belvi.recipie.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.pojos.Ingredient;
import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.presenters.interfaces.IntentKeys;
import xyz.belvi.recipie.views.adapters.RecipeStepsAdapter;
import xyz.belvi.recipie.views.customViews.DividerItemDecoration;

/**
 * Created by zone2 on 6/19/17.
 */

public class RecipeStepInfoFragment extends RecipeStepFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_recipe_step_info, container, false);
        if (savedInstanceState == null) {
            buildIngredientData(rootView, getRecipe());
            initSteps(rootView, getRecipe(), selectedPosition);
        } else {
            selectedPosition = savedInstanceState.getInt(IntentKeys.SelectedPositionKey);
            buildIngredientData(rootView, getRecipe());
            initSteps(rootView, getRecipe(), selectedPosition);
        }
        return rootView;
    }

    private void buildIngredientData(View view, Recipe recipe) {
        AppCompatTextView ingredientTextView = (AppCompatTextView) view.findViewById(R.id.ingredients_details);
        StringBuilder ingredientData = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientData.append("\u25CF");
            ingredientData.append("\t");
            ingredientData.append(ingredient.getIngredient());
            ingredientData.append(" ( ");
            ingredientData.append(ingredient.getQuantity() + " ");
            ingredientData.append(ingredient.getMeasure());
            ingredientData.append(" )");
            ingredientData.append("\n");
        }
        ingredientTextView.setText(ingredientData);

    }

    private void initSteps(View v, Recipe recipe, final int position) {
        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(recipe, position) {
            @Override
            public void recipeStepSelected(Recipe recipe, int position) {
                selectedPosition = position;
                if (getResources().getBoolean(R.bool.is_tab)) {
                    getFragmentManager().beginTransaction().replace(R.id.fl_recipe_step_details, new RecipeStepDetailsFragment().newInstance(recipe, position)).commitAllowingStateLoss();
                } else {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.add(R.id.fl_recipe_step, new RecipeStepDetailsFragment().newInstance(recipe, position)).commitAllowingStateLoss();
                }
            }
        };
        RecyclerView rvRecipeStep = (RecyclerView) v.findViewById(R.id.rv_recipe_step);
        rvRecipeStep.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvRecipeStep.setHasFixedSize(false);
        rvRecipeStep.setAdapter(recipeStepsAdapter);
        rvRecipeStep.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.recycler_divider), false, false));
        rvRecipeStep.setNestedScrollingEnabled(false);

    }

    int selectedPosition;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(IntentKeys.SelectedPositionKey, selectedPosition);
    }
}
