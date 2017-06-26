package xyz.belvi.recipie.views.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.models.pojos.RecipeStep;
import xyz.belvi.recipie.views.holders.RecipeStepsHolder;

/**
 * Created by zone2 on 6/18/17.
 */

public abstract class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsHolder> {
    private ArrayList<RecipeStep> recipeSteps;
    private Recipe recipe;

    public RecipeStepsAdapter(Recipe recipe, int selectedPosition) {
        this.recipe = recipe;
        this.recipeSteps = recipe.getSteps();
        this.selectedPosition = selectedPosition;
    }

    @Override
    public RecipeStepsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecipeStepsHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_step, viewGroup, false));
    }

    int selectedPosition = -1;

    @Override
    public void onBindViewHolder(RecipeStepsHolder recipeHolder, final int position) {
        final RecipeStep recipeStep = getRecipeStep(position);
        recipeHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                recipeStepSelected(recipe, position);
            }
        });


        recipeHolder.getRecipeStepInfo().setText(String.valueOf(position) + "." + recipeStep.getShortDescription());
        if (position == selectedPosition) {
            recipeHolder.getRecipeStepInfo().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_play_arrow_black_24dp, 0);
            recipeHolder.getInfoBg().setBackgroundColor(ContextCompat.getColor(recipeHolder.getContext(), R.color.colorPrimaryLight));
        } else {
            recipeHolder.getRecipeStepInfo().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_play_arrow_trans_24dp, 0);
            recipeHolder.getInfoBg().setBackgroundColor(ContextCompat.getColor(recipeHolder.getContext(), android.R.color.transparent));

        }


    }

    @Override
    public int getItemCount() {
        return recipeSteps.size();
    }

    private RecipeStep getRecipeStep(int position) {
        return recipeSteps.get(position);
    }


    public abstract void recipeStepSelected(Recipe recipe, int position);
}
