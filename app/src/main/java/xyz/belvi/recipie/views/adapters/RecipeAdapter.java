package xyz.belvi.recipie.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xyz.belvi.recipie.R;
import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.views.holders.RecipeHolder;

/**
 * Created by zone2 on 6/18/17.
 */

public abstract class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecipeHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_items, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecipeHolder recipeHolder, final int position) {
        final Recipe recipe = getRecipe(position);
        recipeHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeSelected(recipe);
            }
        });

        recipeHolder.getRecipeName().setText(recipe.getName());
        Glide.with(recipeHolder.getContext()).load(recipe.getImage()).into(recipeHolder.getRecipeImg());
        recipeHolder.getServing().setText(String.format(recipeHolder.getContext().getString(R.string.servings), String.valueOf(recipe.getServings())));
        recipeHolder.getIngredients().setText(String.format(recipeHolder.getContext().getString(R.string.ingredient), String.valueOf(recipe.getIngredients().size())));
        recipeHolder.getSteps().setText(String.format(recipeHolder.getContext().getString(R.string.steps), String.valueOf(recipe.getSteps().size())));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    private Recipe getRecipe(int position) {
        return recipes.get(position);
    }

    public void updateItems(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public abstract void recipeSelected(Recipe recipe);
}
