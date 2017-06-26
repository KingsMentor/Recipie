package xyz.belvi.recipie.presenters.handlers;

import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.ArrayList;

import xyz.belvi.recipie.models.cache.RecipePreference;
import xyz.belvi.recipie.models.pojos.Recipe;
import xyz.belvi.recipie.presenters.app.RecipeApplication;
import xyz.belvi.recipie.presenters.interfaces.RecipeController;
import xyz.belvi.recipie.presenters.network.requests.RecipeRequest;
import xyz.belvi.recipie.views.adapters.RecipeAdapter;

/**
 * Created by zone2 on 6/18/17.
 */

public class RecipeDataHandler {

    private RecipeController mRecipeController;
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private RecipeAdapter recipeAdapter;

    public void init(RecipeController recipeController) {
        mRecipeController = recipeController;
        if (recipes.size() != 0)
            buildRecipeAdapter(recipes);
        loadData();
    }

    public void loadData() {
        startLoad();
        Request<ArrayList<Recipe>> request = new RecipeRequest(RecipeRequest.URL, new Response.Listener<ArrayList<Recipe>>() {
            @Override
            public void onResponse(ArrayList<Recipe> response) {
                buildRecipeAdapter(response);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleFailure(error.getMessage());
            }
        });
        RecipeApplication.getInstance().getVolleyHelper().makeNetworkCall(request);
    }

    private void buildRecipeAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        if (recipeAdapter == null) {
            recipeAdapter = new RecipeAdapter(recipes) {
                @Override
                public void recipeSelected(Recipe recipe) {
                    selectRecipe(recipe);
                }
            };
        } else {
            recipeAdapter.updateItems(recipes);
        }
        deliverAdapter(recipeAdapter);

    }

    private void deliverAdapter(RecipeAdapter adapter) {
        if (mRecipeController != null)
            mRecipeController.recipeAdapter(adapter);
    }

    private void selectRecipe(Recipe recipe) {
        if (mRecipeController != null)
            mRecipeController.recipeSelected(recipe);
    }

    private void startLoad() {
        if (mRecipeController != null)
            mRecipeController.loadingRecipes();
    }

    private void handleFailure(String errorMessage) {
        String recipeJson = new RecipePreference().getCacheRecipe(RecipeApplication.getInstance().getApplicationContext());
        try {
            ArrayList<Recipe> recipes = RecipeRequest.generateRecipeFromJson(recipeJson);
            if (recipes.size() == 0)
                reportFailure(errorMessage);
            else
                buildRecipeAdapter(recipes);
        } catch (JSONException e) {
            e.printStackTrace();

            reportFailure(errorMessage);
        }


    }

    private void reportFailure(final String errorMessage) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRecipeController != null)
                    mRecipeController.failedToLoad(errorMessage);
            }
        }, 1000);

    }
}
