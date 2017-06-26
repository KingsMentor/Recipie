package xyz.belvi.recipie.views.holders;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import xyz.belvi.recipie.R;

/**
 * Created by zone2 on 6/18/17.
 */

public class RecipeHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView steps, ingredients, serving, recipeName;
    private AppCompatImageView recipeImg;
    private Context context;

    public RecipeHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        steps = (AppCompatTextView) itemView.findViewById(R.id.steps);
        ingredients = (AppCompatTextView) itemView.findViewById(R.id.ingredients);
        serving = (AppCompatTextView) itemView.findViewById(R.id.serving);
        recipeName = (AppCompatTextView) itemView.findViewById(R.id.recipe_name);
        recipeImg = (AppCompatImageView) itemView.findViewById(R.id.recipe_img);
    }

    public AppCompatImageView getRecipeImg() {
        return this.recipeImg;
    }

    public AppCompatTextView getSteps() {
        return this.steps;
    }

    public AppCompatTextView getIngredients() {
        return this.ingredients;
    }

    public AppCompatTextView getServing() {
        return this.serving;
    }

    public AppCompatTextView getRecipeName() {
        return this.recipeName;
    }

    public Context getContext() {
        return this.context;
    }
}
