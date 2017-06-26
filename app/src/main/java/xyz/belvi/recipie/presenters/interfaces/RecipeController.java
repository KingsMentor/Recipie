package xyz.belvi.recipie.presenters.interfaces;

import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.views.adapters.RecipeAdapter;

/**
 * Created by zone2 on 6/18/17.
 */

public interface RecipeController {

    void recipeAdapter(RecipeAdapter adapter);

    void recipeSelected(Recipe recipe);

    void failedToLoad(String errorMessage);

    void loadingRecipes();
}
